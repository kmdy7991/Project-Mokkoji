<template>
    <div class="buttons">
        <button class="button" @click="goBack()">
            <img src="@/assets/backdo.png" alt="backdo.png" />
        </button>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import { ref } from 'vue';

const emit = defineEmits(['close']);

const goBack = () => {
    emit('close');
};
</script>

<style scoped>
.buttons {
    display: flex;
    justify-content: flex-end;
}

.button {
    background-color: #ff2001;
    border: #036be7;
    width: 50px; /* 버튼의 너비 */
    height: 50px; /* 버튼의 높이를 너비와 같게 설정하여 정사각형 만들기 */
    line-height: 50px; /* 텍스트를 버튼 중앙에 위치시키기 */
    text-align: center; /* 텍스트 중앙 정렬 */
    margin-top: 1%;
    margin-right: 1%;
    border-radius: 10%; /* 버튼 모서리를 약간 둥글게 */
    transition: background-color 0.3s ease; /* 부드러운 색상 전환 효과 */
    /* 기본 버튼 스타일링 */
    color: white; /* 기본 글자색 */
    box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.button img {
    width: 100%; /* 이미지 너비를 버튼 너비에 맞춤 */
    height: 100%; /* 이미지 높이를 버튼 높이에 맞춤 */
    object-fit: contain; /* 이미지 비율을 유지하면서 버튼 내에 맞춤 */
}
</style>
