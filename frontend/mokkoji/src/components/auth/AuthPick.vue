<template>
  <div class="container">
    <div v-if="Authmain">
      <div class="buttons">
        <button class="button" @click="">ㅋㅋㅋㅋ</button>
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
import { ref } from "vue";
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
</script>

<style scoped>
.container {
  /* 버튼들을 세로 방향으로 중앙에 배치 */
  height: 100%; /* 컨테이너의 높이를 화면 높이와 동일하게 설정 */
  width: 100%;
}
.buttons {
  display: flex;
  justify-content: flex-end;
}

.category {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
.button1,
.button2 {
  display: flex;
  justify-content: center; /* 텍스트를 가로 방향으로 중앙에 배치 */
  align-items: center; /* 텍스트를 세로 방향으로 중앙에 배치 */
  width: 200px; /* 버튼의 너비 */
  height: 200px; /* 버튼의 높이 (정사각형 모양) */
  margin: 50px; /* 위아래 마진 설정 */
  border-radius: 20px;
}

.button1 {
  background-color: orange;
}

.button2 {
  background-color: blue;
}

.button1 p {
  margin: 2px; /* 각 <p> 태그 아래에 간격 추가 */
}
</style>
