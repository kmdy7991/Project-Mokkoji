<template>
  <div class="container">
		<div class="mypicture">
			<mypicture />
		</div>
		<div class="myinfo">
			<myinfomation/>
		</div>
  </div>
</template>

<script setup>
import mypicture from './mypicture.vue';
import myinfomation from './myinfomation.vue'
</script>

<style scoped>
.container {
  display: flex; /* Flexbox를 활성화 */
	margin: 1%;
	width:100%
}
.mypicture {
	width: 250px;
	margin-right: 5%;
}

.myinfo {
	width : 720px;
}

</style>