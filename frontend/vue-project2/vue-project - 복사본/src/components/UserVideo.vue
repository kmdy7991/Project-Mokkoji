<template>
  <div v-if="streamManager" id="video-show">
    <ov-video :stream-manager="streamManager" />
    <div>
      <p>{{ clientData }}</p>
    </div>
  </div>
</template>

<script>
import OvVideo from "./OvVideo.vue";

export default {
  name: "UserVideo",

  components: {
    OvVideo,
  },

  props: {
    streamManager: Object,
  },

  computed: {
    clientData() {
      const { clientData } = this.getConnectionData();
      return clientData;
    },
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    },
  },
};
</script>
<style>
#video-show {
  width: 300px;
  height: 300px;
}
video {
  width: 300px;
  height: 300px;
}
</style>

<!-- <template>
  <div v-if="streamManager">
    <ov-video :stream-manager="streamManager" />
    <div>
      <p>{{ clientData }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import OvVideo from "./OvVideo.vue";

const props = defineProps({
  streamManager: Object,
});

const clientData = computed(() => {
  if (!props.streamManager) {
    return "";
  }

  const connection = props.streamManager.stream.connection;
  return JSON.parse(connection.data).clientData;
});
</script> -->
