<script setup>
import { RouterView } from "vue-router";
</script>

<template>
  <div class="background">
    <RouterView />
  </div>
</template>

<style scoped>
.background {
  background-color: #3bf0fb;
  background-size: cover; /* 배경 이미지가 div를 전체적으로 커버하도록 설정 */
  background-position: center;
  height: 100vh;
}
header {
  line-height: 1.5;
  max-height: 100vh;
}
</style>
