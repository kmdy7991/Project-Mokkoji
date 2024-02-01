<template>
  <div class="container">
    <div v-if="Authmain">
      <div class="buttons">
        <button class="button" @click="goBack()">
          <img src="@/assets/backdo.png" alt="backdo.png" />
        </button>
      </div>
      <div class="category">
        <button class="button1" @click="wordplus()">
          <p>제시어</p>
          <p>카테고리</p>
          <p>추가</p>
        </button>
        <button class="button2" @click="playermanagement">유저 명단</button>
      </div>
    </div>
    <Add v-if="word" @close="wordClose" />
    <Players v-if="player" @close="playerClose" />
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits} from "vue";
import Add from "./Add.vue";
import Players from "./Players.vue";

const Authmain = ref(true);
const word = ref(false);
const player = ref(false);

const wordplus = () => {
  Authmain.value = !Authmain.value;
  word.value = !word.value;
};

const playermanagement = () => {
  Authmain.value = !Authmain.value;
  player.value = !player.value;
};

const wordClose = () => {
  Authmain.value = !Authmain.value;
  word.value = !Authmain.value;
};

const playerClose = () => {
  Authmain.value = !Authmain.value;
  player.value = !player.value;
};

const emit = defineEmits(["close"]);

const goBack = () => {
  emit("close");
};
</script>

<style scoped>
.container {
  /* 버튼들을 세로 방향으로 중앙에 배치 */
  height: 100%; /* 컨테이너의 높이를 화면 높이와 동일하게 설정 */
  width: 100%;
  background-color: #00C3F4;
}
.buttons {
  display: flex;
  justify-content: flex-end;
}

.category {
  display: flex;
  flex-direction: row;
  justify-content: center;
  padding-top: 8%;
  padding-bottom: 8%;
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
.button1,.button2 {
  display: flex;
  flex-direction: column;
  justify-content: center; /* 텍스트를 가로 방향으로 중앙에 배치 */
  align-items: center; /* 텍스트를 세로 방향으로 중앙에 배치 */
  width: 250px; /* 버튼의 너비 */
  height: 250px; /* 버튼의 높이 (정사각형 모양) */
  margin: 100px; /* 위아래 마진 설정 */
  border-radius: 20px;
  font-size: 40px;
  font-family: "LABdigital";
  transition: background-color 0.3s ease;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  border: none;
}

.button2{
  color: white;
}

.button1 {
  background-color: #F68D12;
}

.button1:hover {
  background-color: #f68c12cb;
}
.button2 {
  background-color: #3F46EA;
}

.button2:hover {
  background-color: #3f45eacb;
}
.button1 p {
  margin: 2px; /* 각 <p> 태그 아래에 간격 추가 */
}
</style>