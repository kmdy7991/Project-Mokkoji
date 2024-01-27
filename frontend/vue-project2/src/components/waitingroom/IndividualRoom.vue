<template>
  <div class="waiting_card">
    <div class="game_picture">
      <gamepicture :gameType = "room.game_type"></gamepicture>
      <roomname :gameName = "room.room_name" :gameID = "room.room_id"></roomname>
    </div>
  </div>
</template>

<script setup>
import gamepicture from "./gamepicture.vue";
import roomname from "./roomname.vue";
const { room } = defineProps(["room"]);
</script>
<style scoped>
.waiting_card {
  background-color: #12deff;
  width: 40%;
  height: 100%;
  margin: 1% 2%;
  padding: 5px;
  border-radius: 5px;
}
.game_picture {
  display: flex; /* Flexbox 레이아웃을 사용합니다 */
  flex-direction: row;
  margin: 0% 3%;
  height: 80%;
}
</style>
