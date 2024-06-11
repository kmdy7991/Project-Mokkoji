<template>
  <div class="my-info">
    <div class="info">
      <div class="basic">이름</div>
      <div class="privacy">{{ store.myName }}</div>
      <button class="name-change">이름 변경</button>
    </div>
    <div class="info">
      <div class="basic">전적</div>
      <div class="privacy">100전 100패</div>
    </div>
    <div class="info">
      <div class="basic">승률</div>
      <div class="privacy">0.00%</div>
    </div>
    <div class="info">
      <div class="basic">칭호</div>
      <div class="privacy">아이언</div>
    </div>
    <div class="info">
      <div class="basic">랭킹포인트</div>
      <div class="privacy">0</div>
    </div>
  </div>
</template>

<script setup>
import { userStore } from "@/stores/user";

const store = userStore();
</script>

<style scoped>
.my-info {
  text-align: center;
  width: 100%;
}

.info {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  height: 30px;
  margin-bottom: 3%;
  border-radius: 5px;
  padding: 2%;
  background-color: #0594e0;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.basic {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #00acfc;
  height: 100%;
  width: 15%;
  border-radius: 10px;
  color: white;
  font-family: "DOSMyungjo";
}

.privacy {
  margin-left: 5%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #00acfc;
  height: 100%;
  width: 50%;
  border-radius: 10px;
  color: white;
  font-family: "DOSMyungjo";
}
.name-change {
  margin-left: 5%;
  width: 25%;
  background-color: #00acfc;
  border: none;
  border-radius: 10px;
  color: white;
  transition: background-color 0.3s ease;
  font-family: "DOSMyungjo";
}

.name-change:hover {
  background-color: #3498db; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}
</style>
