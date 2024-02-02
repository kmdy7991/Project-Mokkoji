<template>
  <div v-if="check" class="result">
    <div class="roading">
      <div class="pwd">
        <div class="password">
          비밀번호
        </div>
        <div class="pwdinput">
          <input type="password" class="inputpass" v-model="myinput">
        </div>
      </div>
      <div class="checkbuttons">
        <button @click.native="joinRoomDirectly(roomId, myinput)" class="checkbutton">확인</button>
        <button @click="closeModal" class="checkbutton">취소</button>
      </div>
      </div>
      
  </div>
</template>

<script setup>
import { ref, defineEmits, defineProps } from "vue";
import { useRoomStore }from "@/stores/room";

const store = useRoomStore()

const myinput = ref('')

const props = defineProps({
  check: Boolean,
  roomId: Number
});

const joinRoomDirectly = (roomId, secret) => {
  // roomId가 존재할 때만 세션에 참여
  const payload = {
    roomId : roomId,
    secret : secret,
  }
  console.log(roomId)
  store.entranceSecretRoom(payload)
  console.log(secret)
  emit("close");
};

const emit = defineEmits(["close"]);

function closeModal() {
  emit("close");
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
  height: 30%;
  background-color: #10C2DF;
  font-size: 48px;
  font-family: "DOSMyungjo";
}

.pwd {
  display: flex;
  align-items: center;
  width: 80%;
  height: 20%;
  margin-top: 10%;
  background-color: #0594E0;
  border-radius: 10px;
}

.password {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30%;
  height: 80%;
  margin-left: 5%;
  border-radius: 10px;
  background-color: #00ACFC;
  font-size: 28px;
  color: white;
}
.pwdinput{
  width: 60%;
  height: 80%;
  margin-left: 2%;
  border-radius: 10px;
  background-color: #00ACFC;
}

.inputpass{
  width: 90%;
  height: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 4%;
  margin-top: 1%;
  border-radius: 10px;
  border: none;
  font-size: 24px;
}
.checkbuttons{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5%;
  width: 80%;
}

.checkbutton {
  width: 160px;
  height: 50px;
  background-color: #00ACFC;
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
