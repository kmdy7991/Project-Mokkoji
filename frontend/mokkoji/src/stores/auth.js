import { defineStore } from "pinia";
import { ref, watch, computed } from "vue";
import axios from "axios";

export const useAuthStore = defineStore(
  "Auth",
  () => {
    const API_URL = import.meta.env.VITE_APP_API_URL;
    const categorylist = ref([]);
    const getcategory = () => {
      axios({
        method: "get",
        url: `${API_URL}/api/admin/talkbodylist`,
      })
        .then((res) => {
          categorylist.value = res.data.map((item) => item.subject);
          console.log(categorylist.value);
        })
        .catch((err) => {
          console.log(err.data);
        });
    };
    const plussubject = (payload) => {
      const { subject, word } = payload;

      const jsonData = {
        word: payload.word,
      };
      axios({
        method: "post",
        url: `${API_URL}/api/admin/input`,
        data: jsonData,
      })
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err.data);
        });
    };
    return { categorylist, getcategory, plussubject };
  },
  { persist: true }
);
