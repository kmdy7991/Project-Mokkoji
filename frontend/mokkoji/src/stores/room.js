import { defineStore } from "pinia";
import { ref } from "vue";
import { userStore } from "./user";
import axios from "axios";
import router from "@/router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const API_URL = "http://127.0.0.1:8080"; // 로컬단 서버로 올릴시 수정할것!

    const store = userStore;

    const Roomlist = ref([]);

    const getRoomlist = async () => {
      axios({
        method: "get",
        url: `${API_URL}/api/room`,
      })
        .then((res) => {
          console.log(res.data, "게임방 n 개 조회 됨");
          Roomlist.value = res.data;
          router.push({ name: "waitRoom" });
        })
        .catch((err) => {
          if (err.response && err.response.status === 404) {
            console.log("방이 없습니다.");
          } else {
            console.log(err, "방 n개 Read request 오류");
          }
        });
    };
    watchEffect(() => {
      getRoomlist();
    });

    const roomData = computed(() =>
      Roomlist.value.map((room) => ({
        room_id: room.room_id,
        room_name: room.room_name,
        room_password: room.room_password,
        user_count: room.user_count,
        is_private: room.is_private,
        is_explosion: room.is_explosion,
        is_active: room.is_active,
        game_type: room.game_type,
        owner: room.owner,
      }))
    );

    const createRoom = function (payload) {
      const { room_name, room_password, is_private, game_type } = payload;

      const jsonData = JSON.stringify({
        room_name: room_name,
        room_password: room_password,
        is_private: is_private,
        is_explosion: false,
        is_active: false,
        game_type: game_type,
        owner: store.myName,
      });

      axios({
        method: "post",
        url: `${API_URL}/room/create`,
        data: jsonData,
      })
        .then((res) => {
          console.log(res.data, "게시글 생성됨");
          const createdRoomId = res.data.room_id;
          router.push({ path: `/TalkBody/${createdRoomId}` });
        })
        .catch((err) => {
          console.log(err, "방 create request 오류");
        });
    };

    const entranceRoom = function () {
      axios({
        method: "get",
        url: `${API_URL}/enter/{room_id}`,
      })
        .then((res) => {
          console.log(res.data, "게임방 입장");
          router.push({ path: `/TalkBody/${RoomId}` }); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          if (err.response.stauts === 403) {
            console.log(`방에 인원수가 다 찼습니다!`);
          }
        });
    };

    return { Roomlist, getRoomlist, createRoom, entranceRoom };
  },
  { persist: true }
);
