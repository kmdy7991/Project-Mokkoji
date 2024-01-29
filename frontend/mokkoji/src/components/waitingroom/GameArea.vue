<template>
  <div class="container">
    <div>
      <button class="roomCreate" @click="toggleModal">방만들기</button>
    </div>
    <div class="room-select">
      <Roomarea />
    </div>
    <RoomCreateModal :show="showModal" @close="showModal = false" />
  </div>
</template>

<script setup>
import Roomarea from "./Roomarea.vue";
import RoomCreateModal from "./RoomCreateModal.vue";
import { ref } from "vue";

const showModal = ref(false);

const toggleModal = () => {
  showModal.value = !showModal.value;
};
</script>

<style scoped>
.container {
  flex-direction: column;
  height: 100%; /* This allows items to wrap onto the next line */
  margin: 2%;
}

.roomCreate {
  background-color: #f9e01e;
  border: none;
  border-radius: 10px;
  width: 150px;
  height: 75px;
  font-size: 28px;
  font-family: "DOSMyungjo";
  transition: background-color 0.3s ease;
  color: white;
}

.roomCreate:hover {
  background-color: #dd2b14; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

.room-select, .roomCreateModal {
  width: 100%; /* 전체 너비를 사용 */
  display: block; /* 블록 레벨 요소로 설정 */
}
</style>
