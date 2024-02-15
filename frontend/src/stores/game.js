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
    const showAd = ref(false);
    const store = useOpenViduStore();
    const socketstore = useWebSocketStore();
    const roomstore = useRoomStore();
    const userstore = userStore();
    const category = ref("");
    const answers = ref("");
    const subscriber = ref([]);
    const publisher = ref();
    const combinedParticipants = ref([]); // New reference for combined data
    const nowParticipant = ref("");
    const nowIndex = ref(0);
    const nowturn = ref("");
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
      nowcountdown.value = true;
      getcategory(roomId);
      const numroomId = Number(roomId);
      roomstore.getplayer(numroomId);
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
      subscriber.value = store.subscribers;
      publisher.value = store.publisher;
      combinedParticipants.value = [...subscriber.value, publisher.value];

      // 첫 참가자 시작을 3초 후로 지연
      setTimeout(() => {
        showAd.value = true;
        nowcountdown.value = false;
        answers.value = "";
        updateParticipant(roomId); // 첫 참가자 시작
      }, 3000);
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
          setTimeout(() => {
            setTimeout(() => {
              // 다음 참가자로 넘어가거나 게임 종료 처리
              nowParticipant.value = "";
              nowIndex.value++;
              answers.value = "";
              nowturn.value = "";
              updateParticipant(roomId);
            }, 3000); // 3초동안 정지
          }, 30000);
        } else {
          nowIndex.value++;
          nowturn.value = "";
          updateParticipant(roomId);
        }
        // 게임진행 2분
      } else {
        answers.value = "";
        nowturn.value = "";
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
      }, 3000); // 게임 종료 후 10초 대기, 결과 표시
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
