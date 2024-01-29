<template>
  <div class="container">
    <div class="live-player">임시임시임시!</div>
    <div class="game-room"><GameArea /></div>
  </div>
</template>

<script setup>
import GameArea from "@/components/waitingroom/GameArea.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: row;
}
.live-player {
  margin-left: 1%;
  margin-top: 2%;
  width: 25%;
}
.game-room {
  margin-left: 1%;
  margin-top: 2%;
  margin-bottom: 2%;
  width: 75%;
  border-radius: 10px;
  background-color: #00c4f7;
}
</style>
