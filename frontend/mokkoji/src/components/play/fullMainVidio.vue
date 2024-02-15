<template>
  <video class="live" autoplay />
</template>

<script>
export default {
  name: "fullMainVidio",

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
  border-radius: 10px;
  object-fit: cover;
}
</style>
