<template>
  <div class="mypage-modal">
    <div class="modal-content">
      <div class="buttons">
        <button class="button" @click="closeModal">
          <img src="@/assets/backdo.png" alt="backdo.png" />
        </button>
      </div>
      <div class="mypage-info">
        <div class="mypage-list">
          <myinfo/>
        </div>
      </div>
      <button class="save">
        수정사항 저장
      </button>
    </div>
  </div>
</template>

<script setup>
import myinfo from "./myinfo.vue";
import { defineEmits } from 'vue';


const emit = defineEmits(['close']);

function closeModal() {
  emit('close');
}
</script>

<style scoped>
.mypage-modal {
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

.mypage-info{
  display:flex;
  justify-content: center;
  align-items:center;
  width : 100%
}

.mypage-list {
  margin: 1% 0%;
  height: 80%;
  width: 90%;
  display: flex;
  justify-content: start;
}

.save {
  border : 0;
  height : 40px;
  width: 50%;
  background-color: #00ACFC;
  border-radius: 10px;
  color: white;
  font-family: "DOSMyungjo";
  transition: background-color 0.3s ease;
	font-size: 20px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.save:hover{
  background-color: #3498db; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}
</style>