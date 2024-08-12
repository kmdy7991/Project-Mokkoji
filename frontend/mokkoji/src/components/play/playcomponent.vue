<template>
  <div class="container">
    <div class="player-wrapper">
      <div class="player">
        <user-video
          class="people"
          :stream-manager="vidustore.publisher"
          @click.native="updateMainVideoStreamManager(publisher)"
        />
        <user-video
          class="people"
          v-for="sub in vidustore.subscribers"
          :key="sub.stream.connection.connectionId"
          :stream-manager="sub"
          @click.native="updateMainVideoStreamManager(sub)"
        />
      </div>
    </div>
    <div class="mainvidio">
      <mainvidio/>
    </div>
    <div class="chat">
      <chatcomponent :room-id="roomId" />
    </div>
    <div class="session"></div>
  </div>
</template>

<script setup>
import mainvidio from "../play/mainvidio.vue";
import { ref } from "vue";
import { useRoute } from "vue-router";
import { useOpenViduStore } from "@/stores/openvidu";
import UserVideo from "./UserVideo.vue";
import chatcomponent from "./chatcomponent.vue";

const vidustore = useOpenViduStore();
const mainStreamManager = ref(null);
const publisher = vidustore.publisher;
const route = useRoute();
const roomId = route.params.id; // 현재 라우트에서 roomId 파라미터를 가져옵니다.

</script>

<style scoped>
.container {
  height: 30%;
  display: flex;
  justify-content: start;
  align-items: flex-start;
  max-width: 100%;
}

.player-wrapper {
  width: 10%; /* 조절 가능한 너비 */
  height: 620px;
  display: flex;
  flex-direction: column;

  align-items: center;
  margin-left: 3%;
  margin: 1%;
}
.player {
  width: 100%;
}

.people {
  width: 100%;
  height: 120px;
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
  height: 50%;
  margin-top: 1%;
  margin-right: 1%;
}
</style>
