<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <div class="nickname">
        <div class="label-color">닉네임을 입력해주세요</div>
        <div>
          <input
            v-model="username"
            class="form-control"
            type="text"
            placeholder="별명을 입력해주세요"
          />
        </div>
        <div v-if="store.double" class="doublename">
          ! 닉네임이 중복되었습니다!
        </div>
        <div v-if="specialChar" class="doublename">
          ! 별명에는 특수문자를 사용하실수 없습니다!
        </div>
        <div v-if="fuckword" class="doublename">
          ! 부적절한 단어가 사용되었습니다!
        </div>
        <button>확인</button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { defineProps, defineEmits } from "vue";
import { useRoomStore } from "@/stores/room";
import { userStore } from "@/stores/user";
import profanities from "@/assets/profanities";

const roomStore = useRoomStore();
const username = ref("");
const store = userStore();
const specialChar = ref(false);
const fuckword = ref(false);

const isValidUsername = (name) => {
  // 영어, 한글, 숫자만을 허용하는 정규표현식
  const regex = /^[A-Za-z가-힣0-9]+$/;
  return regex.test(name.value);
};

const containsProfanity = (name) => {
  return profanities.some((profanity) => name.value.includes(profanity));
};

const props = defineProps({
  show: Boolean,
});

// Emit 함수 정의
const emit = defineEmits(["close"]);

// Methods
function closeModal() {
  emit("close");
}
</script>

<style scoped>
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed; /* 고정 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비 */
  height: 100%; /* 전체 높이 */
  background-color: rgba(0, 0, 0, 0.1); /* 반투명 배경 */
  z-index: 1000; /* 다른 요소들보다 상위에 위치 */
}
.modal-content {
  background-color: #3f46ea;
  display: flex;
  justify-content: center;
  align-items: start;
  text-align: left;
  width: 900px;
  height: 500px;
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.label-color {
  color: white;
  font-weight: bold;
  font-size: 64px;
  display: block; /* label을 블록 요소로 만들어 줄바꿈을 만듭니다. */
  text-align: left;
  width: 100%;
  margin-bottom: 5px;
  font-family: "DOSMyungjo";
}
</style>
