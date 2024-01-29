import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import { useOpenViduStore } from "./openvidu";

export const useGameStore = defineStore(
  "game",
  () => {
    const start = ref(false);
    const countdown = ref(5);
    const countdown2 = ref(3);
    const showAd = ref(false);
    const store = useOpenViduStore();
    const subscriber = ref([]);
    const publisher = ref();
    const combinedParticipants = ref([]); // New reference for combined data
    const randomParticipant = ref('');
    const randomIndex = ref('');

    const gameStart = () => {
      start.value = true;
      const interval = setInterval(() => {
        countdown.value--;
        if (countdown.value === 0) {
          clearInterval(interval);
          // 게임 시작 로직

          // 게임 시작 후 추가 카운트다운 시작
          const adInterval = setInterval(() => {
            countdown2.value--;
            if (countdown2.value === 0) {
              clearInterval(adInterval);
              showAd.value = true; // 광고판 표시
              console.log("광고판 표시 상태:", showAd.value); // 상태 확인
              subscriber.value = store.subscribers; // 예시: store에 subscribers 상태가 있다고 가정
              console.log("구독자 목록:", subscriber.value);
              publisher.value = store.publisher;
              console.log("구독자 목록:", publisher.value);
              combinedParticipants.value = [
                ...subscriber.value,
                publisher.value,
              ];
              console.log("참가자 목록:", combinedParticipants.value);
              console.log("참가자 목록:", combinedParticipants.value);
              randomIndex.value = Math.floor(Math.random() * combinedParticipants.value.length);
              randomParticipant.value = combinedParticipants.value[randomIndex.value];
              console.log("랜덤 참가자:", randomParticipant.value, randomIndex.value)
            }
          }, 1000);
        }
      }, 1000);
    };

    const gameout = () => {
      start.value = false;
      countdown.value = 5;
      countdown2.value = 3;
      showAd.value = false;
      randomParticipant.value = false;
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
      randomParticipant,
      randomIndex,
      gameStart,
      gameout,
    };
  },
  { persist: true }
);
