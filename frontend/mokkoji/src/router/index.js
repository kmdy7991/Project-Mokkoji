import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import WaitroomView from "../views/WaitroomView.vue";
import TalkBodyGameView from "../views/TalkBodyGameView.vue";
import AuthView from "../views/AuthView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/waitRoom",
      name: "waitRoom",
      component: WaitroomView,
    },
    {
      path: "/TalkBody/:id",
      name: "TalkBody",
      component: TalkBodyGameView,
    },
    {
      path: "/auth",
      name: "Auth",
      component: AuthView,
    },
  ],
});

export default router;