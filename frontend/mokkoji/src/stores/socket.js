import { ref } from "vue";
import { defineStore } from "pinia";
import { useChatStore } from "@/stores/chat";
import { userStore } from "./user";
import sockJs from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";

export const useWebSocketStore = defineStore('Socket' , () => {
  const store = useChatStore();
  const userstore = userStore();
  const sock = ref(null); 
  const stomp = ref(null);
  const roomId = ref(null);
  const myName = userstore.myName;
  

  const initializeWebSocket = (newRoomId) => {
    roomId.value = newRoomId;
    sock.value = new sockJs("https://localhost:8080/chat");
    stomp.value = Stomp.over(sock.value);
    stomp.value.connect({}, frame => {
      console.log("Connected: " + frame);
      subscribeToRoom();
    });
  };

  const subscribeToRoom = () => {
    if (roomId.value && stomp.value) {
      stomp.value.subscribe(`/topic/${roomId.value}`, message => {
        console.log("구독받은 메세지 = " + message.body);
        const messageObject = JSON.parse(message.body);
        store.addChat(messageObject);
      });
    }
  };
  
  const sendMessage = (inputChat) => {
    if (!inputChat.trim()) {
      return;
    }
    console.log(myName)
    const messageObject = {
      sessionId: roomId.value,
      userName: myName,
      content: inputChat,
    };
    stomp.value.send(`/pub/${roomId.value}`, JSON.stringify(messageObject), {});
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

  return { sock, stomp, roomId, myName ,initializeWebSocket, subscribeToRoom, sendMessage, disconnectWebSocket };
},{persist: true});  
  

