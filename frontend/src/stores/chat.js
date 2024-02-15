import { ref, computed, onMounted } from "vue";
import { defineStore } from "pinia";

export const useChatStore = defineStore(
  "chat",
  () => {
    let id = 0;
    const roomId = ref("");
    const chats = ref([]);
    const setRoomId = (newRoomId) => {
      roomId.value = newRoomId;
    };

    const addChat = function (inputChat) {
      chats.value.push({
        roomId: inputChat.roomId,
        name: inputChat.userNickname,
        text: inputChat.content,
      });
    };

    const clearChats = () => {
      // 초기 메시지 설정이 필요하다면 여기서 추가
      chats.value = [{}];
    };

    return { chats, setRoomId, addChat, clearChats };
  },
  { persist: true }
);
