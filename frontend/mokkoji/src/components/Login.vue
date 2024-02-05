<template>
  <div id="join-dialog" class="jumbotron vertical-center">
    <h1>모꼬지 로그인 임시 화면</h1>
    <div class="form-container">
      <div class="form-group">
        <label class="label-color">별명</label>
        <input
          v-model="username"
          class="form-control"
          type="text"
          placeholder="별명을 입력해주세요"
        />
      </div>
      <p class="text-center">
        <button class="btn" @click="goTogame()">입장</button>
        <button class="btn" @click="goToAuthView()">관리자 입장</button>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoomStore } from "@/stores/room";
import { userStore } from "@/stores/user";
import { useRouter } from "vue-router";

const router = useRouter();
const roomStore = useRoomStore();
const username = ref("");
const store = userStore();

// const click = async () => {
//   await axios
//     .post("http://192.168.31.42:8080/info", {
//       // nickname: "사용자 닉네임",
//       roomId: roomnumber.value,
//       userId: username.value,
//     })
//     .then((response) => {
//       console.log("서버 응답:", response.data);
//     })
//     .catch((error) => {
//       console.error("요청 오류:", error);
//     })
//     .finally(() => {
//       if (username.value.trim() === "") {
//         store.setMyName("Participant" + Math.floor(Math.random() * 100));
//       } else {
//         store.setMyName(username.value);
//       }
//     });
// };

const goToAuthView = () => {
  if (username.value.trim() === "") {
    store.myName = "Participant" + Math.floor(Math.random() * 100);
  } else {
    store.myName = username.value
  }
  store.Auth = !store.Auth;
  roomStore.getRoomlist();
  console.log(store.Auth);
  router.replace({ name: "Auth" });
};

const goTogame = () => {
  if (username.value.trim() === "") {
    store.myName = "Participant" + Math.floor(Math.random() * 100);
  } else {
    store.myName = username.value
  }
  roomStore.getRoomlist();
  router.replace({ name: "waitRoom" });
};
</script>

<style scoped>
.jumbotron {
  /* background-image: url("/src/assets/moggozi.png");
  background-size: cover; /* 배경 이미지가 div를 전체적으로 커버하도록 설정 
  background-position: center; */
  padding: 3%;
  width: 80%;
  height: 80%;
  margin-left: 5%;
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
  cursor: click;
}

.text-center {
  display: flex;
}
</style>
