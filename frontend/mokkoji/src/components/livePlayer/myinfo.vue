<template>
  <div class="myinfos">
		<div class="myinfo">
			<div class="center">
				임시닉네임3579
			</div>
		</div>
		<div class="myinfo">
			<div class="center">
				아이언
			</div>
		</div>
		<div class="live-player">
			<div class="livecenter">
				실시간 접속자
			</div>
		</div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.myinfos{
	display: flex;
	justify-content: center;
	flex-direction: column;
	align-items: center;
}

.myinfo{
	width: 80%;
	height: 30px;
	display: flex;
	justify-content: center;
	background-color: #015CB3;
	margin-top: 2%;
	border-radius: 10px;
	color: white;
	font-family: "DOSMyungjo";
}

.center{
	margin-top: 2%;
}
.live-player{
	width: 80%;
	height: 40px;
	margin-top: 5%;
	display: flex;
	justify-content: center;
	color: white;
	font-family: "DOSMyungjo";
	background-color: #052B68;
	border-radius: 10px;
	margin-bottom: 1%;
}

.livecenter{
	margin-top: 3%;
	font-size: 20px;
}
</style>