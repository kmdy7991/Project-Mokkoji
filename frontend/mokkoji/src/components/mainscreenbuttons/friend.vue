<template>
	<div class="ranking-modal">
	  <div class="modal-content">
		  <div class="buttons">
		  <button class="button" @click="closeModal">
			<img src="@/assets/backdo.png" alt="backdo.png" />
		  </button>
		</div>
		<h1>친구 목록(개발중)</h1>
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
  
  .buttons {
	display: flex; /* 버튼을 위한 Flexbox 컨테이너 설정 */
	justify-content: flex-end; /* 버튼을 오른쪽 끝으로 정렬 */
  }
  
  .button {
	background-color: #ff2001;
	border: #036be7;
	width: 50px; /* 버튼의 너비 */
	height: 50px; /* 버튼의 높이를 너비와 같게 설정하여 정사각형 만들기 */
	line-height: 50px; /* 텍스트를 버튼 중앙에 위치시키기 */
	text-align: center; /* 텍스트 중앙 정렬 */
	margin-top: 1%;
	margin-right: 1%;
	border-radius: 10%; /* 버튼 모서리를 약간 둥글게 */
	transition: background-color 0.3s ease; /* 부드러운 색상 전환 효과 */
	/* 기본 버튼 스타일링 */
	color: white; /* 기본 글자색 */
	box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  }
  
  .button:hover {
	background-color: #dd2b14; /* 예: 파란색 배경 */
	color: #fff; /* 예: 흰색 글자 */
	cursor: click;
  }
  .button img {
	width: 100%; /* 이미지 너비를 버튼 너비에 맞춤 */
	height: 100%; /* 이미지 높이를 버튼 높이에 맞춤 */
	object-fit: contain; /* 이미지 비율을 유지하면서 버튼 내에 맞춤 */
  }
  
	/* 추가 스타일링 */
  </style>