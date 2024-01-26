<template>
  <video autoplay />
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
