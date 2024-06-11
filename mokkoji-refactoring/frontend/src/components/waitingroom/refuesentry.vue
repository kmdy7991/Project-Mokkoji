<template>
  <div v-if="store.refuesentry" class="result">
    <div class="roading">
      <div class="pwd">
        <div>!해당 방은 현재 접근할수 없습니다.</div>
        <div>
          새로고침을 하시거나 해당 문제가 지속될경우 재접속 해주시기 바랍니다.
        </div>
      </div>
      <div class="checkbuttons">
        <button @click="closeModal" class="checkbutton">확인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from "vue";
import { useRoomStore } from "@/stores/room";

const store = useRoomStore();

const emit = defineEmits(["close"]);

function closeModal() {
  emit("close");
  store.refuesentry = false;
}
</script>

<style scoped>
.result {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: none;
  position: fixed; /* 고정 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비 */
  height: 100%;
  z-index: 1000;
}

.roading {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 50%;
  height: 40%;
  background-color: #10c2df;
  font-size: 48px;
  font-family: "DOSMyungjo";
}

.pwd {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 80%;
  height: 20%;
  margin-top: 10%;
  background-color: #0594e0;
  color: red;
  border-radius: 10px;
  font-size: 20px;
}

.checkbuttons {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 3%;
  width: 80%;
}

.checkbutton {
  width: 160px;
  height: 50px;
  background-color: #00acfc;
  margin-left: 5%;
  border: #0082fc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  color: white;
  font-size: 24px;
  transition: background-color 0.3s ease;
  font-family: "DOSMyungjo";
}

.checkbutton:hover {
  background-color: #fdc909;
}
</style>
