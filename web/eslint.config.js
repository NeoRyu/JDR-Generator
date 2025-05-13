import {defineConfig} from "eslint/config";
import js from "@eslint/js";


export default defineConfig([
  {
    files: ["**/*.js"],
    extends: [js.configs.recommended],
  },
]);


