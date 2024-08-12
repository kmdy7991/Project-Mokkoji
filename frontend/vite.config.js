import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// import mkcert from "vite-plugin-mkcert"; //화상 연결 필요시.
// import fs from "fs";

// // https://vitejs.dev/config/
// export default defineConfig({
//   plugins: [vue(), mkcert()],
//   resolve: {
//     alias: {
//       "@": fileURLToPath(new URL("./src", import.meta.url)),
//     },
//   },
//   server: {
//     https: {
//       key: fs.readFileSync("C:/Users/SSAFY/Desktop/localhost-key.pem"),
//       cert: fs.readFileSync("C:/Users/SSAFY/Desktop/localhost.pem"),
//     },
//   },
// });

// "vite-plugin-mkcert": "^1.17.3"

// import { fileURLToPath, URL } from "node:url";

// import { defineConfig } from "vite";
// import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});
