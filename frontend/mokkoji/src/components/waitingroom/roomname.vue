<template>
  <div class="room_name">
    <div class="room_number">
      <p>000</p>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.room_name {
  display: flex; /* Flexbox를 사용합니다 */
  /* justify-content: center; 수평 방향 중앙 정렬 */
  align-items: center;
  width: 70%;
  height: 15%;
  background-color: #0594e0;
  border-radius: 10px;
  margin: 1% 3%;
}
.room_number {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 3% 3%;
  width: 20%;
  height: 70%;
  background-color: #00acfc;
  text-align: center;
  border-radius: 10px;
  color: white;
}

.room_number > p {
  margin: 3%;
  object-fit: contain;
}
</style>
