import { ref } from "vue";
import { defineStore } from "pinia";
import { useOpenViduStore } from "./openvidu";
import { useWebSocketStore } from "./socket";
import { useRoomStore } from "./room";
import { userStore } from "./user";
import axios from "axios";

export const useGameStore = defineStore(
  "game",
  () => {
    const start = ref(false);
    const nowcountdown = ref(false);
    const countdown = ref(5);
    const countdown2 = ref(3);
    const gamecountdown = ref(30);
    const showAd = ref(false);
    const store = useOpenViduStore();
    const socketstore = useWebSocketStore();
    const roomstore = useRoomStore();
    const userstore = userStore();
    const category = ref("");
    const answers = ref("");
    const subscriber = ref([]);
    const publisher = ref();
    const nowturn = ref("");
    const combinedParticipants = ref([]); // New reference for combined data
    const nowParticipant = ref("");
    const nowIndex = ref(0);
    const gameend = ref(false);
    const gameresult = ref(false);
    const ranks = ref([]);

    const getcategory = (roomId) => {
      axios({
        method: "get",
        url: `/api/talkbody/${roomId}/select`,
      })
        .then((res) => {
          category.value = res.data.subject;
        })
        .catch((err) => {});
    };

    const gameover = (roomId) => {
      axios({
        method: "get",
        url: `/api/room/${roomId}/start`,
      })
        .then((res) => {})
        .catch((err) => {});
    };
    const gameStart = async (roomId) => {
      const numroomId = Number(roomId);
      getcategory(roomId);
      axios({
        method: "get",
        url: `/api/room/${numroomId}/start`,
      })
        .then((res) => {})
        .catch((err) => {});
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
      gamecountdown.value = 30;
      subscriber.value = store.subscribers;
      publisher.value = store.publisher;
      combinedParticipants.value = [...subscriber.value, publisher.value];

      // 첫 참가자 시작을 3초 후로 지연
      setTimeout(() => {
        showAd.value = true;
        nowcountdown.value = false;
        answers.value = "";
        updateParticipant(roomId); // 첫 참가자 시작
      }, 3000); // 프론트 만 접속시
    }

    function updateParticipant(roomId) {
      if (combinedParticipants.value.length > nowIndex.value) {
        const nowplay = roomstore.players[nowIndex.value].user_nickname;
        nowturn.value = nowplay;
        if (userstore.myName.matchAll(nowplay)) {
          socketstore.getTHEME();
        }
        let foundIndex = -1;
        for (let i = 0; i < combinedParticipants.value.length; i++) {
          const nowPart = combinedParticipants.value[i];
          const clientdata = JSON.parse(nowPart.stream.connection.data);
          const client = clientdata.clientData;
          if (nowplay == client) {
            foundIndex = i; // 일치하는 인덱스 저장
            break; // 반복문 탈출
          }
        }
        if (foundIndex !== -1) {
          nowParticipant.value = combinedParticipants.value[foundIndex];
          const countdownInterval = setInterval(() => {
            gamecountdown.value -= 1;
            if (gamecountdown.value === 0) {
              clearInterval(countdownInterval); // 카운트다운이 끝나면 인터벌을 중지
              // 카운트다운이 0에 도달했을 때 실행할 로직
              setTimeout(() => {
                // 다음 참가자로 넘어가거나 게임 종료 처리
                nowParticipant.value = "";
                nowIndex.value++;
                answers.value = "";
                nowturn.value = "";
                gamecountdown.value = 30;
                updateParticipant(roomId);
              }, 3000); // 3초 후에 실행
            }
          }, 1000); // 1초마다 실행
        } else {
          nowIndex.value++;
          nowturn.value = "";
          updateParticipant(roomId);
        }
        // 게임진행 30초
      } else {
        answers.value = "";
        nowturn.value = "";
        gamecountdown.value = 30;
        endGame(roomId);
      }
    }

    function endGame(roomId) {
      socketstore.gameEnd();
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
      nowcountdown.value = false;
      countdown.value = 5;
      countdown2.value = 3;
      showAd.value = false;
      nowParticipant.value = "";
      nowIndex.value = 0;
    };

    return {
      start,
      countdown,
      countdown2,
      gamecountdown,
      showAd,
      subscriber,
      category,
      answers,
      publisher,
      combinedParticipants,
      nowParticipant,
      nowIndex,
      nowturn,
      gameStart,
      gameout,
      gameend,
      gameresult,
      nowcountdown,
      ranks,
    };
  },
  { persist: true }
);
