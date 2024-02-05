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
        <div class="nickname">{{ rank.nickname || "" }}</div>
        <div class="rankingpoint">{{ rank.point || "" }}</div>
      </div>
      <div class="buttons">
        <button class="button">한번더!</button>
        <button class="button" @click="close">나가기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from "vue";
import { useGameStore } from "@/stores/game";

const usegamestore = useGameStore();

const ranks = ref([
  { nickname: "누구", point: 50 },
  { nickname: "나두", point: 20 },
  { nickname: "궁금", point: -50 },
]);

const adjustedRanks = computed(() => {
  const filledArray = ranks.value.slice(); // ranks 배열 복사
  for (let i = filledArray.length; i < 6; i++) {
    filledArray.push({}); // 빈 객체를 추가하여 길이를 6으로 맞춤
  }
  return filledArray;
});

function close() {
  usegamestore.gameresult = false;
  usegamestore.start = false;
  usegamestore.showAd = false;
}
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
  height: 80%;
  margin: 0% 10%;
}
</style>
