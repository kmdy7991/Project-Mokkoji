<template>
  <div class="container">
    <div class="buttons">
      <button class="button" @click="goBack()">
        <img src="@/assets/backdo.png" alt="backdo.png" />
      </button>
    </div>
    <div class="select">
      <div class="word">
        <div class="category">
          <div class="categoryvalue">
            <div class="center">
              카테고리
            </div>
          </div>
          <div class="categoryselect">
            <select class="toggledown">
              <option value="option1">Option 1</option>
              <option value="option2">Option 2</option>
              <option value="option3">Option 3</option>
              <!-- 여기에 더 많은 옵션을 추가할 수 있습니다. -->
            </select>
          </div>
        </div>
        <div class="category">
          <div class="categoryvalue">
            <div class="center">
              제시어
            </div>
          </div>
          <div class="categoryselect">
            <input class="wordinput" type="text">
          </div>
        </div>
      </div>
    </div>
    <div class="addbutton">
      <button>
        추가
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { ref } from "vue";

const emit = defineEmits(["close"]);

const goBack = () => {
  emit("close");
};
</script>

<style scoped>
.buttons {
  display: flex;
  justify-content: flex-end;
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

.select {
  display: flex;
  flex-direction: column;
  align-content:center;
  padding-top: 8%;
  width: 100%;
}

.word{
  display: flex;
  flex-direction: column;
  align-items: center;
}

.category{
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 60%;
  height: 100px;
  background-color: #0594E0;
  border-radius: 15px;
  padding: 1%;
  margin-bottom: 3%;
}

.categoryvalue{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
  width: 15%;
  height: 100%;
  background-color: #00ACFC;
}

.center{
  width: 100%;
  display: flex;
  justify-content: center;
  object-fit: contain;
  color: white;
  font-size: 36px;
  font-family: "DOSMyungjo";
}

.categoryselect{
  display: flex;
  margin-left: 5%;
  width: 80%;
  height: 100%;
  background-color: #00ACFC;
  position: relative;
}

.toggledown{
  margin: 1%;
  width: 80%;
  height: 85px;
  border-radius: 10px;
}

.togglebutton{
  margin: 1%;
  width: 10%;
}

.wordinput{
  margin: 1%;
  width: 80%;
  height: 80px;
  border-radius: 10px;
  border: none;
  font-size: 36px;
}

.addbutton{
  display: flex;
  justify-content: center;
  padding-bottom: 10%;
}

.addbutton > button{
  width: 20%;
  height: 100px;
  border-radius: 10px;
  font-size: 52px;
  font-family: "LABdigital";
  border: none;
  background-color: #FDC909;
  transition: background-color 0.3s ease;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.addbutton > button:hover{
  background-color: #fdc809bd;
}
</style>