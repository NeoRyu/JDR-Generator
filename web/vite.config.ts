import react from "@vitejs/plugin-react-swc";
import path from "node:path";
import {defineConfig} from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      "/characters": {
        target: "http://34.155.16.213:80", // Ce proxy est pour le serveur de dev seulement
        changeOrigin: true,
      },
    },
  },
});
