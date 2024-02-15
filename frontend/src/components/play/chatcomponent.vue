<template>
  <div>
    <div class="room-info">
      <div class="room-number">
        <p>{{ roomId }}</p>
      </div>
      <div class="room-name">
        <p>{{ roomName }}</p>
      </div>
    </div>
    <div class="chatting-area" ref="chattingAreaRef">
      <div class="chat" v-for="chat in store.chats" :key="chat.id" :chat="chat">
        <p v-if="chat.roomId === roomIdRef">
          {{ chat.name }} : {{ chat.text }}
        </p>
      </div>
    </div>
    <form @submit.prevent="sendMessage()">
      <input
        type="text"
        v-model="inputChat"
        class="chat-area"
        placeholder="채팅을 입력해주세요"
      />
    </form>
  </div>
</template>
<script setup>
import { useChatStore } from "@/stores/chat";
import { useWebSocketStore } from "@/stores/socket";
import { ref, onMounted, nextTick, watch } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const roomId = route.params.id;
const roomName = ref("");
const chattingAreaRef = ref(null);
const store = useChatStore();
const webSocketStore = useWebSocketStore();
const inputChat = ref("");
const props = defineProps({
  roomId: String,
});
const roomIdRef = ref(props.roomId);
// 서버로 메시지 보내는 함

onMounted(() => {
  roomName.value = route.query.roomName;
  store.setRoomId(roomId);
  store.clearChats(); // 채팅방에 들어올 때 이전 채팅 내용을 초기화
  webSocketStore.initializeWebSocket(roomId);
});

watch(
  () => store.chats.length,
  () => {
    nextTick(() => {
      if (chattingAreaRef.value) {
        chattingAreaRef.value.scrollTop = chattingAreaRef.value.scrollHeight;
      }
    });
  }
);

function sendMessage() {
  webSocketStore.sendMessage(inputChat.value);
  inputChat.value = "";
  inputChat.value = null;
}
</script>

<style scoped>
.room-info {
  display: flex; /* Flexbox 레이아웃을 사용 */
  align-items: center; /* 요소들을 세로 중앙에 정렬 */
  width: 98%;
  height: 30px;
  margin-bottom: 1%;
  padding: 1%;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background-color: #0877d1;
}

.chat {
  margin: 0%;
  color: white;
  margin-left: 2%;
  margin-bottom: 2%;
  font-size: 24px;
}

.chat > p {
  margin: 1%;
}
.room-number {
  margin-left: 5%;
  padding-top: 1%;
  height: 90%;
  width: 30%;
  background-color: #24b9d1;
  border-radius: 10px;
  display: flex; /* Flexbox 레이아웃을 사용 */
  align-items: center; /* 텍스트를 세로 중앙에 정렬 */
  justify-content: center; /* 텍스트를 가로 중앙에 정렬 */
}

.room-name {
  margin-left: 5%; /* 여백 조정 */
  margin-right: 5%;
  width: auto; /* 필요한 만큼의 너비 사용 */
}

.room-name > p {
  margin: 0%;
  font-family: "LABdigital";
  font-size: 24px;
  color: white;
}

.room-number > p {
  margin: 0%;
  text-align: center;
  color: white;
  font-family: "LABdigital";
  font-size: 24px;
}
.chatting-area {
  width: 100%;
  height: 750px;
  background-color: #0877d1;
  overflow-y: auto;
}

.chatting-area::-webkit-scrollbar-track {
  background-color: #054d9d; /* 여기에 원하는 배경 색상 코드를 입력하세요 */
}

/* 스크롤바 썸 (스크롤바 자체) 스타일링 */
.chatting-area::-webkit-scrollbar-thumb {
  background-color: #1e9ae6; /* 여기에 원하는 스크롤바 색상 코드를 입력하세요 */
}

.chat-area {
  width: 96.5%;
  height: 30px;
  margin-top: 1%;
  padding-left: 3%;
  background-color: #0877d1;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  border: none;
  color: white;
}

.chat-area::placeholder {
  margin-left: 3%;
  color: white; /* 플레이스홀더 텍스트 색상을 흰색으로 설정 */
  opacity: 1; /* 브라우저가 기본적으로 적용하는 투명도를 제거 */
}
</style>
