<template>
  <div class="result">
    <div class="roading">
      <div class="gameresult">게임 결과</div>
      <div class="rankinginfo">
        <div class="rank">게임 순위</div>
        <div class="nickname">별명</div>
        <div class="rankingpoint">승점</div>
      </div>
      <div
        class="rankinginfo"
        v-for="(rank, index) in adjustedRanks"
        :key="index"
      >
        <div class="rank">{{ index + 1 }}</div>
        <div class="nickname">{{ rank.user_nickname || "" }}</div>
        <div class="rankingpoint">
          {{
            rank.score !== null && rank.score !== undefined ? rank.score : ""
          }}
        </div>
      </div>
      <div class="buttons">
        <button class="button" @click="close">한번더!</button>
        <button class="button" @click="gameout()">
          나가기 {{ countdown }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from "vue";
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
const countdown = ref(7); // 카운트다운 시작 숫자
const result = ref(usegamestore.ranks);

let countdownInterval;

watch(
  () => usegamestore.ranks,
  (newVal) => {
    result.value = newVal;
    adjustedRanks;
    // 이 시점에서 gameresult가 업데이트 되었으므로,
    // Vue의 반응성 시스템에 의해 adjustedRanks도 자동으로 업데이트됩니다.
    // 필요한 경우 여기서 추가 로직을 수행할 수 있습니다.
  } // 컴포넌트가 마운트될 때 즉시 실행되도록 설정
);

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
  const filledArray = result.value.slice(); // ranks 배열 복사
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

  const payload = {
    roomId: Number(roomId),
    nickname: userstore.myName,
  };
  usegamestore.gameout();
  usegamestore.countdown = 5;
  store.disconnectWebSocket();
  vidustore.leaveSession();
  session.value = vidustore.session;
  roomstore.exitRoom(payload);
  roomstore.getRoomlist();
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
  height: 80%;
  background-color: rgba(255, 255, 255, 0.5);
  font-size: 48px;
  font-family: "DOSMyungjo";
}

.gameresult {
  margin: 3% 5%;
  width: 90%;
  height: 10;
  display: flex;
  justify-content: center;
  align-items: center;
}

.rankinginfo {
  width: 80%;
  margin-bottom: 2%;
  display: flex;
}

.rank {
  width: 25%;
  display: flex;
  justify-content: center;
}

.nickname {
  width: 45%;
  display: flex;
  justify-content: center;
}

.rankingpoint {
  width: 30%;
  display: flex;
  justify-content: center;
}
.buttons {
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: center;
  align-items: center;
}

.button {
  width: 15%;
  height: 70%;
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
