<template>
  <div class="ranking-modal">
    <div class="modal-content">
      <h1>친구 목록(개발중)</h1>
      <button @click="closeModal">Close</button>
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
	align-items: end;
	justify-content: end;
}
  
.modal-content {
	background-color: #3FEAE0;
	text-align: center;
	width: 20%;
	height: 80%;
	padding: 20px;
	font-family: "DOSMyungjo";
	border-radius: 5px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}
  
  /* 추가 스타일링 */
</style>