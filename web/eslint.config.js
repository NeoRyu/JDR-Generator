import globals from "globals";
import tseslint from "typescript-eslint";
import pluginReact from "eslint-plugin-react";
import {defineConfig} from "eslint/config";
import path from "node:path";
import {fileURLToPath} from "node:url";
import js from "@eslint/js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default defineConfig([
  // Règles JavaScript de base
  {
    files: ["**/*.{js,ts,jsx,tsx}"],
    extends: [js.configs.recommended],
    languageOptions: {
      ecmaVersion: "latest",
      sourceType: "module",
      globals: { ...globals.browser },
    },
  },

  // Règles TypeScript
  {
    files: ["**/*.{ts,tsx}"],
    plugins: { "@typescript-eslint": tseslint.plugin },
    extends: [
      "@typescript-eslint/recommended-type-checked",
      "@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: tseslint.parser,
      parserOptions: {
        project: path.resolve(__dirname, "tsconfig.json"),
        tsconfigRootDir: __dirname,
        sourceType: "module",
        ecmaVersion: "latest",
      },
    },
  },

  // Règles React
  {
    files: ["**/*.{jsx,tsx}"],
    plugins: { react: pluginReact },
    extends: ["react/recommended", "react/jsx-runtime"],
    settings: { react: { version: "detect" } },
    rules: {
      "react/prop-types": "off",
      "react/react-in-jsx-scope": "off",
    },
  },
]);