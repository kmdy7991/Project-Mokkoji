<template>
	<div>
  	<div class="my-picture">
  	</div>
		<button class="picture-change">
				프로필 사진 클릭
		</button>
	</div>
</template>

<script setup>

</script>

<style scoped>
.my-picture{
	width: 100%;
	height: 300px;
	margin-bottom: 10%;
	background-color: #12DEFF;
	border-radius: 10px;
	box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}
.picture-change{
	width: 300px;
	height: 50px;
	border-radius: 5px;
	background-color: #00ACFC;
	border: 0px;
	transition: background-color 0.3s ease;
	color: white;
	font-family: "DOSMyungjo";
	font-size: 20px;
	box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.picture-change:hover {
  background-color: #3498db; /* 예: 파란색 배경 */
  color: #fff; /* 예: 흰색 글자 */
  cursor: click;
}

</style>