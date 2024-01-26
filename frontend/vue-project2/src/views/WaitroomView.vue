<template>
  <div class="main">
    <div class="title">
      <h1>모꼬지</h1>
      <div class="buttons">
        <button class="button" @click="logMessage">
          <img src="@/assets/rank.png" alt="rank.png" />
        </button>
        <button class="button" @click="GoGameroom($event)" data-room-id="1">
          <img src="@/assets/bodyword.png" alt="bodyword.png" />
        </button>
        <button class="button" @click="GoGameroom($event)" data-room-id="2">
          버튼3
        </button>
        <button class="redbutton">
          <RouterLink :to="{ name: 'home' }">
            <img src="@/assets/logout.png" alt="logout.png" />
          </RouterLink>
        </button>
      </div>
    </div>
    <div>
      <WaitingArea />
    </div>
  </div>
</template>

<script setup>
import WaitingArea from "@/components/waitingroom/WaitingArea.vue";
import { useRouter } from "vue-router";

const router = useRouter();
const logMessage = () => {
  console.log(1);
};

const GoGameroom = (event) => {
  const roomId = event.currentTarget.dataset.roomId;
  console.log("goTalkbodyroom" + " " + roomId);
  router.push({ name: "TalkBody", params: { id: roomId } });
};
</script>
<style scoped>
.title {
  display: flex; /* Flexbox 컨테이너 설정 */
  align-items: center; /* 수직 정렬 */
  color: white; /* 글자색 설정 */
  font-family: "DOSMyungjo"; /* 폰트 설정 */
}
.title > h1 {
  margin-left: 3%; /* 제목 왼쪽 여백 설정 */
  flex-grow: 1; /* 제목이 남은 공간을 모두 차지하도록 설정 */
}

.buttons {
  display: flex; /* 버튼을 위한 Flexbox 컨테이너 설정 */
  justify-content: flex-end; /* 버튼을 오른쪽 끝으로 정렬 */
}

.buttons > button {
  margin-left: 10px; /* 버튼 사이의 간격 설정 */
}

.button {
  background-color: #12deff;
  border: #036be7;
  width: 50px; /* 버튼의 너비 */
  height: 50px; /* 버튼의 높이를 너비와 같게 설정하여 정사각형 만들기 */
  line-height: 50px; /* 텍스트를 버튼 중앙에 위치시키기 */
  text-align: center; /* 텍스트 중앙 정렬 */
  margin: 0 5px; /* 버튼 사이의 간격 */
  border-radius: 10%; /* 버튼 모서리를 약간 둥글게 */
  transition: background-color 0.3s ease; /* 부드러운 색상 전환 효과 */
  /* 기본 버튼 스타일링 */
  color: white; /* 기본 글자색 */
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.button img {
  width: 100%; /* 이미지 너비를 버튼 너비에 맞춤 */
  height: 100%; /* 이미지 높이를 버튼 높이에 맞춤 */
  object-fit: contain; /* 이미지 비율을 유지하면서 버튼 내에 맞춤 */
}

.button:hover {
  background-color: #3498db; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

.redbutton {
  background-color: #ff2001;
  border: #ff2001;
  width: 50px; /* 버튼의 너비 */
  height: 50px; /* 버튼의 높이를 너비와 같게 설정하여 정사각형 만들기 */
  line-height: 50px; /* 텍스트를 버튼 중앙에 위치시키기 */
  text-align: center; /* 텍스트 중앙 정렬 */
  margin: 0 5px; /* 버튼 사이의 간격 */
  border-radius: 10%; /* 버튼 모서리를 약간 둥글게 */
  transition: background-color 0.3s ease; /* 부드러운 색상 전환 효과 */
  /* 기본 버튼 스타일링 */
  color: white; /* 기본 글자색 */
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.redbutton img {
  width: 100%; /* 이미지 너비를 버튼 너비에 맞춤 */
  height: 100%; /* 이미지 높이를 버튼 높이에 맞춤 */
  object-fit: contain; /* 이미지 비율을 유지하면서 버튼 내에 맞춤 */
}

.redbutton:hover {
  background-color: #dd2b14; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

.live-player {
  width: 25%;
}

.game-room {
  width: 75%;
}
</style>
