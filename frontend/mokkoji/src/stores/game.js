import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import { useOpenViduStore } from "./openvidu";
import { useWebSocketStore } from "./socket";
import { useRoomStore } from "./room";
import axios from "axios";

export const useGameStore = defineStore(
  "game",
  () => {
    const API_URL = "http://192.168.31.58:8080"; // 로컬단 서버로 올릴시 수정할것!
    const start = ref(false);
    const nowcountdown = ref(false);
    const countdown = ref(5);
    const countdown2 = ref(3);
    const showAd = ref(false);
    const store = useOpenViduStore();
    const socketstore = useWebSocketStore();
    const roomstore = useRoomStore();
    const category = ref("");
    const answers = ref("");
    const subscriber = ref([]);
    const publisher = ref();
    const combinedParticipants = ref([]); // New reference for combined data
    const nowParticipant = ref("");
    const nowIndex = ref(0);
    const gameend = ref(false);
    const gameresult = ref(false);

    const getcategory = (roomId) => {
      axios({
        method: "get",
        url: `${API_URL}/api/talkbody/${roomId}/select`,
      })
        .then((res) => {
          // console.log("주제 응답:", res.data);
          category.value = res.data.subject;
          // answers.value = res.data.elements;
        })
        .catch((err) => {
          console.error("주제 받아오기 실패:", err);
        });
    };

    const gameover = (roomId) => {
      axios({
        method: "get",
        url: `${API_URL}/api/room/${roomId}/start`,
      })
        .then((res) => {
          // console.log("시작 응답:", res);
        })
        .catch((err) => {
          console.error("게임 종료 요청 에러:", err);
        });
    };
    const gameStart = async (roomId) => {
      nowcountdown.value = true;
      const numroomId = Number(roomId);
      console.log(typeof numroomId);
      roomstore.getplayer(numroomId);
      axios({
        method: "get",
        url: `${API_URL}/api/room/${numroomId}/start`,
      })
        .then((res) => {
          // console.log("시작 응답:", res);
        })
        .catch((err) => {
          console.error("게임 시작 요청 에러:", err);
        });
      getcategory(roomId);
      start.value = true;
      countdown.value = 5;
      nowcountdown.value = true;
      const interval = setInterval(() => {
        countdown.value--;

        if (countdown.value === 0) {
          clearInterval(interval);
          showAd.value = false; // 게임 시작 직전 광고 비활성화
          initGame(roomId);
        }
      }, 1000);
    };
    function initGame(roomId) {
      subscriber.value = store.subscribers;
      publisher.value = store.publisher;
      combinedParticipants.value = [...subscriber.value, publisher.value];

      // 첫 참가자 시작을 3초 후로 지연
      setTimeout(() => {
        showAd.value = true;
        nowcountdown.value = false;
        updateParticipant(roomId); // 첫 참가자 시작
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

    function updateParticipant(roomId) {
      if (combinedParticipants.value.length > nowIndex.value) {
        const nowplay = roomstore.players[nowIndex.value].user_nickname;
        let foundIndex = -1;
        for (let i = 0; i < combinedParticipants.value.length; i++) {
          const nowPart = combinedParticipants.value[i];
          const clientdata = JSON.parse(nowPart.session.connection.data);
          const client = clientdata.clientData;
          if (nowplay == client) {
            foundIndex = i; // 일치하는 인덱스 저장
            break; // 반복문 탈출
          }
        }
        if (foundIndex !== -1) {
          nowParticipant.value = combinedParticipants.value[foundIndex];
          getcategory(roomId);
          // console.log(
          //   "현재 참가자: game.js 71",
          //   JSON.parse(nowParticipant.value.stream.connection.data)
          // );
          // 2분 후, 3초간 여유 시간
          setTimeout(() => {
            setTimeout(() => {
              // 다음 참가자로 넘어가거나 게임 종료 처리
              nowParticipant.value = "";
              nowIndex.value++;
              updateParticipant(roomId);
            }, 3000); // 3초동안 정지
          }, 10000);
        } else {
          nowIndex.value++;
          updateParticipant(roomId);
        }
        // 게임진행 2분
      } else {
        endGame(roomId);
      }
    }

    function endGame(roomId) {
      gameend.value = true;
      nowParticipant.value = "";
      nowIndex.value = 0;
      category.value = "";
      setTimeout(() => {
        gameend.value = false;
        gameresult.value = true; // 결과 표시 시 광고 비활성화
        gameover(roomId);
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
      category,
      answers,
      publisher,
      combinedParticipants,
      nowParticipant,
      nowIndex,
      gameStart,
      gameout,
      gameend,
      gameresult,
      nowcountdown,
    };
  },
  { persist: true }
);