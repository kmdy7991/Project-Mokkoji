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
    const myName = userstore.myName;
    const API_URL = import.meta.env.VITE_APP_API_URL;

    const initializeWebSocket = (newRoomId) => {
      roomId.value = newRoomId;
      sock.value = new sockJs(`${API_URL}/chat`); // 로컬단 서버로 올릴시 수정할것! 58 예진님 42 대영
      stomp.value = Stomp.over(sock.value);

      stomp.value.connect({}, (frame) => {
        console.log("Connected: " + frame);
        subscribeToRoom();
        stomp.value.send(
          `/pub/${roomId.value}`,
          JSON.stringify({
            roomId: roomId.value,
            type: "ENTER",
            user_nickname: myName,
          })
        );
      });
    };

    const subscribeToRoom = () => {
      if (roomId.value && stomp.value) {
        stomp.value.subscribe(`/topic/${roomId.value}`, (message) => {
          // console.log("구독받은 메세지 = " + message.body);
          const messageObject = JSON.parse(message.body);
          // store.addChat(messageObject);
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
              // console.log(messageObject);
              const words = messageObject.content.split(" ");
              usegamestore.category = words[0];
              usegamestore.answers = words[words.length - 1];
              break;
            case "OWNER":
              // console.log(messageObject);
              roomexplosion.value = messageObject.corrects;
              console.log(roomexplosion.value);
              break;
            case "SUCCESS":
              store.addChat(messageObject);
              break;
            // case "TURN":
            //   // TURN 유형의 메시지 처리
            //   console.log("차례가 변경되었습니다.");
            //   // 여기에 TURN 유형에 대한 처리 로직 추가
            //   break;
            // case "END":
            //   // END 유형의 메시지 처리
            //   console.log("채팅이 종료되었습니다.");
            //   // 여기에 END 유형에 대한 처리 로직 추가
            //   break;
            //   default:
            //     // 알 수 없는 메시지 유형 처리
            //     console.log(
            //       "알 수 없는 메시지 유형입니다: " + messageObject.type
            // );
          }
        });
      }
    };

    const sendMessage = (inputChat) => {
      if (!inputChat.trim()) {
        return;
      }
      console.log(myName);
      const messageObject = {
        roomId: roomId.value,
        user_nickname: myName,
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
      disconnectWebSocket,
    };
  },
  { persist: true }
);
