<template>
  <div class="game">
    <div class="buttons">
      <button class="button" @click="goWaitRoom()">
        <img src="@/assets/backdo.png" alt="backdo.png" />
      </button>
    </div>
    <div class="player">
      <playcomponent :roomId="roomId" />
    </div>
  </div>
</template>

<script setup>
import playcomponent from "@/components/play/playcomponent.vue";
import { gameStore } from "@/stores/game";
import { useWebSocketStore } from "@/stores/socket";
import { defineProps } from "vue";
import { useRouter } from "vue-router";

const gamestore = gameStore();
const start = gamestore.start;
const router = useRouter();
const store = useWebSocketStore();

const props = defineProps({
  roomId: String,
});



const goWaitRoom = () => {
  console.log(start)
  store.disconnectWebSocket()
  console.log('byebye~!')
  router.push({name: 'waitRoom'})
}
</script>

<style scoped>
.game {
  max-width: 100%; /* 화면의 전체 너비를 최대 너비로 설정 */
  overflow: hidden; /* 화면 밖으로 내용이 넘어가지 않도록 설정 */
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

.button img {
  width: 100%; /* 이미지 너비를 버튼 너비에 맞춤 */
  height: 100%; /* 이미지 높이를 버튼 높이에 맞춤 */
  object-fit: contain; /* 이미지 비율을 유지하면서 버튼 내에 맞춤 */
}

.button:hover {
  background-color: #dd2b14; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

.player {
  margin-left: 3%;
}
</style>
