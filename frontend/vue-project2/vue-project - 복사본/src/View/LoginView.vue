<template>
  <div id="join-dialog" class="jumbotron vertical-center">
    <h1>Join a video session</h1>
    <div class="form-group">
      <p>
        <label>Participant</label>
        <input v-model="myUserName" class="form-control" type="text" required />
      </p>
      <p>
        <label>Session</label>
        <input
          v-model="mySessionId"
          class="form-control"
          type="text"
          required
        />
      </p>
      <p class="text-center">
        <button class="btn btn-lg btn-success" @click="joinSession()">
          Join!
        </button>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useOpenViduStore } from "@/stores/openvidu";
import { useRouter } from "vue-router";
const store = useOpenViduStore();
const mySessionId = ref("");
const myUserName = ref("");
const router = useRouter();

const joinSession = () => {
  const payload = {
    mySessionId: mySessionId.value,
    myUserName: myUserName.value,
  };
  store.joinSession(payload);
  router.push({ name: "game" });
  // session.value = store.session;
  // console.log("in Vue 95 = " + session.value);
  // console.log(session);
  // console.log("------vue check 1-------");

  // subscribers = store.subscribers;
  // console.log("in Vue 97 = " + subscribers);
  // console.dir(subscribers);
  console.log("------vue check2------");
};

onMounted(() => {
  mySessionId.value = store.mySessionId;
  myUserName.value = store.myUserName;
  console.log(store.subscribers);
});
</script>

<style scoped></style>
