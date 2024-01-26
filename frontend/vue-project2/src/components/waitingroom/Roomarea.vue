<template>
  <div class="game_room">
    <IndividualRoom
      v-for="number in 6"
      :key="number"
      itemKey="number"
      class="welcome_itme"
    >
    </IndividualRoom>
  </div>
</template>

<script setup>
import IndividualRoom from "./IndividualRoom.vue";
</script>
<style>
.game_room {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 3%;
  background-color: #054ca3;
  border-radius: 10px;
}

.welcome_items {
  display: flex;
  flex-wrap: wrap; /* 요소들이 넘칠 경우 다음 줄로 넘어가도록 설정 */
}
</style>
