<template>
  <div>
    <div v-if="streamManager" id="video-show">
      <ov-video :stream-manager="streamManager" />
    </div>
    <div v-if="streamManager" class="name">
      <p>{{ clientData }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps } from "vue";
import OvVideo from "./OvVideo.vue";

// props 정의
const props = defineProps({
  streamManager: Object,
});

// computed 속성
const clientData = computed(() => {
  const connection = props.streamManager.stream.connection;
  return JSON.parse(connection.data).clientData;
});

// methods
const getConnectionData = () => {
  const connection = props.streamManager.stream.connection;
  return JSON.parse(connection.data);
};
</script>
<style scoped>
#video-show {
  background-color: #08beff;
  width: 100%;
  height: 80px;
  border-top-right-radius: 10px;
  border-top-left-radius: 10px;
}

.name {
  background-color: #d9d9d9;
  text-align: center;
  border-bottom-right-radius: 10px; /* 오른쪽 하단 모서리 */
  border-bottom-left-radius: 10px; /* 왼쪽 하단 모서리 */
  height: 20px;
  margin-bottom: 5%;
}

.name > p {
  margin: 0%;
  padding: 0%;
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
