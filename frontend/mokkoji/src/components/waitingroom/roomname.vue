<template>
  <div class="room_name">
    <div class="room_number">
      <p>{{ roomId }}</p>
    </div>
    {{ roomName }}
    <div v-if="private" class="screet">
      <img src="@/assets/screetroom.png" alt="비밀방" />
    </div>
  </div>
</template>

<script setup>
import { stringify } from "sockjs-client/dist/sockjs";
import { defineProps } from "vue";

const props = defineProps({
  roomId: Number,
  roomName: String,
  private: Boolean,
});
</script>

<style scoped>
.room_name {
  display: flex; /* Flexbox를 사용합니다 */
  /* justify-content: center; 수평 방향 중앙 정렬 */
  align-items: center;
  width: 70%;
  height: 15%;
  background-color: #0594e0;
  font-family: "DOSMyungjo";
  border-radius: 10px;
  margin: 1% 3%;
}
.room_number {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 3% 3%;
  width: 20%;
  height: 20px;
  background-color: #00acfc;
  text-align: center;
  border-radius: 10px;
  font-family: "LABdigital";
  color: white;
}

.room_number > p {
  margin: 3%;
  object-fit: contain;
}

.screet {
  display: flex;
  width: 60%;
  height: 20px;
  justify-content: end;
  height: 100%;
}

.screet > img {
  height: 40px;
  object-fit: contain;
}
</style>
