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
      const isRequestSent = ref(false);
      const interval = setInterval(() => {
        countdown.value--;
        // if (!isRequestSent) {
        //   isRequestSent.value = true; // 요청을 보냈다는 표시를 합니다.

        //   // 여기서 GET 요청을 보냅니다.
        //   axios
        //     .get(`${API_URL}/api/room/start/${roomId}`)
        //     .then(async (response) => {
        //       console.log("첫 번째 서버 응답:", response.data);
        //       // 서버로부터 받은 데이터를 처리합니다.
        //       const secondResponse = await axios.get(
        //         `${API_URL}/api/room/second-request`
        //       );
        //       console.log("두 번째 서버 응답:", secondResponse.data);
        //     })
        //     .catch((error) => {
        //       console.error("요청 중 에러 발생:", error);
        //     });
        // } //백엔드 할때 테스 연결 부분 API 명세서.
        if (countdown.value === 0) {
          clearInterval(interval);
          // 게임 시작 로직

          // 게임 시작 후 추가 카운트다운 시작
          const adInterval = setInterval(() => {
            countdown2.value--;
            console.log(countdown2.value)
            if (countdown2.value === 0) {
              clearInterval(adInterval);
              showAd.value = true; // 광고판 표시
              // console.log("광고판 표시 상태:", showAd.value); // 상태 확인
              subscriber.value = store.subscribers; // 예시: store에 subscribers 상태가 있다고 가정
              // console.log("구독자 목록:", subscriber.value);
              publisher.value = store.publisher;
              // console.log("구독자 목록:", publisher.value);
              combinedParticipants.value = [
                ...subscriber.value,
                publisher.value,
              ];
              updateParticipant();
              // console.log("전체 참가자 목록:", combinedParticipants.value);
              if (gameend.value === false) {
                const participantInterval = setInterval(updateParticipant, 1230);
              } else {
                clearInterval(adInterval);
                console.log('게임 종료')
              }
              return;
            }
          }, 1000);
        }
      }, 1000);
    };

    function updateParticipant() {
      if (combinedParticipants.value.length > nowIndex.value) {
        nowParticipant.value = combinedParticipants.value[nowIndex.value];
        console.log("현재 참가자:", nowParticipant.value);
        nowIndex.value++;

        setTimeout(() => {
          // 2분 후 화면 종료 로직
          console.log("화면 종료");
          
          setTimeout(() => {
            // 3초 후에 다시 화면 시작
            console.log("화면 시작");
          }, 300); // 3초 대기
        }, 1200); // 2분 대기
      } else {
        console.log("끝~~~!!")
        nowParticipant.value = ""
        gameend.value = true
        setTimeout(() => {
          gameend.value = false;
          gameresult.value = true;
        }, 10000);
        return;
      }
    }



    const gameout = () => {
      start.value = false;
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
