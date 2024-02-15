import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import axios from "axios";

export const userStore = defineStore(
  "user",
  () => {
    const myName = ref("");
    const router = useRouter();
    const Auth = ref(false);
    const Autherr = ref(false);
    const double = ref(false);
    const createuser = () => {
      axios({
        method: "get",
        url: `/api/guest/register/${myName.value}`,
      })
        .then((res) => {
          double.value = false;
          router.replace({ name: "waitRoom" });
        })
        .catch((err) => {
          double.value = true;
        });
    };
    const Authlogin = (payload) => {
      const { Authemail } = payload;
      const jsonData = {
        email: payload.Authemail,
      };
      console.log(typeof payload.Authemail);
      axios({
        method: "post",
        url: `/api/login`,
        data: jsonData,
      })
        .then((res) => {
          Autherr.value = false;
          Auth.value = true;
          router.push({ name: "Auth" });
        })
        .catch((err) => {
          Autherr.value = true;
        });
    };

    return { myName, Auth, Autherr, double, createuser, Authlogin };
  },
  { persist: true }
);
