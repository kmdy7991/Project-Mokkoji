import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import axios from "axios";

export const userStore = defineStore(
  "user",
  () => {
    const API_URL = import.meta.env.VITE_APP_API_URL; //http://192.168.31.42:8080 대영이 로컬 호스트 주소
    const myName = ref("");
    const router = useRouter();
    const Auth = ref(false);
    const double = ref(false);
    const createuser = () => {
      // console.log(API_URL);
      axios({
        method: "get",
        url: `${API_URL}/api/guest/register/${myName.value}`,
      })
        .then((res) => {
          double.value = false;
          if (Auth.value === true) {
            router.replace({ name: "Auth" });
          } else {
            router.replace({ name: "waitRoom" });
          }
        })
        .catch((err) => {
          console.log(err);
          // if (err.response.status === 409) {
          double.value = true;
          // }
        });
    };
    return { myName, Auth, double, createuser };
  },
  { persist: true }
);
