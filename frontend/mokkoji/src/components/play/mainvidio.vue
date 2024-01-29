<template>
  <div class="container">
    <h1 v-if="!gameStore.showAd" class="gamename">몸으로 말해요</h1>
    <h1 v-if="gameStore.showAd" class="gamename">제시어: 박땡땡</h1>
    <div class="mainvidio">
      <fullVidio
        v-show="gameStore.randomParticipant"
        class="people"
        :stream-manager="gameStore.randomParticipant"
        :number="gameStore.randomIndex"
        @click.native="updateMainVideoStreamManager(gameStore.randomParticipant)"
      />
    </div>
    <div class="gamestart">
      <div>
        <form v-if="!gameStore.start" @submit.prevent="gamestart">
          <button class="button">게임시작</button>
        </form>
        <div v-else>
          <div v-if="gameStore.countdown > 0">{{ gameStore.countdown }}</div>
          <div v-else-if="!gameStore.showAd">게임이 시작됩니다!</div>
          <div v-if="gameStore.showAd">여기에 광고판 컨텐츠</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useChatStore } from "@/stores/chat";
import { useGameStore } from "@/stores/game";
import fullVidio from "./fullVidio.vue";

const store = useChatStore();
const gameStore = useGameStore();
const start = ref(false);

const gamestart = function () {
  gameStore.gameStart();
};

const plusChat = function () {
  if (start === false) {
    return;
  }
  store.startChat();
};
</script>

<style scoped>
.container {
  display: flex; /* Flex 컨테이너 설정 */
  flex-direction: column; /* 자식 요소들을 세로로 배치 */
  align-items: center; /* 가로 방향에서 중앙 정렬 */
  width: 100%; /* 컨테이너의 너비를 100%로 설정 */
  height: 100vh; /* 뷰포트 높이에 맞게 컨테이너의 높이 설정 */
  justify-content: center; /* 세로 방향에서 중앙 정렬 */
}
.mainvidio {
  width: 100%;
  height: 450px;
  background-color: #12deff;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  border-right: 2%;
}

.gamename {
  margin-top: 1%;
  margin-bottom: 1%;
  width: 100%;
  text-align: center;
  color: white;
  font-family: "DosMyungjo";
  font-size: 48px;
}

.button {
  width: 200px;
  height: 70px;
  border-radius: 10px;
  border: none;
  font-family: "LABdigital";
  color: black;
  font-size: 48px;
  background-color: #fee200;
  transition: background-color 0.3s ease; /* 부드러운 색상 전환 효과 */
  /* 기본 버튼 스타일링 */
  box-shadow: 2px 4px 10px rgba(0, 0, 0, 0.3);
}
.button:hover {
  background-color: #dd2b14; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

.gamestart {
  width: 100%; /* 버튼을 포함하는 div의 너비를 100%로 설정 */
  display: flex; /* Flexbox 레이아웃 사용 */
  justify-content: center; /* 가로 방향으로 중앙 정렬 */
  align-items: center; /* 세로 방향으로 중앙 정렬 */
  margin-top: 5%; /* 위쪽 여백 설정 */
  color: white;
  font-family: "LABdigital";
}
</style>
