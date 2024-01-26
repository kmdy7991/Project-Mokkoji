<template>
  <div class="session">
    <div id="session-header">
      <h1 v-show="false" id="session-title">{{ store.mySessionId }}</h1>
      <input
        class="btn btn-large btn-danger"
        type="button"
        id="buttonLeaveSession"
        @click="leaveSession()"
        value="Leave session"
      />
    </div>
    <div class="session">
      <div id="main-video" class="col-3">
        <user-video :stream-manager="mainStreamManager" />
      </div>
      <div id="video-container" class="col-3">
        <user-video
          :stream-manager="store.publisher"
          @click.native="updateMainVideoStreamManager(publisher)"
        />
        <user-video
          v-for="sub in store.subscribers"
          :key="sub.stream.connection.connectionId"
          :stream-manager="sub"
          @click.native="updateMainVideoStreamManager(sub)"
        />
      </div>
      <div class="col-3" id="chat-component">
        <chatcomponent room-id="001" :user-name="mySessionId" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import { ref, onMounted } from "vue";
import { useOpenViduStore } from "@/stores/openvidu"; // 필요에 따라 경로 조정
import UserVideo from "../components/UserVideo.vue";
import chatcomponent from "../components/chatcomponent.vue";
// import joinComponent from './components/joinComponent.vue';

const store = useOpenViduStore();
const mySessionId = ref("");
const myUserName = ref("");
const mainStreamManager = ref(null);
const session = ref(false);
let subscribers = null;
const publisher = store.publisher;

// const updateMainVideoStreamManager = (streamManager) => {
//   mainStreamManager.value = streamManager;
// };

const leaveSession = () => {
  store.leaveSession();
  session.value = store.session;
  router.push({ name: "login" });
};
</script>

<style scoped>
.session {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: start;
}
#video-container {
  margin-left: 0px;
  margin-right: 800px;
  width: 70vh;
}
#chat-component {
  width: 300px;
  height: 150%;
}
</style>
