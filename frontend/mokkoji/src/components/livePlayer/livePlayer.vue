<template>
  <div class="container">
    <div class="levelarae">
      <div class="level">레벨</div>
    </div>
    <div>
      <myinfo />
    </div>
    <div class="players">
      <otherplayer />
    </div>
  </div>
</template>

<script setup>
import myinfo from "./myinfo.vue";
import otherplayer from "./otherplayer.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.levelarae {
  width: 100%;
  background-color: #015dba;
  height: 30px;
  border-top-left-radius: 10px; /* 왼쪽 상단 모서리 */
  border-top-right-radius: 10px;
}
.level {
  margin-left: 10%;
  padding-top: 2%;
  font-family: "DOSMyungjo";
}
.players {
  margin: 2% 5%;
  height: 50%;
  border-radius: 10px;
}
</style>
