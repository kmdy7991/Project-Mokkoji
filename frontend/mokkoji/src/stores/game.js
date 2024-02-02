import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import { useOpenViduStore } from "./openvidu";
import axios from "axios";

export const useGameStore = defineStore(
  "game",
  () => {
    const API_URL = "https://192.168.31.58:443"; // 로컬단 서버로 올릴시 수정할것!
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

    const gameStart = (payload) => {
      const { roomId } = payload;
      axios
        .get(`${API_URL}/api/room/${roomId}/start`)
        .then(() => {
          start.value = true; // 게임 시작 플래그 활성화
          beginCountdown(); // 별도의 카운트다운 함수 호출
        })
        .catch((err) => {
          console.error("게임 시작 오류:", err);
        });
    };

    function beginCountdown() {
      const countdownInterval = setInterval(() => {
        countdown.value--;
        if (countdown.value === 0) {
          clearInterval(countdownInterval); // 첫 번째 카운트다운 종료
          startGameLogic(); // 게임 로직 시작
        }
      }, 1000);
    }

    function startGameLogic() {
      // 게임 시작 로직
      const adCountdownInterval = setInterval(() => {
        countdown2.value--;
        if (countdown2.value === 0) {
          clearInterval(adCountdownInterval); // 광고 카운트다운 종료
          showAd.value = true; // 광고 표시
          updateParticipant(); // 참가자 목록 업데이트
        }
      }, 1000);
    }

    function updateParticipant() {
      if (combinedParticipants.value.length > nowIndex.value) {
        nowParticipant.value = combinedParticipants.value[nowIndex.value];
        console.log("현재 참가자:", nowParticipant.value);
        nowIndex.value++;

        setTimeout(() => {
          // 2분 후, 3초간 여유 시간
          setTimeout(() => {
            // 두 번째 2분 동안 활동
            setTimeout(() => {
              // 다음 참가자로 넘어가거나 게임 종료 처리
              if (nowIndex.value < combinedParticipants.value.length) {
                nowIndex.value++;
                updateParticipant();
              } else {
                gameend.value = true;
                nowParticipant.value = "";
                setTimeout(() => {
                  gameend.value = false;
                  gameresult.value = true;
                }, 10000);
              }
            }, 1200); // 두 번째 2분 120000
          }, 300); // 3초 여유 시간 3000
        }, 1200); // 첫 번째 2분 120000
      }
    }

    const gameout = () => {
      start.value = false;
      gameend.value = false;
      gameresult.value = false;
      countdown.value = 5;
      countdown2.value = 3;
      showAd.value = false;
      nowParticipant.value = false;
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
