<template>
  <div class="container">
    <h1 v-if="!gameStore.showAd" class="gamename">몸으로 말해요</h1>
    <h1 v-if="gameStore.showAd && nowplayer" class="gamename">
      제시어: {{ gameStore.answers }}
    </h1>
    <h1 v-if="gameStore.showAd && !nowplayer" class="gamename">
      카테고리: {{ gameStore.category }}
    </h1>
    <div class="mainvidio">
      <fullVidio
        v-show="now"
        :key="nowkey"
        class="people"
        :stream-manager="now"
        @click.native="updateMainVideoStreamManager(now)"
      />
    </div>
    <div class="gamestart">
      <div>
        <form v-if="roomowner && !gameStore.start" @submit.prevent="gamestart">
          <button class="button">게임시작</button>
        </form>
        <div v-else class="showAD">
          <div v-if="gameStore.countdown > 0 && gameStore.nowcountdown">
            {{ gameStore.countdown }}
          </div>
          <div v-else-if="!gameStore.showAd && gameStore.nowcountdown">
            게임이 시작됩니다!
          </div>
          <div
            v-if="
              (gameStore.showAd || roomStore.owner !== userStore.myName) &&
              !gameStore.nowcountdown
            "
          >
            여기에 광고판 컨텐츠
          </div>
        </div>
      </div>
    </div>
    <div v-if="gameStore.gameend">
      <resultloading />
    </div>
    <div v-if="gameStore.gameresult">
      <result />
    </div>
    <div v-if="!gameStore.start && !usesocketstore.roomexplosion">
      <roomexplosion />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useChatStore } from "@/stores/chat";
import { useGameStore } from "@/stores/game";
import { userStore } from "@/stores/user";
import { useRoomStore } from "@/stores/room";
import { useWebSocketStore } from "@/stores/socket";
import fullVidio from "./fullVidio.vue";
import resultloading from "./resultloading.vue";
import result from "./result.vue";
import roomexplosion from "./roomexplosion.vue";

const route = useRoute();
const roomId = route.params.id;
const store = useChatStore();
const gameStore = useGameStore();
const userstore = userStore();
const usesocketstore = useWebSocketStore();
const roomStore = useRoomStore();
const playername = userstore.myName;
const myindex = ref(0);
const now = ref();
const nowkey = ref();
const nowuserjson = ref();
const nowuser = ref();
const roomowner = roomStore.owner === userstore.myName;
const nowplayer = computed(() => nowuser.value?.clientData === playername);

// console.log(roomStore.owner);
// console.log(userstore.myName);
// console.log(gameStore.start);
// console.log(roomowner);

watch(
  () => gameStore.nowParticipant,
  (newVal, oldVal) => {
    // console.log("New Participant:", newVal, "myindex.value:", myindex.value);
    now.value = newVal;
    // console.log(now.value.stream.connection.connectionId);
    nowkey.value = now.value.stream?.connection.connectionId;
    // console.log(nowkey.value);
    nowuserjson.value = now.value.stream?.connection.data;
    // console.log(nowuserjson.value);
    if (nowuserjson.value !== undefined) {
      nowuser.value = JSON.parse(nowuserjson.value);
      // console.log(nowuser.value);
    }
  },
  { deep: true }
);

const gamestart = () => {
  usesocketstore.gameStart();
};

onMounted(() => {
  gameStartOnLoad();
});

const gameStartOnLoad = () => {
  gameStore.start = false; // gameStore의 start 상태를 true로 설정
  gameStore.gameend = false;
  gameStore.gameresult = false;
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
  height: 650px;
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
  margin-top: 0%;
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
  color: white;
  margin-top: 3%;
  font-family: "LABdigital";
}

.showAD {
  font-size: 48px;
}
</style>
