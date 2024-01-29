<template>
  <div>zzzzz</div>
  <button @click="goBack">돌아가기</button>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { ref } from "vue";

const emit = defineEmits(["close"]);

const goBack = () => {
  emit("close");
};
</script>

<style lang="scss" scoped></style>
