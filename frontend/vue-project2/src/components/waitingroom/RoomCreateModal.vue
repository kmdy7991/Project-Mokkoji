<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <div class="game-info">
        <div class="game-buttons">
          <button>몸으로 말해요</button>
          <button>뮤직큐</button>
        </div>
        <div class="ro"></div>
        <button @click="closeModal">취소</button>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    show: Boolean,
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
  },
};
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
  background-color: rgba(0, 0, 0, 0.2); /* 반투명 배경 */
  z-index: 1000; /* 다른 요소들보다 상위에 위치 */
}

.modal-content {
  background-color: #0be2ff;
  text-align: center;
  width: 80%;
  height: 60%;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1001; /* 필요에 따라 너비와 높이 설정 */
  display: flex; /* Make this a flex container */
  justify-content: center; /* Center horizontally */
  align-items: start; /* Center vertically */
}

.game-info {
  width: 80%;
}
.game-buttons {
  margin-top: 5%;
  margin-bottom: 5%;
  width: 100%;
  height: 200px;
  /* Adjust the margin as needed */
  display: flex; /* Set as Flex container */
  justify-content: space-around; /* Even space around each item */
  align-items: center;
}

.game-buttons > button {
  width: 30%;
  height: 100%;
  background-color: #12deff;
  font-size: 56px;
  font-family: "LABdigital";
  border-radius: 5%;
  border: 0;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}
</style>
