<template>
  <div>
    <div class="other">
      <otherliveplayer
        v-for="player in paginatedplayers"
        :key="player.id || `empty-${player.index}`"
        :player="player"
      />
    </div>
    <div class="buttoncontainer">
      <div class="page">
        <button
          @click="prevPage"
          :class="{ 'disabled-button': pageNumber === 0 }"
        >
          ◀
        </button>
        <button
          @click="nextPage"
          :class="{ 'disabled-button': pageNumber >= maxPage }"
        >
          ▶
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import otherliveplayer from "./otherliveplayer.vue";

const liveplayer = ref([]);
const perplayer = 8; // 한 페이지당 방 갯수
const pageNumber = ref(0);

const paginatedplayers = computed(() => {
  const start = pageNumber.value * perplayer;
  const end = start + perplayer;
  let pageplayer = liveplayer.value.slice(start, end);

  // liveplayer 배열의 길이가 10 미만일 경우, 빈 객체를 추가하여 길이를 10으로 만듭니다.
  while (pageplayer.length < perplayer) {
    pageplayer.push({ id: null, index: pageplayer.length + 1 });
  }

  return pageplayer;
});

const maxPage = computed(() => {
  return Math.ceil(liveplayer.length / perplayer) - 1;
});

const nextPage = () => {
  if (pageNumber.value < maxPage.value) pageNumber.value++;
};

const prevPage = () => {
  if (pageNumber.value > 0) pageNumber.value--;
};
</script>

<style scoped>
.other {
  margin: 0% 5%;
  padding: 3% 0%;
  background-color: #052b68;
  border-radius: 10px;
}

.buttoncontainer {
  display: flex;
  justify-content: center;
  width: 100%;
}

.page {
  display: flex;
  justify-content: center;
  background-color: #052b68;
  width: 50%;
  height: 40px;
  border-bottom-right-radius: 10px;
  border-bottom-left-radius: 10px;
}

.page > button {
  height: 75%;
  width: 40%;
  background-color: #00acfc;
  border-color: #054ca3;
  border-radius: 3px;
  margin: 0% 3%;
  transition: background-color 0.3s ease;
  color: #4ddffb;
  border: none;
}

.page > button:hover {
  background-color: #0197dd;
  cursor: click;
}

.page > button.disabled-button {
  background-color: #19adfc; /* 회색 배경색으로 변경 */
  border: none; /* 회색 테두리로 변경 */
  cursor: not-allowed; /* 커서를 not-allowed로 변경하여 클릭 비활성화 */
  color: #888; /* 글자색을 회색으로 변경 */
}

.page > button.disabled-button:hover {
  background-color: #19adfc; /* hover 시 배경색도 그대로 유지 */
  cursor: not-allowed; /* hover 시 커서도 그대로 유지 */
}
</style>
