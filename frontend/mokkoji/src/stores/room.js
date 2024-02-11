import { defineStore } from "pinia";
import { ref, watch, computed } from "vue";
import { userStore } from "./user";
import { useOpenViduStore } from "./openvidu";
import axios from "axios";
import { useRouter } from "vue-router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const API_URL = import.meta.env.VITE_APP_API_URL; // 로컬단 서버로 올릴시 수정할것! http://192.168.31.58:8080 예진님 코드
    const store = userStore();
    const router = useRouter();
    const name = store.myName;
    const vidustore = useOpenViduStore();
    const Roomlist = ref([]);
    const owner = ref("");
    const players = ref([]);
    const roomId = ref();
    const roomname = ref("");
    const prevRoomlistLength = ref(Roomlist.value.length);
    const preplayers = ref(players.value.length);
    const worngPassWord = ref(false);

    const getRoomlist = () => {
      axios({
        method: "get",
        url: `${API_URL}/api/room`,
      })
        .then((res) => {
          // console.log(res.data, "게임방 n 개 조회 됨"); //확인 완료. getmapping 성공
          Roomlist.value = res.data.filter(
            (room) => !room._active && !room._explosion
          );
        })
        .catch((err) => {
          if (err.response && err.response.status === 404) {
            console.log("방이 없습니다.");
          } else {
            // console.log(err, "방 n개 Read request 오류");
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
        owner: store.myName,
        _private: payload._private,
        _explosion: false,
        _active: false,
        game_type: payload.game_type,
      };
      axios({
        method: "post",
        url: `${API_URL}/api/room/create`,
        data: jsonData,
      })
        .then((res) => {
          // console.log(res.data, "방 생성됨");
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
        .catch((err) => {
          // console.log(err, "방 create request 오류");
        });
    };

    const entranceRoom = function (roomIdNumber) {
      roomId.value = roomIdNumber;
      axios({
        method: "get",
        url: `${API_URL}/api/room/enter/${roomIdNumber}/${name}`,
      })
        .then((res) => {
          console.log(res.data, "게임방 입장"); //테스트 완료
          // console.log(res.data.participants);
          players.value = res.data.participants;
          roomname.value = res.data.room.room_name;
          roomIdNumber = String(roomIdNumber);
          const payload = {
            roomId: roomIdNumber,
          };
          owner.value = res.data.room.owner;
          // console.log(res.data.room.owner);
          console.log(owner.value);
          vidustore.joinSession(payload);
          router.replace({
            path: `/TalkBody/${roomIdNumber}`,
            query: {
              roomName: roomname.value,
            },
          }); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          // console.log(err);
        });
    };

    const entranceSecretRoom = function (payload) {
      const { roomId, secret } = payload;
      // const data = JSON.stringify({ password: secret });
      const data = { password: secret };
      console.log(typeof roomId);
      console.log(typeof data);
      axios({
        method: "post",
        url: `${API_URL}/api/room/${roomId}/checkpwd`,
        data: data,
      })
        .then((res) => {
          // console.log(res.data, "게임방 입장");
          entranceRoom(roomId); // entranceRoom  함수에서 받아올 예정
        })
        .catch((err) => {
          if (err.response.status === 401) {
            console.log(`비밀번호 틀림!`);
            worngPassWord.value = true;
          }
        });
    };

    const getplayer = function (roomId) {
      const myroomId = Number(roomId);
      axios({
        method: "get",
        url: `${API_URL}/api/room/${myroomId}/participants`,
      })
        .then((res) => {
          // console.log(res.data, "게임방 입장");
          players.value = res.data;
        })
        .catch((err) => {
          console.log(`최신화 실패`);
        });
    };

    const exitRoom = function (payload) {
      const { roomId, nickname } = payload;
      console.log(typeof payload.roomId);
      console.log(payload.nickname);
      axios({
        method: "delete",
        url: `${API_URL}/api/room/delete/${payload.roomId}/${payload.nickname}`,
      })
        .then((res) => {
          // console.log(res);
          owner.value = "";
          // console.log(players.value);
          // 성공적인 응답 처리
          // console.log(res);
          // console.log(`성공!`); 성공 완료
        })
        .catch((error) => {
          // 에러 처리
          // console.log(error);
          console.log(`실패!`);
        });
    };

    return {
      Roomlist,
      worngPassWord,
      owner,
      players,
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
