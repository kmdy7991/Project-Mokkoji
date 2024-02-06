import { ref, computed, onMounted, watchEffect } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const userStore = defineStore(
  "user",
  () => {
    const API_URL = "https://192.168.31.58:443";
    const myName = ref("");
    const Auth = ref(false);
    const createuser = () => {
      axios({
        method: "get",
        url: `${API_URL}/api/v1/users/register/${myName.value}`,
      })
        .then((res) => {
          console.log(res.data);
          if (Auth.value === true) {
          }
        })
        .catch((err) => {
          console.log(err);
        });
    };
    return { myName, Auth, createuser };
  },
  { persist: true }
);
