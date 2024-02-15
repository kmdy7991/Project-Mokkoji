<template>
  <div class="container">
    <div class="live-player"><livePlayer /></div>
    <div class="game-room"><GameArea /></div>
  </div>
</template>

<script setup>
import GameArea from "@/components/waitingroom/GameArea.vue";
import livePlayer from "../livePlayer/livePlayer.vue";
</script>

<style scoped>
.container {
  display: flex;
  width: 100%;
  height: 100%;
}
.live-player {
  margin-left: 1%;
  margin-top: 2%;
  width: 25%;
  height: 100%;
  background-color: #006dc4;
  border-radius: 10px;
}
.game-room {
  margin-left: 1%;
  margin-top: 2%;
  width: 75%;
  border-radius: 10px;
  height: 100%;
  padding-bottom: 2%;
  background-color: #00c4f7;
}
</style>
