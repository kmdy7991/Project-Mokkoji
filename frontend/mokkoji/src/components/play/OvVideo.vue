<template>
  <video class="live" autoplay />
</template>

<script>
export default {
  name: "OvVideo",

  props: {
    streamManager: Object,
  },

  mounted() {
    this.streamManager.addVideoElement(this.$el);
  },
};
</script>

<style scoped>
.live {
  text-align: center;
  width: 100%;
  height: 100%;
  border-top-left-radius: 10px; /* 왼쪽 상단 모서리 */
  border-top-right-radius: 10px;
}
</style>

<!-- <template>
  <video autoplay ref="videoElement" />
</template>

<script setup>
import { onMounted, ref } from "vue";

const props = defineProps({
  streamManager: Object,
});

const videoElement = ref(null);

onMounted(() => {
  if (props.streamManager && videoElement.value) {
    props.streamManager.addVideoElement(videoElement.value);
  }
});
</script> -->
