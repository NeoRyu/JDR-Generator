import globals from "globals";
import tseslint from "typescript-eslint";
import pluginReact from "eslint-plugin-react";
import {defineConfig} from "eslint/config";
import path from "node:path";
import js from "@eslint/js";

export default defineConfig([
  // Règles JavaScript et TypeScript de base
  {
    files: ["**/*.{js,ts,jsx,tsx}"],
    plugins: { js },
    extends: ["eslint:recommended"],
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
      "plugin:@typescript-eslint/recommended-type-checked",
      "plugin:@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: tseslint.parser,
      parserOptions: {
        project: true,
        tsconfigRootDir: path.resolve(__dirname),
        sourceType: "module",
        ecmaVersion: "latest",
      },
    },
  },

  // Règles React
  {
    files: ["**/*.{jsx,tsx}"],
    plugins: { react: pluginReact },
    extends: ["plugin:react/recommended", "plugin:react/jsx-runtime"],
    settings: { react: { version: "detect" } },
    rules: {
      "react/prop-types": "off",
      "react/react-in-jsx-scope": "off",
    },
  },

  // Intégration de Prettier
  {
    files: ["**/*.{js,ts,jsx,tsx}"],
    extends: ["eslint-config-prettier"],
    rules: {},
  },
]);