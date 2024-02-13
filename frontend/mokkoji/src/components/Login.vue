<template>
  <div id="join-dialog" class="jumbotron vertical-center">
    <h1 class="gamename">모꼬지</h1>
    <div class="form-container">
      <!-- <div class="social">
        <img class="kakao" src="@/assets/kakao_login.png" alt="kakao_login" />
      </div> -->
      <div class="social">
        <img class="naver" src="@/assets/naver_login.png" alt="naver_login" />
      </div>
      <!-- <div class="social">
        <img class="naver" src="@/assets/google_login.png" alt="google_login" />
      </div> -->
      <div class="text-center">
        <button class="btn" @click="goTogame()">비회원 입장</button>
        <button class="btn" @click="goToAuthView()">관리자 입장</button>
      </div>
      <!-- <button @click="toggleModal">임시 닉네임 확인</button> -->
    </div>
    <div>
      <Authmodal :show="AuthshowModal" @close="AuthshowModal = false" />
    </div>
    <inputnickname :show="showModal" @close="showModal = false" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoomStore } from "@/stores/room";
import { useRouter } from "vue-router";
import { userStore } from "@/stores/user";
import profanities from "@/assets/profanities";
import inputnickname from "./inputnickname.vue";
import Authmodal from "./Authmodal.vue";

const roomStore = useRoomStore();
const username = ref("");
const router = useRouter();
const store = userStore();
const specialChar = ref(false);
const fuckword = ref(false);
const showModal = ref(false);
const AuthshowModal = ref(false);

const toggleModal = () => {
  showModal.value = !showModal.value;
};

const AuthtoggleModal = () => {
  AuthshowModal.value = !AuthshowModal.value;
};

onMounted(() => {
  username.value = "";
});

const isValidUsername = (name) => {
  // 영어, 한글, 숫자만을 허용하는 정규표현식
  const regex = /^[A-Za-z가-힣0-9]+$/;
  return regex.test(name.value);
};

const containsProfanity = (name) => {
  return profanities.some((profanity) => name.value.includes(profanity));
};

async function goToAuthView() {
  username.value = "mokko" + Math.floor(Math.random() * 10000);
  await new Promise((resolve) => setTimeout(resolve, 100));
  store.myName = username.value;
  specialChar.value = false;
  fuckword.value = false;
  store.Auth = true;

  if (isValidUsername(username)) {
    specialChar.value = false;
    if (containsProfanity(username)) {
      fuckword.value = true;
    } else {
      roomStore.getRoomlist();
      store.createuser();
      // if (store.Auth === true) {
      //   router.replace({ name: "Auth" });
      // } else {
      //   router.replace({ name: "waitRoom" });
      // }
    }
  } else {
    specialChar.value = true;
  }
}

async function goTogame() {
  username.value = "mokko" + Math.floor(Math.random() * 10000);
  await new Promise((resolve) => setTimeout(resolve, 100));
  store.myName = username.value;
  specialChar.value = false;
  fuckword.value = false;
  store.Auth = false;

  if (isValidUsername(username)) {
    specialChar.value = false;
    if (containsProfanity(username)) {
      fuckword.value = true;
    } else {
      roomStore.getRoomlist();
      store.createuser();
      // if (store.Auth === true) {
      //   router.replace({ name: "Auth" });
      // } else {
      //   router.replace({ name: "waitRoom" });
      // }
    }
  } else {
    specialChar.value = true;
  }
}
</script>

<style scoped>
.jumbotron {
  /* background-image: url("/src/assets/moggozi.png");
  background-size: cover; /* 배경 이미지가 div를 전체적으로 커버하도록 설정 
  background-position: center; */
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

.form-group {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* label을 왼쪽 끝에 정렬 */
  margin-bottom: 10px; /* 각 입력 필드 간 간격 조정 */ /* 각 입력 필드 간 간격 조정 */
}

.form-control::placeholder {
  margin-left: 3%;
  color: white; /* 플레이스홀더 텍스트의 색상을 흰색으로 설정 */
  opacity: 1; /* 플레이스홀더 텍스트의 불투명도를 조정 (필요에 따라 조정) */
}

.form-control {
  background-color: #00c3f4;
  color: white;
  font-weight: bold;
  text-indent: 2%;
  height: 30px;
  width: 100%;
  border-radius: 5px;
}

.label-color {
  color: white;
  font-weight: bold;
  font-size: 24px;
  display: block; /* label을 블록 요소로 만들어 줄바꿈을 만듭니다. */
  text-align: left;
  width: 100%;
  margin-bottom: 5px;
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

.doublename {
  color: red;
  font-size: 28px;
  font-family: "DOSMyungjo";
}

.social {
  width: 600px;
  height: 50px;
  margin-bottom: 10px;
}

.kakao {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  object-fit: cover;
}

.naver {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  object-fit: cover;
}

.naver:hover {
  cursor: pointer;
}

.gamename {
  font-family: DOSMyungjo;
}
</style>
