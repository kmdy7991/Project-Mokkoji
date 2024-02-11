<template>
  <div>
    <div class="game_room">
      <IndividualRoom
        v-for="room in paginatedRooms"
        :key="room.room_id || `empty-${room.index}`"
        class="welcome_itme"
        :room="room"
        @click.native="joinRoomDirectly(room.room_id, room._private)"
      />
    </div>
    <div class="buttoncontainer">
      <div class="page">
        <button
          @click="prevPage"
          :class="{ 'disabled-button': pageNumber === 0 }"
        >
          ◀
        </button>
        <button
          @click="nextPage"
          :class="{ 'disabled-button': pageNumber >= maxPage }"
        >
          ▶
        </button>
      </div>
    </div>
    <checkpwd
      :check="checkpassword"
      :roomId="selectedRoomId"
      @close="checkpassword = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, watchEffect } from "vue";
import { useRouter } from "vue-router";
import { useOpenViduStore } from "@/stores/openvidu";
import { useRoomStore } from "@/stores/room";
import IndividualRoom from "./IndividualRoom.vue";
import checkpwd from "./checkpwd.vue";

const router = useRouter();
const store = useOpenViduStore();
const roomStore = useRoomStore();
const checkpassword = ref(false);
const selectedRoomId = ref(null);
// const joinRoomDirectly = (roomId) => {
//   // roomId가 존재할 때만 세션에 참여
//   const roomIdString = roomId ? roomId.toString() : null;

//   if (roomIdString) {
//     const payload = {
//       roomId: roomIdString,
//     };
//     store.joinSession(payload);
//     // console.log(roomIdString)
//     router.replace({ name: "TalkBody", params: { id: roomIdString } });
//   }
// }; // 프론트만 돌릴때 (더미데이터 존재시).

const joinRoomDirectly = (roomId, secret) => {
  // roomId가 존재할 때만 세션에 참여
  const roomIdNumber = typeof roomId === "number" ? roomId : null;

  if (roomIdNumber !== null) {
    selectedRoomId.value = roomIdNumber;
    // console.log(selectedRoomId.value);
    if (secret === true) {
      checkpassword.value = !checkpassword.value;
      // console.log(checkpassword.value);
    } else {
      roomStore.entranceRoom(roomIdNumber);
      // console.log(roomIdNumber);
    }

    // router.push({ name: "TalkBody", params: { id: roomIdString } });
  }
}; //백엔드 연결 코드 (성공 확인)

const rooms = roomStore.Roomlist; // 백엔드 연결
// const rooms = ref([
//   {
//     room_id: 8,
//     room_name: "abc",
//     room_password: null,
//     user_count: 1,
//     game_type: 0,
//     owner: "ssafy",
//     _active: false,
//     _explosion: false,
//     _private: false,
//   },
//   {
//     room_id: 9,
//     room_name: "abc",
//     room_password: null,
//     user_count: 1,
//     game_type: 1,
//     owner: "ssafy",
//     _active: false,
//     _explosion: false,
//     _private: false,
//   },
//   {
//     room_id: 10,
//     room_name: "abc",
//     room_password: 1234,
//     user_count: 1,
//     game_type: 1,
//     owner: "ssafy",
//     _active: false,
//     _explosion: false,
//     _private: true,
//   },
// ]); // 테스트 더미 데이터

const perPage = 6; // 한 페이지당 방 갯수
const pageNumber = ref(0);

const paginatedRooms = computed(() => {
  const start = pageNumber.value * perPage;
  const end = start + perPage;
  // let pageRooms = rooms.value.slice(start, end); // 더미데이터 진행시
  let pageRooms = rooms.slice(start, end); // 백엔드 연결시
  while (pageRooms.length < perPage) {
    pageRooms.push({ id: null, index: pageRooms.length + 1 });
  }

  return pageRooms;
});

const maxPage = computed(() => {
  return Math.ceil(rooms.length / perPage) - 1;
});

const nextPage = () => {
  if (pageNumber.value < maxPage.value) pageNumber.value++;
};

const prevPage = () => {
  if (pageNumber.value > 0) pageNumber.value--;
};

watchEffect(() => {
  // console.log("Paginated Rooms Updated:", paginatedRooms);
});
</script>
<style>
.game_room {
  width: 95%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 3%;
  background-color: #054ca3;
  border-radius: 10px;
}

.welcome_items {
  display: flex;
  flex-wrap: wrap; /* 요소들이 넘칠 경우 다음 줄로 넘어가도록 설정 */
}

.buttoncontainer {
  display: flex;
  justify-content: center;
  width: 95%;
}
.page {
  display: flex;
  justify-content: center;
  background-color: #054ca3;
  width: 30%;
  height: 30px;
  border-bottom-right-radius: 10px;
  border-bottom-left-radius: 10px;
}

.page > button {
  height: 75%;
  width: 25%;
  background-color: #00acfc;
  border-color: #0065fc;
  border-radius: 3px;
  margin: 0% 3%;
  transition: background-color 0.3s ease;
  color: #4ddffb;
  border: none;
}

.page > button:hover {
  background-color: #0197dd;
  cursor: click;
}

.page > button.disabled-button {
  background-color: #19adfc; /* 회색 배경색으로 변경 */
  border: none; /* 회색 테두리로 변경 */
  cursor: not-allowed; /* 커서를 not-allowed로 변경하여 클릭 비활성화 */
  color: #888; /* 글자색을 회색으로 변경 */
}

.page > button.disabled-button:hover {
  background-color: #19adfc; /* hover 시 배경색도 그대로 유지 */
  cursor: not-allowed; /* hover 시 커서도 그대로 유지 */
}
</style>
