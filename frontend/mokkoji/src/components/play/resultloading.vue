<template>
  <div class="result">
		<div class="roading">
			게임결과 대기중{{ loadingDots }}
		</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
const loadingDots = ref('.');

onMounted(() => {
  let count = 0;
  const interval = setInterval(() => {
    count = (count + 1) % 4;
    loadingDots.value = '.'.repeat(count || 1);
  }, 500);

  onUnmounted(() => {
    clearInterval(interval);
  });
});

</script>

<style scoped>

.result {
	display: flex;
  justify-content: center;
  align-items: center;
	background-color: none;
	position: fixed; /* 고정 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비 */
  height: 100%;
	z-index: 1000;
}

.roading {
	display: flex;
  justify-content: center;
  align-items: center;
	width: 50%;
	height: 50%;
	background-color: rgba(255, 255, 255, 0.5);
	font-size: 48px;
	font-family: "DOSMyungjo";
}
</style>