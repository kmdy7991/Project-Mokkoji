<template>
  <div class="uservidio">
    <div class="vidio">
      <img src="@/assets/bodyword.png" alt="bodyword.png" width="60px" />
    </div>
    <div class="name">
      {{ index }}
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  index: Number,
});
</script>

<style scoped>
.vidio {
  background-color: #08beff;
  width: 100%;
  height: 15%;
  border-top-left-radius: 10px; /* 왼쪽 상단 모서리 */
  border-top-right-radius: 10px; /* 오른쪽 상단 모서리 */
}
.name {
  background-color: #d9d9d9;
  text-align: center;
  border-bottom-right-radius: 10px; /* 오른쪽 하단 모서리 */
  border-bottom-left-radius: 10px; /* 왼쪽 하단 모서리 */
  height: 25px;
}
.uservidio {
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  border-bottom-right-radius: 10px; /* 오른쪽 하단 모서리 */
  border-bottom-left-radius: 10px;
  margin-bottom: 10%;
}
</style>
