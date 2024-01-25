import axios from "axios";
import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const api = "http://127.0.0.1:8000"; // 게임서버 연결시 호스트 바꿀것!

    const router = useRouter();

    const Roomlist = ref(null);

    // 룸의 갯수 정보 requset
    const getRoomlist = function () {
      axios({
        method: "get",
        url: `${api}/room/`,
        // headers: {
        //   Authorization: `Token ${token}`
        // }
      })
        .then((res) => {
          console.log(res.data, "n 개 조회 됨");
          Roomlist.value = res.data;
          router.push({ name: "waitRoom" });
        })
        .catch((err) => {
          if (err.response && err.response.status === 404) {
            console.log("게시글이 없습니다.");
          } else {
            console.log(err, "게시글 n개 Read request 오류");
          }
        });
    };
    return { getRoomlist };
  },
  { persist: true }
);
