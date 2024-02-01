<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <div class="room">
        <div class="game-info">
          <div class="game-buttons">
            <button
              :class="{ selected: gameType === 0, unselected: gameType !== 0 }"
              @click="selectGameType(0)"
            >
              몸으로 말해요
            </button>
            <button
              :class="{ selected: gameType === 1, unselected: gameType !== 1 }"
              @click="selectGameType(1)"
            >
              뮤직큐
            </button>
          </div>
        </div>
        <div class="form-group">
          <div class="room-info">
            <div class="room-number">
              <div class="info">방제목</div>
              <div class="input-value">
                <input class="roomname" v-model="roomId" type="text" required />
              </div>
            </div>
          </div>
          <div class="room-info">
            <div class="room-number">
              <div class="info">비밀번호</div>
              <div class="input-value">
                <input
                  v-if="isPasswordEnabled"
                  v-model="roomPwd"
                  class="roomname"
                  type="password"
                  required
                />
                <div v-else class="freeroom"></div>

                <div class="screet-room">
                  <input type="checkbox" v-model="isPasswordEnabled" />
                  비밀방 설정
                </div>
              </div>
            </div>
          </div>
        </div>
        <button class="btn btn-lg btn-success" @click="joinSession()">
          Join!
        </button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { ref, onMounted } from "vue";
import { useOpenViduStore } from "@/stores/openvidu";
import { useRoomStore } from "@/stores/room";
import { useRouter } from "vue-router";
const store = useOpenViduStore();
const Roomstore = useRoomStore();
const roomId = ref("");
const roomPwd = ref(null);
const router = useRouter();
const isPasswordEnabled = ref(false);
const gameType = ref(0);

const selectGameType = (type) => {
  gameType.value = type; // 선택된 게임 유형 업데이트
  console.log(gameType.value);
  console.log(typeof gameType.value);
};
// Props 정의
const props = defineProps({
  show: Boolean,
});

// Emit 함수 정의
const emit = defineEmits(["close"]);

// Methods
function closeModal() {
  emit("close");
}

// const joinSession = () => {
//   const payload = {
//     roomId: roomId.value,
//   };
//   store.joinSession(payload);
//   router.replace({ name: "TalkBody", params: { id: roomId.value } });
// }; // 프론트에서만 돌리는 코드

//테스트 코드
const joinSession = () => {
  const payload = {
    room_name: roomId.value,
    room_password: roomPwd.value,
    _private: isPasswordEnabled.value,
    game_type: 0,
  };
  Roomstore.createRoom(payload);
  Roomstore.getRoomlist();
}; //백엔드 연결시 돌리는 코드  (연결 성공)
</script>

<style scoped>
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed; /* 고정 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비 */
  height: 100%; /* 전체 높이 */
  background-color: rgba(0, 0, 0, 0.2); /* 반투명 배경 */
  z-index: 1000; /* 다른 요소들보다 상위에 위치 */
}

.modal-content {
  background-color: #0be2ff;
  text-align: center;
  width: 1218px;
  height: 630px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1001; /* 필요에 따라 너비와 높이 설정 */
  display: flex; /* Make this a flex container */
  justify-content: center; /* Center horizontally */
  align-items: start; /* Center vertically */
}

.room {
  width: 100%;
}
.game-info {
  width: 100%;
}
.game-buttons {
  margin-top: 5%;
  margin-bottom: 5%;
  width: 100%;
  height: 250px;
  /* Adjust the margin as needed */
  display: flex; /* Set as Flex container */
  justify-content: center; /* Even space around each item */
  align-items: center;
}

.unselected,
.selected {
  width: 250px;
  height: 250px;
  margin: 0% 5%;
  font-size: 72px;
  font-family: "LABdigital";
  border-radius: 5%;
  border: 0;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  transition: background-color 0.3s ease, color 0.5s ease;
}
.unselected {
  background-color: #12deff;
}

.selected {
  background-color: #fdc909; /* 예시 색상, 원하는 색상으로 변경 가능 */
}
.form-group {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.room-info {
  width: 980px;
  height: 50px;
  background-color: #0594e0;
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
  border-radius: 10px;
  margin-bottom: 10px;
}

.room-number {
  width: 90%;
  margin-left: 5%;
  margin-top: 7px;
  display: flex;
  justify-content: start;
  align-content: center;
  font-size: 32px;
  font-family: "DOSMyungjo";
  color: white;
}

.info {
  background-color: #00acfc;
  width: 20%;
  margin-right: 5%;
  border-radius: 10px;
}
.input-value {
  height: 35px;
  width: 80%;
  background-color: #00acfc;
  border-radius: 5px;
  display: flex;
  flex-direction: row;
  align-content: center;
}

.roomname {
  display: flex;
  margin: 1%;
  height: 60%;
  width: 70%;
  border-radius: 5px;
  border: none;
}

.freeroom {
  display: flex;
  margin: 1%;
  height: 60%;
  width: 70%;
  border-radius: 5px;
  border: none;
  background-color: #afafaf;
}
.screet-room {
  font-size: 24px;
}

.screet-room > input {
  width: 20px;
  height: 20px;
  margin-top: 5%;
}
</style>
