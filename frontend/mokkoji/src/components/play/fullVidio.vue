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
</script>
<style scoped>
#video-show {
  background-color: #08beff;
  margin-top: 8%;
  width: 100%;
  height: 82%;
}
</style>
