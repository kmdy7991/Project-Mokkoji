import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import axios from "axios";

export const userStore = defineStore(
  "user",
  () => {
    const API_URL = "https://192.168.31.58:443";
    const myName = ref("");
    const router = useRouter();
    const Auth = ref(false);
    const double = ref(false);
    const createuser = () => {
      axios({
        method: "get",
        url: `${API_URL}/api/v1/users/register/${myName.value}`,
      })
        .then((res) => {
          console.log(res.data);
          double.value = false;
          if (Auth.value === true) {
            router.replace({ name: "Auth" });
          } else {
            router.replace({ name: "waitRoom" });
          }
        })
        .catch((err) => {
          console.log(myName);
          console.log(err);
          if (err.response.status === 409) {
            double.value = true;
          }
        });
    };
    return { myName, Auth, double, createuser };
  },
  { persist: true }
);
