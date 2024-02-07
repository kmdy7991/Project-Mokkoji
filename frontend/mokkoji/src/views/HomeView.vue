<script setup>
import Login from "../components/Login.vue";
</script>

<template>
  <main class="background">
    <Login />
  </main>
</template>

<style scoped>
.background {
  background-image: url("@/assets/moggozi.png");
  background-size: cover; /* 배경 이미지가 div를 전체적으로 커버하도록 설정 */
  background-position: center;
  height: 100%;
  width: 100%;
  border-radius: 10px;
}
</style>
