<template>
  <div id="join-dialog" class="jumbotron vertical-center">
    <h1 class="gamename">모꼬지</h1>
    <div class="form-container">
      <div class="text-center">
        <button class="btn" @click="goTogame()">비회원 입장</button>
        <button class="btn" @click="AuthtoggleModal">관리자 입장</button>
      </div>
    </div>
    <div>
      <Authmodal :show="AuthshowModal" @close="AuthshowModal = false" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoomStore } from "@/stores/room";
import { userStore } from "@/stores/user";
import Authmodal from "./Authmodal.vue";

const roomStore = useRoomStore();
const username = ref("");
const store = userStore();
const specialChar = ref(false);
const fuckword = ref(false);
const AuthshowModal = ref(false);

const AuthtoggleModal = () => {
  AuthshowModal.value = !AuthshowModal.value;
};

onMounted(() => {
  username.value = "";
});

async function goTogame() {
  username.value = "mokko" + Math.floor(Math.random() * 10000);
  await new Promise((resolve) => setTimeout(resolve, 100));
  store.myName = username.value;
  specialChar.value = false;
  fuckword.value = false;
  store.Auth = false;
  roomStore.getRoomlist();
  store.createuser();
}
</script>

<style scoped>
.jumbotron {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.jumbotron > h1 {
  color: white;
  font-weight: bold;
  text-align: center;
  font-size: 48px;
}

.form-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 자식 요소들을 중앙 정렬 */
  width: 100%;
}
.btn {
  padding: 10px 20px; /* 상하좌우 패딩으로 버튼 크기 조정 */
  font-size: 18px; /* 버튼 내 텍스트 크기 */
  width: 200px; /* 버튼의 너비 */
  height: 50px; /* 버튼의 높이 */
  /* 추가 스타일링이 필요하면 여기에 추가 */
  display: block; /* 버튼을 블록 레벨 요소로 만들어 줄바꿈을 만듭니다. */
  margin: 20px; /* 버튼을 수평 중앙에 배치 */
  background-color: #00acfc;
  border-color: #00acfc;
  border-radius: 10px;
  transition: background-color 0.3s ease;
  color: white;
}

.btn:hover {
  background-color: #3498db; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: pointer;
}

.text-center {
  display: flex;
}

.gamename {
  font-family: DOSMyungjo;
}
</style>
