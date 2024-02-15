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
  object-fit: cover;
}
</style>
