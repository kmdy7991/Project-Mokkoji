import { ref, computed, onMounted } from "vue";
import { defineStore } from "pinia";

export const useChatStore = defineStore(
  "chat",
  () => {
    let id = 0;
    const roomId = ref("");
    const setRoomId = (newRoomId) => {
      roomId.value = newRoomId;
    };
    const chats = ref([
      {
        sessionId: roomId.value,
        name: "시스템",
        text: "게임에 참가하였습니다.",
      },
    ]);

    const addChat = function (inputChat) {
      chats.value.push({
        sessionId: inputChat.sessionId,
        name: inputChat.userName,
        text: inputChat.content,
      });
    };

    const removeChat = function (sessionIdToRemove) {
      chats.value = chats.value.filter(
        (chat) => chat.sessionId !== sessionIdToRemove
      );
    };

    function startChat() {
      let counter = 5;
      const intervalId = setInterval(() => {
        console.log(counter);
        counter--;
        if (counter === 0) {
          clearInterval(intervalId);
        }
      }, 1000);
    }
    return { chats, setRoomId, addChat, startChat, removeChat };
  },
  { persist: true }
);

// const intervalId = setInterval(() => {
//   chats.value.push({
//     id: id++,
//     name: "시스템",
//     text: counter,
//   });
//   counter--;
//   if (counter === 0) {
//     // 카운트다운이 끝나면, clearInterval을 사용하여 반복 중지
//     clearInterval(intervalId);
//     // 카운트다운 종료 메시지 추가
//     chats.value.push({
//       id: id++,
//       name: "시스템",
//       text: "게임 시작!",
//     });
//   }
// }, 1000);
// }
