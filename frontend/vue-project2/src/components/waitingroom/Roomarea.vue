<template>
  <div class="game_room">
    <IndividualRoom
      v-for="room in roomData.slice(0, 6)"
      :key="room.room_id"
      :room="room"
    >
    </IndividualRoom>
  </div>
</template>

<script setup>
import IndividualRoom from "./IndividualRoom.vue";
import { useRoomStore } from "@/stores/room";
import {onMounted ,nextTick } from "vue";
const store = useRoomStore(); // 피니아에서 가져옴
onMounted(async () => {
  await store.getRoomlist(); // Assuming getRoomlist is an async function
  await nextTick(); // Wait for the next view update cycle
  roomData.value = store.roomData;
});
const roomData = store.roomData; // 피니아에서 룸 데이터 불러옴.
</script>
<style>
.game_room {
  width: 100%;
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
</style>
