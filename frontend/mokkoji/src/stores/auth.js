import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

export const useAuthStore = defineStore(
  "Auth",
  () => {
    const categorylist = ref([]);
    const getcategory = () => {
      axios({
        method: "get",
        url: `/api/admin/talkbodylist`,
      })
        .then((res) => {
          categorylist.value = res.data.map((item) => item.subject);
        })
        .catch((err) => {});
    };
    const plussubject = (payload) => {
      const { subject, word } = payload;
      const elementsArray = payload.word.split(",");
      const jsonData = {
        subject: payload.subject,
        elements: elementsArray,
      };
      axios({
        method: "post",
        url: `/api/admin/input`,
        data: jsonData,
      })
        .then((res) => {})
        .catch((err) => {});
    };
    return { categorylist, getcategory, plussubject };
  },
  { persist: true }
);
