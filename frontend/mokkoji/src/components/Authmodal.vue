<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <div class="room">
        <div class="form-group">
          <div class="Authlogin">관리자 로그인</div>
          <div class="room-info">
            <div class="room-number">
              <div class="info">관리자 ID</div>
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
                  v-model="roomPwd"
                  class="roomname"
                  type="password"
                  required
                />
              </div>
            </div>
          </div>
        </div>
        <div class="checkbuttons">
          <button class="checkbutton" @click="joinSession()">확인</button>
          <button class="checkbutton" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
const roomId = ref("");
const roomPwd = ref(null);
const router = useRouter();
const isPasswordEnabled = ref(false);

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
  width: 1000px;
  height: 400px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1001; /* 필요에 따라 너비와 높이 설정 */
  display: flex; /* Make this a flex container */
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
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
  font-family: "DOSMyungjo";
  font-size: 50px;
}

.Authlogin {
  margin-bottom: 1%;
}
.room-info {
  width: 800px;
  height: 75px;
  background-color: #0594e0;
  display: flex;
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
  border-radius: 10px;
  margin-bottom: 10px;
}

.room-number {
  width: 90%;
  height: 80%;
  margin-left: 5%;
  display: flex;
  justify-content: center;
  align-content: center;
  font-size: 24px;
  font-family: "DOSMyungjo";
  color: white;
}

.info {
  background-color: #00acfc;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20%;
  margin-right: 3%;
  border-radius: 10px;
}
.input-value {
  height: 100%;
  width: 100%;
  background-color: #00acfc;
  border-radius: 5px;
  display: flex;
  flex-direction: row;
  align-content: center;
  justify-content: center;
}

.roomname {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1%;
  height: 80%;
  width: 100%;
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

.checkbuttons {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2%;
}

.checkbutton {
  width: 160px;
  height: 50px;
  background-color: #00acfc;
  margin: 0% 5%;
  border: #0082fc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  color: white;
  font-size: 24px;
  transition: background-color 0.3s ease;
  font-family: "DOSMyungjo";
}

.checkbutton:hover {
  background-color: #fdc909;
}
</style>
