<template>
  <div>
    <div v-if="streamManager && gameStore.showAd" id="video-show">
      <fullMainVidoo :stream-manager="streamManager" />
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps } from "vue";
import fullMainVidoo from "./fullMainVidio.vue";
import { useGameStore } from "@/stores/game";
const gameStore = useGameStore();
// props 정의
const props = defineProps({
  streamManager: Object,
});

const clientData = computed(() => {
  const connection = props.streamManager.stream.connection;
  return JSON.parse(connection.data).clientData;
});
</script>
<style scoped>
#video-show {
  width: 100%;
  height: 100%;
}
</style>
