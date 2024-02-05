import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import { useOpenViduStore } from "./openvidu";
import axios from "axios";

export const useGameStore = defineStore(
  "game",
  () => {
    const API_URL = "http://127.0.0.1:8080"; // 로컬단 서버로 올릴시 수정할것!
    const start = ref(false);
    const countdown = ref(5);
    const countdown2 = ref(3);
    const showAd = ref(false);
    const store = useOpenViduStore();
    const subscriber = ref([]);
    const publisher = ref();
    const combinedParticipants = ref([]); // New reference for combined data
    const nowParticipant = ref("");
    const nowIndex = ref(0);
    const gameend = ref(false);
    const gameresult = ref(false);
    const gameStart = async (payload) => {
      const { roomId } = payload;
      start.value = true;
      countdown.value = 5;
      const interval = setInterval(() => {
        countdown.value--;

        if (countdown.value === 0) {
          clearInterval(interval);
          showAd.value = false; // 게임 시작 직전 광고 비활성화
          initGame();
        }
      }, 1000);
    };
    function initGame() {
      subscriber.value = store.subscribers;
      publisher.value = store.publisher;
      combinedParticipants.value = [...subscriber.value, publisher.value];

      // 첫 참가자 시작을 3초 후로 지연
      setTimeout(() => {
        showAd.value = true;
        updateParticipant(); // 첫 참가자 시작
      }, 3000); // 프론트 만 접속시
      // axios
      //   .get(`${API_URL}/api/room/start/${roomId}`)
      //   .then((response) => {
      //     console.log("서버 응답:", response.data);
      //     // 참가자 정보 업데이트 등 초기화 로직
      //     subscriber.value = store.subscribers;
      //     publisher.value = store.publisher;
      //     combinedParticipants.value = [...subscriber.value, publisher.value];

      //     // 첫 참가자 시작을 3초 후로 지연
      //     setTimeout(() => {
      //       showAd.value = true;
      //       updateParticipant(); // 첫 참가자 시작
      //     }, 3000);
      //   })
      //   .catch((error) => {
      //     console.error("게임 시작 요청 에러:", error);
      //   }); // 백엔드 요청시
    }

    function updateParticipant() {
      if (combinedParticipants.value.length > nowIndex.value) {
        // 유저 데이터 값을 백엔드에서 받아올 예정.
        nowParticipant.value = combinedParticipants.value[nowIndex.value];
        console.log(
          "현재 참가자: game.js 71",
          nowParticipant.value.stream.connection.data
        );
        // 2분 후, 3초간 여유 시간
        setTimeout(() => {
          // 두 번째 2분 동안 활동
          setTimeout(() => {
            // 다음 참가자로 넘어가거나 게임 종료 처리
            nowIndex.value++;
            updateParticipant();
          }, 3000); // 두 번째 2분 120000
        }, 10000); // 3초 여유 시간 3000
      } else {
        endGame();
      }
    }

    function endGame() {
      gameend.value = true;
      nowParticipant.value = "";
      nowIndex.value = 0;
      setTimeout(() => {
        gameend.value = false;
        gameresult.value = true; // 결과 표시 시 광고 비활성화
      }, 10000); // 게임 종료 후 10초 대기, 결과 표시
    }

    const gameout = () => {
      start.value = false;
      gameend.value = false;
      gameresult.value = false;
      countdown.value = 5;
      countdown2.value = 3;
      showAd.value = false;
      nowParticipant.value = "";
      nowIndex.value = 0;
    };

    // Vue 3 Composition API를 사용하여 mounted 시점에 로직을 실행할 수 있습니다.
    onMounted(() => {
      // 여기에 필요한 로직을 추가합니다.
    });

    return {
      start,
      countdown,
      countdown2,
      showAd,
      subscriber,
      publisher,
      combinedParticipants,
      nowParticipant,
      nowIndex,
      gameStart,
      gameout,
      gameend,
      gameresult,
    };
  },
  { persist: true }
);
