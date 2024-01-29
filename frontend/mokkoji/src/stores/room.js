import { defineStore } from "pinia";
import { ref } from "vue";
import { userStore } from "./user";
import axios from "axios";

export const roomStore = defineStore("room", () => {
  const API_URL = "";
  const store = userStore();
  const roomList = ref(null);

  const getRooms = function () {
    axios({
      method: "get",
      url: `${API_URL}/`,
    })
      .then((res) => {
        console.log(res.data, "게임방 n 개 조회 됨");
        roomList.value = res.data;
      })
      .catch((err) => {
        if (err.response && err.response.status === 404) {
          console.log("방이 없습니다.");
        } else {
          console.log(err, "방 n개 Read request 오류");
        }
      });
  };
  const createroom = function (payload) {
    const { room_name, room_password, is_private, game_type } = payload;

    axios({
      method: "post",
      url: `${API_URL}/room/create`,
      data: {
        room_name: payload.room_name,
        room_password: payload.room_password,
        is_private: payload.is_private,
        is_explosion: false,
        is_actice: false,
        game_type: payload.game_type,
        owner: store.myName,
      },
    })
      .then((res) => {
        console.log(res.data, "방 생성됨");
      })
      .catch((err) => {
        console.log(err, "방 create request 오류");
      });
  };
});
