<template>
  <div class="background">
    <Talkbodygame/>
  </div>
</template>

<script setup>
import Talkbodygame from "../components/bodygame/Talkbodygame.vue"; // 현재 라우트에서 roomId 파라미터를 가져옵니다.
</script>
<style scoped>
.background {
  background-color: #0286ff;
  max-width: 100%;
  height: 100%;
  background-size: cover; /* 배경 이미지가 div를 전체적으로 커버하도록 설정 */
  background-position: center;
  border-radius: 15px;
  margin: 0.5%;
}
</style>
