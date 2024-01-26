<template>
  <div id="main-container" class="container">
    <RouterView />
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import { ref, onMounted } from "vue";
import { useOpenViduStore } from "@/stores/openvidu"; // 필요에 따라 경로 조정
import UserVideo from "./components/UserVideo.vue";
import chatcomponent from "./components/chatcomponent.vue";
// import joinComponent from './components/joinComponent.vue';

const store = useOpenViduStore();
const mySessionId = ref("");
const myUserName = ref("");
const mainStreamManager = ref(null);
const session = ref(false);
let subscribers = null;
const publisher = store.publisher;

// const updateMainVideoStreamManager = (streamManager) => {
//   mainStreamManager.value = streamManager;
// };

// const joinSession = () => {
//   const payload = {
//     mySessionId: mySessionId.value,
//     myUserName: myUserName.value,
//   };
//   store.joinSession(payload);

//   session.value = store.session;
//   console.log("in Vue 95 = " + session.value);
//   console.log(session);
//   console.log("------vue check 1-------");

//   subscribers = store.subscribers;
//   console.log("in Vue 97 = " + subscribers);
//   console.dir(subscribers);
//   console.log("------vue check2------");
// };

const leaveSession = () => {
  store.leaveSession();
  session.value = store.session;
};

onMounted(() => {
  mySessionId.value = store.mySessionId;
  myUserName.value = store.myUserName;
  console.log(store.subscribers);
});
</script>
<!-- <script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "./components/UserVideo.vue";
import chatcomponent from "./components/chatcomponent.vue";
import joinComponent from "./components/joinComponent.vue";

axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL =
  process.env.NODE_ENV === "production" ? "" : "http://192.168.31.42:5000/";
const OPENVIDU_SERVER_URL = "https://13.54.203.42:5253/";
const OPENVIDU_SERVER_SECRET = "SSAFY";

export default {
  name: "App",

  components: {
    UserVideo,
    chatcomponent,
    joinComponent,
  },

  data() {
    return {
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      // mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      // Join form
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
    };
  },

  methods: {
    joinSession() {
      // --- 1) Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- 2) Init a session ---
      this.session = this.OV.initSession();

      // --- 3) Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
        console.log(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {
        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // --- 5) Get your own camera stream with the desired properties ---

            // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
            // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            // Set the main video in the page to display our webcam and store our Publisher
            // this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- 6) Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      // Empty all properties...
      this.session = undefined;
      // this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      // Remove beforeunload listener
      window.removeEventListener("beforeunload", this.leaveSession);
    },

    // updateMainVideoStreamManager(stream) {
    //   if (this.mainStreamManager === stream) return;
    //   this.mainStreamManager = stream;
    // },

    /**
     * --------------------------------------------
     * GETTING A TOKEN FROM YOUR APPLICATION SERVER
     * --------------------------------------------
     * The methods below request the creation of a Session and a Token to
     * your application server. This keeps your OpenVidu deployment secure.
     *
     * In this sample code, there is no user control at all. Anybody could
     * access your application server endpoints! In a real production
     * environment, your application server must identify the user to allow
     * access to the endpoints.
     *
     * Visit https://docs.openvidu.io/en/stable/application-server to learn
     * more about the integration of OpenVidu in your application server.
     */
    async getToken(mySessionId) {
      const sessionId = await this.createSession(mySessionId);
      return await this.createToken(sessionId);
    },

    // createSession(sessionId) {
    //   return new Promise((resolve, reject) => {
    //     let data = JSON.stringify({ customSessionId: sessionId });
    //     axios
    //       .post(`${OPENVIDU_SERVER_URL}openvidu/api/sessions`, data, {
    //         headers: {
    //           Authorization: `Basic ${btoa(
    //             `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
    //           )}`,
    //           "Content-Type": "application/json",
    //         },
    //       })
    //       .then((response) => {
    //         resolve(response.data.id);
    //       })
    //       .catch((response) => {
    //         let error = { ...response };
    //         if (error?.response?.status === 409) {
    //           resolve(sessionId);
    //         } else if (
    //           window.confirm(
    //             `No connection to OpenVidu Server. This may be a certificate error at "${OPENVIDU_SERVER_URL}"\n\nClick OK to navigate and accept it. ` +
    //               `If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
    //           )
    //         ) {
    //           window.location.assign(
    //             `${OPENVIDU_SERVER_URL}/accept-certificate`
    //           );
    //         }
    //       });
    //   });
    // },

    // createToken(sessionId) {
    //   return new Promise((resolve, reject) => {
    //     let data = {};
    //     axios
    //       .post(
    //         `${OPENVIDU_SERVER_URL}openvidu/api/sessions/${sessionId}/connection`,
    //         data,
    //         {
    //           headers: {
    //             Authorization: `Basic ${btoa(
    //               `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
    //             )}`,
    //             "Content-Type": "application/json",
    //           },
    //         }
    //       )
    //       .then((response) => {
    //         resolve(response.data.token);
    //       })
    //       .catch((error) => reject(error));
    //   });
    // },
    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        let data = JSON.stringify({ customSessionId: sessionId });
        axios
          .post(`${OPENVIDU_SERVER_URL}openvidu/api/sessions`, data, {
            headers: {
              Authorization: `Basic ${btoa(
                `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
              )}`,
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            resolve(response.data.id);
          })
          .catch((response) => {
            let error = { ...response };
            if (error?.response?.status === 409) {
              resolve(sessionId);
            } else if (
              window.confirm(
                `No connection to OpenVidu Server. This may be a certificate error at "${OPENVIDU_SERVER_URL}"\n\nClick OK to navigate and accept it. ` +
                  `If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
              )
            ) {
              window.location.assign(
                `${OPENVIDU_SERVER_URL}/accept-certificate`
              );
            }
          });
      });
    },

    createToken(sessionId) {
      return new Promise((resolve, reject) => {
        let data = {};
        axios
          .post(
            `${OPENVIDU_SERVER_URL}openvidu/api/sessions/${sessionId}/connection`,
            data,
            {
              headers: {
                Authorization: `Basic ${btoa(
                  `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
                )}`,
                "Content-Type": "application/json",
              },
            }
          )
          .then((response) => {
            resolve(response.data.token);
          })
          .catch((error) => reject(error));
      });
    },
  },
};
</script> -->

<style scoped>
#main-container {
  background-color: aqua;
}
.session {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: start;
}
#video-container {
  margin-left: 0px;
  margin-right: 800px;
  width: 70vh;
}
#chat-component {
  width: 300px;
  height: 150%;
}
</style>
