import { defineStore } from "pinia";
import { ref, watch, computed } from "vue";
import { userStore } from "./user";
import { useOpenViduStore } from "./openvidu";
import axios from "axios";
import router from "@/router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const API_URL = "https://192.168.31.58:443"; // 로컬단 서버로 올릴시 수정할것!
    const store = userStore();
    const vidustore = useOpenViduStore();
    const Roomlist = ref([]);
    const prevRoomlistLength = ref(Roomlist.value.length);

    const getRoomlist = () => {
      axios({
        method: "get",
        url: `${API_URL}/api/room`,
      })
        .then((res) => {
          //console.log(res.data, "게임방 n 개 조회 됨"); //확인 완료. getmapping 성공
          Roomlist.value = res.data.filter(
            (room) => !room._active && !room._explosion
          );
        })
        .catch((err) => {
          if (err.response && err.response.status === 404) {
            console.log("방이 없습니다.");
          } else {
            console.log(err, "방 n개 Read request 오류");
            Roomlist.value = [];
          }
        });
    };

    watch(Roomlist, (newValue, oldValue) => {
      // This function will be called whenever Roomlist changes.
      if (newValue.length !== prevRoomlistLength.value) {
        getRoomlist();
        prevRoomlistLength.value = newValue.length;
      }
    });
    // 테스트 코드

    const createRoom = function (payload) {
      const { room_name, room_password, _private, game_type } = payload;

      const jsonData = {
        room_name: payload.room_name,
        room_password: payload.room_password,
        _private: payload._private,
        _explosion: false,
        _active: false,
        game_type: payload.game_type,
      };
      console.log(jsonData);
      axios({
        method: "post",
        url: `${API_URL}/api/room/create`,
        data: jsonData,
      })
        .then((res) => {
          console.log(res.data, "방 생성됨");
          const createdRoomId = String(res.data.room_id);
          console.log(createdRoomId);
          const payload = {
            roomId: createdRoomId,
          };
          vidustore.joinSession(payload);
          router.replace({ path: `/TalkBody/${createdRoomId}` });
        })
        .catch((err) => {
          console.log(err, "방 create request 오류");
        });
    };

    const entranceRoom = function (roomIdNumber) {
      console.log(typeof roomIdNumber);
      axios({
        method: "get",
        url: `${API_URL}/api/room/enter/${roomIdNumber}`,
      })
        .then((res) => {
          // console.log(res.data, "게임방 입장"); 테스트 완료
          roomIdNumber = String(roomIdNumber);
          const payload = {
            roomId: roomIdNumber,
          };
          vidustore.joinSession(payload);
          router.replace({ path: `/TalkBody/${roomIdNumber}` }); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          console.log(err);
        });
    };

    const entranceSecretRoom = function (payload) {
      const { roomId, secret } = payload;
      const data = JSON.stringify({ password: secret });
      console.log(typeof roomId);
      console.log(typeof data);
      axios({
        method: "post",
        url: `${API_URL}/api/room/${roomId}/checkpwd`,
        data: data,
      })
        .then((res) => {
          console.log(res.data, "게임방 입장");
          roomIdNumber = String(roomIdNumber);
          const payload = {
            roomId: roomIdNumber,
          };
          vidustore.joinSession(payload);
          router.replace({ path: `/TalkBody/${roomIdNumber}` }); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          console.log(err);
        });
    };

    const exitRoom = function (payload) {
      const { roomId } = payload;
      console.log(typeof payload.roomId);
      axios
        .get(`${API_URL}/api/room/delete/${roomId}`)
        .then((res) => {
          // 성공적인 응답 처리
          // console.log(res);
          // console.log(`성공!`); 성공 완료
        })
        .catch((error) => {
          // 에러 처리
          console.log(error);
          console.log(`실패!`);
        });
    };

    return {
      Roomlist,
      getRoomlist,
      createRoom,
      entranceRoom,
      exitRoom,
      entranceSecretRoom,
    };
  },
  { persist: true }
);
