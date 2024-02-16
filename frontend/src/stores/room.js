import { defineStore } from "pinia";
import { ref, watch } from "vue";
import { userStore } from "./user";
import { useOpenViduStore } from "./openvidu";
import axios from "axios";
import { useRouter } from "vue-router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const store = userStore();
    const router = useRouter();
    const vidustore = useOpenViduStore();
    const Roomlist = ref([]);
    const owner = ref("");
    const players = ref([]);
    const roomId = ref();
    const roomname = ref("");
    const fullroom = ref(false);
    const refuesentry = ref(false);
    const prevRoomlistLength = ref(Roomlist.value.length);
    const worngPassWord = ref(false);

    const getRoomlist = () => {
      axios({
        method: "get",
        url: `/api/room`,
      })
        .then((res) => {
          Roomlist.value = res.data.filter(
            (room) => !room._active && !room._explosion
          );
        })
        .catch((err) => {
          Roomlist.value = [];
        });
    };

    watch(Roomlist, (newValue, oldValue) => {
      // 룸 변화 감지시.
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
        owner: store.myName,
        _private: payload._private,
        _explosion: false,
        _active: false,
        game_type: payload.game_type,
      };
      axios({
        method: "post",
        url: `/api/room/create`,
        data: jsonData,
      })
        .then((res) => {
          roomname.value = res.data.room_name;
          owner.value = res.data.owner;
          roomId.value = res.data.room_id;
          const createdRoomId = String(res.data.room_id);
          const payload = {
            roomId: createdRoomId,
          };
          vidustore.joinSession(payload);
          router.replace({
            path: `/TalkBody/${createdRoomId}`,
            query: {
              roomName: roomname.value,
            },
          });
        })
        .catch((err) => {});
    };

    const entranceRoom = function (roomIdNumber) {
      roomId.value = roomIdNumber;
      axios({
        method: "get",
        url: `/api/room/enter/${roomIdNumber}/${store.myName}`,
      })
        .then((res) => {
          players.value = res.data.participants;
          roomname.value = res.data.room.room_name;
          roomIdNumber = String(roomIdNumber);
          const payload = {
            roomId: roomIdNumber,
          };
          owner.value = res.data.room.owner;
          vidustore.joinSession(payload);
          router.replace({
            path: `/TalkBody/${roomIdNumber}`,
            query: {
              roomName: roomname.value,
            },
          }); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          if (err.response.status === 403) {
            fullroom.value = true;
          } else {
            refuesentry.value = true;
          }
        });
    };

    const entranceSecretRoom = function (payload) {
      const { roomId, secret } = payload;
      const data = { password: secret };
      axios({
        method: "post",
        url: `/api/room/${roomId}/checkpwd`,
        data: data,
      })
        .then((res) => {
          entranceRoom(roomId); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          if (err.response.status === 401) {
            worngPassWord.value = true;
          }
        });
    };

    const getplayer = function (roomId) {
      const myroomId = Number(roomId);
      axios({
        method: "get",
        url: `/api/room/${myroomId}/participants`,
      })
        .then((res) => {
          players.value = res.data;
        })
        .catch((err) => {});
    };

    const exitRoom = function (payload) {
      const { roomId, nickname } = payload;
      axios({
        method: "delete",
        url: `/api/room/delete/${payload.roomId}/${payload.nickname}`,
      })
        .then((res) => {
          owner.value = "";
        })
        .catch((error) => {});
    };

    return {
      Roomlist,
      worngPassWord,
      owner,
      players,
      fullroom,
      refuesentry,
      getRoomlist,
      createRoom,
      entranceRoom,
      exitRoom,
      getplayer,
      entranceSecretRoom,
    };
  },
  { persist: true }
);
