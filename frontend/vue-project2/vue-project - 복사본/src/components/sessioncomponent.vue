<template>
  <div>
    <div id="session-header">
      <h1 v-show="false" id="session-title">{{ mySessionId }}</h1>
      <input
        class="btn btn-large btn-danger"
        type="button"
        id="buttonLeaveSession"
        @click="leaveSession"
        value="Leave session"
      />
    </div>
    <div class="session">
      <div id="main-video" class="col-3">
        <user-video :stream-manager="mainStreamManager" />
      </div>
      <div id="video-container" class="col-3">
        <user-video
          :stream-manager="publisher"
          @click.native="updateMainVideoStreamManager(publisher)"
        />
        <user-video
          v-for="sub in subscribers"
          :key="sub.stream.connection.connectionId"
          :stream-manager="sub"
          @click.native="updateMainVideoStreamManager(sub)"
        />
      </div>
      <div class="col-3" id="chat-component">
        <chatcomponent room-id="001" :user-name="mySessionId" />
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped></style>
