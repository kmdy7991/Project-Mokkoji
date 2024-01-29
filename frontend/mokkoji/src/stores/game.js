import { ref, computed, onMounted } from "vue";
import { defineStore } from "pinia";

export const gameStore = defineStore('game' , () => {
const start = ref(false);

const setGameStart = () => {
    console.log(start.value)
    start.value = true;
    console.log(start.value)
    if (start.value === true) {
      setTimeout(() => {
        start.value = false;
        console.log('게임 끝~!:', start.value);
      }, 60000); // 1 minute
    }
  };

return { start, setGameStart }
},{persist: true})