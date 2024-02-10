<template>
  <div class="result">
    <div class="roading">
      <div class="rankinginfo">방장이 나가 자동으로 대기실로 이동됩니다.</div>
      <div class="buttons">
        <button class="button" @click="gameout()">
          나가기 {{ countdown }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from "vue";
import { useGameStore } from "@/stores/game";
import { useWebSocketStore } from "@/stores/socket";
import { useOpenViduStore } from "@/stores/openvidu";
import { userStore } from "@/stores/user";
import { useRoomStore } from "@/stores/room";
import { useRouter, useRoute } from "vue-router";

const usegamestore = useGameStore();
const session = ref(false);
const route = useRoute();
const roomId = route.params.id;
const router = useRouter();
const store = useWebSocketStore();
const vidustore = useOpenViduStore();
const userstore = userStore();
const roomstore = useRoomStore();
const countdown = ref(5); // 카운트다운 시작 숫자
let countdownInterval;

onMounted(() => {
  countdownInterval = setInterval(() => {
    countdown.value--;
    if (countdown.value === 0) {
      gameout(); // 카운트다운이 0에 도달하면 gameout 함수 실행
    }
  }, 1000);
});

onUnmounted(() => {
  clearInterval(countdownInterval); // 컴포넌트 언마운트 시 인터벌 정리
});

const adjustedRanks = computed(() => {
  const filledArray = ranks.value.slice(); // ranks 배열 복사
  for (let i = filledArray.length; i < 6; i++) {
    filledArray.push({}); // 빈 객체를 추가하여 길이를 6으로 맞춤
  }
  return filledArray;
});

function close() {
  usegamestore.gameout();
  clearInterval(countdownInterval);
}

const gameout = () => {
  clearInterval(countdownInterval);
  store.roomexplosion = true;
  console.log(store.roomexplosion);
  const payload = {
    roomId: Number(roomId),
    nickname: userstore.myName,
  };
  usegamestore.gameout();
  store.disconnectWebSocket();
  vidustore.leaveSession();
  session.value = vidustore.session;
  roomstore.getRoomlist();
  roomstore.exitRoom(payload);

  if (userstore.Auth === true) {
    router.replace({ name: "Auth" });
  } else {
    router.replace({ name: "waitRoom" });
  }
}; // 비동기 처리가 필요한 함수라고 가정
// 페이지 전환은 모든 비동기 작업이 완료된 후 실행
</script>

<style scoped>
.result {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: none;
  position: fixed; /* 고정 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비 */
  height: 100%;
  z-index: 1000;
}

.roading {
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 80%;
  height: 30%;
  background-color: rgba(255, 255, 255, 0.5);
  font-size: 48px;
  font-family: "DOSMyungjo";
}

.rankinginfo {
  width: 80%;
  margin-top: 5%;
  margin-bottom: 2%;
  display: flex;
  justify-content: center;
}

.buttons {
  display: flex;
  width: 100%;
  height: 50%;
  justify-content: center;
  align-items: center;
}

.button {
  width: 15%;
  height: 50%;
  margin: 0% 10%;
  border-radius: 10px;
  border: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  background-color: rgba(20, 224, 255, 0.39);
  transition: background-color 0.5s ease;
  font-size: 32px;
  font-family: "DOSMyungjo";
}

.button:hover {
  background-color: rgba(253, 200, 9, 0.39);
}
</style>
