<template>
  <div class="container">
    <div>
      <button class="roomCreate" @click="toggleModal">방만들기</button>
    </div>
    <div>
      <Roomarea></Roomarea>
    </div>
    <RoomCreateModal :show="showModal" @close="showModal = false">
    </RoomCreateModal>
  </div>
</template>

<script setup>
import Roomarea from "./Roomarea.vue";
import RoomCreateModal from "./RoomCreateModal.vue";
import { ref } from "vue";

const { rooms } = defineProps(['rooms']);
const showModal = ref(false);

const toggleModal = () => {
  showModal.value = !showModal.value;
};
</script>

<style scoped>
.container {
  display: flex;
  flex-wrap: wrap; /* This allows items to wrap onto the next line */
  margin: 3%;
}

.roomCreate {
  background-color: #f9e01e;
  border: none;
  border-radius: 10px;
  width: 150px;
  height: 75px;
  font-size: 28px;
  margin: 3%;
  font-family: "DOSMyungjo";
  transition: background-color 0.3s ease;
  color: white;
}

.roomCreate:hover {
  background-color: #dd2b14; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}
</style>
