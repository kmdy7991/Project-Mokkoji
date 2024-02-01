import { ref, computed, onMounted, watchEffect } from "vue";
import { defineStore } from "pinia";

export const userStore = defineStore(
  "user",
  () => {
    const myName = ref("");
    const Auth = ref(false);
    const setMyName = (name) => {
      myName.value = name;
      console.log(myName.value);
    };
    watchEffect(() => {
      // console.log("Name changed:", myName.value);
    });
    return { myName, setMyName, Auth };
  },
  { persist: true }
);
