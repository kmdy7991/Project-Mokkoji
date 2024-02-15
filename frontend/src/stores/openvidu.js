import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import { userStore } from "./user";

const OPENVIDU_SERVER_URL = import.meta.env.VITE_APP_OPEN_VIDU_API_URL;
const OPENVIDU_SERVER_SECRET = "SSAFY";

export const useOpenViduStore = defineStore(
  "openvidu",
  () => {
    const OV = ref();
    const session = ref();
    const publisher = ref(undefined);
    const subscribers = ref([]);
    const store = userStore();
    const mySessionId = ref("" + Math.floor(Math.random() * 100));

    const joinSession = (payload) => {
      const { roomId } = payload;
      // 1) Get an OpenVidu object
      OV.value = new OpenVidu();
      // 2) Init a session
      session.value = OV.value.initSession();
      // 3) Specify the actions when events take place in the session
      // On every new Stream received...
      session.value.on("streamCreated", ({ stream }) => {
        const subscriber = session.value.subscribe(stream);
        subscribers.value.push(subscriber);
      });

      // On every Stream destroyed...
      session.value.on("streamDestroyed", ({ stream }) => {
        const index = subscribers.value.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          subscribers.value.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      session.value.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // 4) Connect to the session with a valid user token
      getToken(payload.roomId).then((token) => {
        session.value
          .connect(token, { clientData: store.myName })
          .then(() => {
            // 5) Get your own camera stream with the desired properties
            let newPublisher = OV.value.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "800x600", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            publisher.value = newPublisher;
            session.value.publish(newPublisher);
          })
          .catch((error) => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });
    };

    const leaveSession = () => {
      if (session.value) session.value.disconnect();
      session.value = undefined;
      publisher.value = undefined;
      subscribers.value = [];
      OV.value = undefined;
    };

    const getToken = async (mySessionId) => {
      try {
        // Await the resolution of createSession before moving to createToken
        const sessionId = await createSession(mySessionId);
        return await createToken(sessionId);
      } catch (error) {
        // Handle any errors that might occur during createSession or createToken
        console.error("Error getting token:", error);
        throw error;
      }
    };

    const createSession = async (sessionId) => {
      try {
        let datas = JSON.stringify({ customSessionId: sessionId });

        const response = async () =>
          await axios.post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions`,
            datas,
            {
              headers: {
                Authorization: `Basic ${btoa(
                  `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
                )}`,
                "Content-Type": "application/json",
              },
            }
          );

        let test = await response();
        return test.data.id;
      } catch (error) {
        console.log(error);
        if (error?.response?.status === 409) {
          return sessionId;
        } else {
          throw error;
        }
      }
    };

    const createToken = async (sessionId) => {
      try {
        let data = { customSessionId: sessionId };
        const response = async () =>
          await axios.post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            data,
            {
              headers: {
                Authorization: `Basic ${btoa(
                  `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
                )}`,
                "Content-Type": "application/json",
              },
            }
          );
        let test = await response();
        return test.data.token;
      } catch (error) {
        throw error;
      }
    };

    return {
      OV,
      session,
      publisher,
      subscribers,
      mySessionId,
      joinSession,
      leaveSession,
      getToken,
      createSession,
      createToken,
    };
  },
  { persist: true }
);
