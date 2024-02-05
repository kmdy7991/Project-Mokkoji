import { ref, computed, onMounted, watchEffect } from "vue";
import { defineStore } from "pinia";

export const userStore = defineStore(
  "user",
  () => {
    const myName = ref("");
    const Auth = ref(false);
    return { myName, Auth };
  },
  { persist: true }
);
