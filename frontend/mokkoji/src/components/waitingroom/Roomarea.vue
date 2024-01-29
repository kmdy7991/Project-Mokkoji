<template>
  <div class="game_room">
    <IndividualRoom
      v-for="room in paginatedRooms"
      :key="room.id || `empty-${room.index}`"
      class="welcome_itme"
    />
    <button @click="prevPage" :disabled="pageNumber === 0">이전</button>
    <button @click="nextPage" :disabled="pageNumber >= maxPage">다음</button>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import IndividualRoom from "./IndividualRoom.vue";

const rooms = ref([1, 2, 3, 4, 5, 6, 7, 8]);

const perPage = 6; // 한 페이지당 방 갯수
const pageNumber = ref(0);

const paginatedRooms = computed(() => {
  const start = pageNumber.value * perPage;
  const end = start + perPage;
  let pageRooms = rooms.value.slice(start, end);

  while (pageRooms.length < perPage) {
    pageRooms.push({ id: null, index: pageRooms.length + 1 });
  }

  return pageRooms;
});

const maxPage = computed(() => {
  return Math.ceil(rooms.value.length / perPage) - 1;
});

const nextPage = () => {
  if (pageNumber.value < maxPage.value) pageNumber.value++;
};

const prevPage = () => {
  if (pageNumber.value > 0) pageNumber.value--;
};
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
