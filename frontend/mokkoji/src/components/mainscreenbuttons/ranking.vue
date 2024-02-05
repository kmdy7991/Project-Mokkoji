<template>
  <div class="ranking-modal">
    <div class="modal-content">
      <h1>모꼬지 랭킹포인트</h1>
      <div class="checkbuttons">
        <button class="checkbutton" @click="closeModal">
          확인
        </button>
      </div>
      <div class="ranking-list">
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';


const emit = defineEmits(['close']);

function closeModal() {
  emit('close');
}
</script>

<style scoped>
.ranking-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.2);
}

.modal-content {
  background-color: #10C2DF;
  text-align: center;
  width: 80%;
  height: 80%;
  padding: 20px;
  font-family: "DOSMyungjo";
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.checkbuttons{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 3%;
  width: 90%;
}


.checkbutton {
  width: 160px;
  height: 50px;
  background-color: #00ACFC;
  margin-left: 10%;
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
/* 추가 스타일링 */
</style>