import { ref } from "vue";
import { defineStore } from "pinia";
import { useChatStore } from "@/stores/chat";
import { useGameStore } from "./game";
import { useRoomStore } from "./room";
import { userStore } from "./user";
import sockJs from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";

export const useWebSocketStore = defineStore(
  "Socket",
  () => {
    const store = useChatStore();
    const userstore = userStore();
    const usegamestore = useGameStore();
    const roomstore = useRoomStore();
    const sock = ref(null);
    const stomp = ref(null);
    const roomId = ref(null);
    const roomexplosion = ref(true);
    const API_URL = "";

    console.log(API_URL);
    const initializeWebSocket = (newRoomId) => {
      roomId.value = newRoomId;
      sock.value = new sockJs(`${API_URL}/chat`);
      stomp.value = Stomp.over(sock.value);

      stomp.value.connect({}, (frame) => {
        console.log("Connected: " + frame);
        subscribeToRoom();
        stomp.value.send(
          `/pub/${roomId.value}`,
          JSON.stringify({
            roomId: roomId.value,
            type: "ENTER",
            user_nickname: userstore.myName,
          })
        );
      });
    };

    const subscribeToRoom = () => {
      if (roomId.value && stomp.value) {
        stomp.value.subscribe(`/topic/${roomId.value}`, (message) => {
          const messageObject = JSON.parse(message.body);
          // 메시지 유형에 따른 조건 처리
          switch (messageObject.type) {
            case "CHAT":
              // CHAT 유형의 메시지 처리
              store.addChat(messageObject);
              break;
            case "START":
              // START 유형의 메시지 처리
              store.addChat(messageObject);
              usegamestore.gameStart(roomId.value);
              // 여기에 START 유형에 대한 처리 로직 추가
              break;
            case "ENTER":
              store.addChat(messageObject);
              roomstore.getplayer(roomId.value);
              break;
            case "THEME":
              const words = messageObject.content.split(" ");
              usegamestore.category = words[0];
              usegamestore.answers = words[words.length - 1];
              break;
            case "OWNER":
              roomexplosion.value = messageObject.corrects;
              console.log(roomexplosion.value);
              break;
            case "SUCCESS":
              store.addChat(messageObject);
              break;
            case "END":
              // END 유형의 메시지 처리
              usegamestore.ranks = messageObject.userList;
              console.log(usegamestore.ranks);
              break;
          }
        });
      }
    };

    const sendMessage = (inputChat) => {
      if (!inputChat.trim()) {
        return;
      }
      const messageObject = {
        roomId: roomId.value,
        user_nickname: userstore.myName,
        content: inputChat,
        type: "CHAT",
      };
      stomp.value.send(
        `/pub/${roomId.value}`,
        JSON.stringify(messageObject),
        {}
      );
    };

    const gameStart = () => {
      stomp.value.send(
        `/pub/${roomId.value}`,
        JSON.stringify({
          roomId: roomId.value,
          type: "START",
        }),
        {}
      );
    };

    const getTHEME = () => {
      stomp.value.send(
        `/pub/${roomId.value}`,
        JSON.stringify({
          roomId: roomId.value,
          type: "THEME",
        }),
        {}
      );
    };

    const gameEnd = () => {
      stomp.value.send(
        `/pub/${roomId.value}`,
        JSON.stringify({
          roomId: roomId.value,
          type: "END",
        }),
        {}
      );
    };

    const disconnectWebSocket = () => {
      if (stomp.value && stomp.value.connected) {
        stomp.value.disconnect(() => {
          console.log("Disconnected");
        });
      }
      sock.value = null;
      stomp.value = null;
      roomId.value = null;
    };

    return {
      sock,
      stomp,
      roomId,
      roomexplosion,
      initializeWebSocket,
      subscribeToRoom,
      sendMessage,
      gameStart,
      getTHEME,
      gameEnd,
      disconnectWebSocket,
    };
  },
  { persist: true }
);
