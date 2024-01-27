import axios from "axios";
import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";

export const useRoomStore = defineStore(
  "room",
  () => {
    const api = "http://127.0.0.1:8080"; // 게임서버 연결시 호스트 바꿀것!

    const router = useRouter();

    const Roomlist = ref([]);

    // 룸의 갯수 정보 requset
    const getRoomlist = function () {
      axios({
        method: "get",
        url: `${api}/api/room`,
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

        const pData = computed(
          Roomlist.value.map((d) => ({
            roomId : room_id,
          }))
        );
    };
    // Roomlist를 우리가 편한 형식으로 가공.
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
      owner: room.owner
    })
    )
    );
    return { Roomlist, getRoomlist,roomData };
  },
  { persist: true }
);
