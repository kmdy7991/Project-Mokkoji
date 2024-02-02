<template>
  <div class="game">
    <img
      v-if="gameType === 0"
      src="@/assets/bodyword.png"
      alt="bodyword.png"
      class="centered-image"
    />
    <img
      v-else-if="gameType === 1"
      src="@/assets/musicQ.png"
      alt="musicQ.png"
      class="centered-image"
    />
    <div v-else class="centered-image">
      <div class="blank"></div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  gameType: Number,
});
</script>

<style scoped>
.game {
  display: flex; /* Flexbox를 사용합니다 */
  justify-content: center; /* 수평 방향 중앙 정렬 */
  align-items: center; /* 수직 방향 중앙 정렬 */
  background-color: #13acfe;
  padding: 3%;
  width: 25%;
  height: 20%;
  border-radius: 5%;
}

.centered-image {
  width: 100px; /* 이미지가 div를 벗어나지 않도록 함 */
  height: 100px; /* 이미지가 div를 벗어나지 않도록 함 */
  object-fit: contain;
  border-radius: 5%;
  box-shadow: 1% 2% rgba(0, 0, 0, 0.2);
}

.blank {
  width: 100px; /* 이미지가 div를 벗어나지 않도록 함 */
  height: 100px; /* 이미지가 div를 벗어나지 않도록 함 */
  object-fit: contain;
  border-radius: 5%;
  box-shadow: 1% 2% rgba(0, 0, 0, 0.2);
}
</style>
