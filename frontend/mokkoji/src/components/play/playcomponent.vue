<template>
  <div class="container">
    <div class="player-wrapper">
      <div class="player">
        <uservidiocomponent />
        <uservidiocomponent />
        <uservidiocomponent />
        <uservidiocomponent />
        <uservidiocomponent />
        <uservidiocomponent />
      </div>
    </div>
    <div class="mainvidio">
      <mainvidio />
    </div>
    <div class="chat">
      <chatcomponent :roomId="roomId" />
    </div>
  </div>
</template>

<script setup>
import uservidiocomponent from "../play/uservidiocomponent.vue";
import mainvidio from "../play/mainvidio.vue";
import chatcomponent from "../play/chatcomponent.vue";
import { defineProps } from "vue";

const props = defineProps({
  roomId: String,
});
</script>

<style scoped>
.container {
  height: 60%;

  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  max-width: 100%;
}

.player-wrapper {
  width: 10%; /* 조절 가능한 너비 */
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 요소들 사이의 간격을 균등하게 분배 */
  align-items: center;
  margin-left: 3%;
  margin: 1%;
}
.player {
  width: 100%;
  margin-bottom: 10%;
}
.mainvidio {
  flex: 1;
  width: 60%;
  margin: 1%;
  border-radius: 10px;
}

.chat {
  width: 30%;
  height: 60%;
  margin-top: 1%;
  margin-right: 1%;
}
</style>
