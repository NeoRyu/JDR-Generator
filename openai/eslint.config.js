import globals from "globals";
import tseslint from "typescript-eslint";
import {defineConfig} from "eslint/config";
import path from "node:path";
import {fileURLToPath} from "node:url";
import js from "@eslint/js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default defineConfig([
  // Règles JavaScript et TypeScript de base
  {
    files: ["**/*.{js,ts}"], // Changement : Pas de JSX/TSX pour une API
    extends: [js.configs.recommended],
    languageOptions: {
      ecmaVersion: "latest",
      sourceType: "module",
      globals: { ...globals.node },  // Changement : globals.node pour Node.js
    },
  },

  // Règles TypeScript
  {
    files: ["**/*.{ts}"],  // Changement : Pas de TSX pour une API
    plugins: { "@typescript-eslint": tseslint.plugin },
    extends: [
      "@typescript-eslint/recommended-type-checked",
      "@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: tseslint.parser,
      parserOptions: {
        project: [
          path.resolve(__dirname, "tsconfig.json"),
        ],
        tsconfigRootDir: __dirname,
        sourceType: "module",
        ecmaVersion: "latest",
      },
    },
    rules: {
      // TODO : Commenter les règles plus tard pour corriger les errors manuellement
      "@typescript-eslint/prefer-optional-chain": "off",
      "@typescript-eslint/no-misused-promises": "off",
      "@typescript-eslint/no-empty-object-type": "off",
      "@typescript-eslint/consistent-type-definitions": "off",
      "@typescript-eslint/await-thenable": "off",
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-unsafe-assignment": "off",
      "@typescript-eslint/no-unsafe-argument": "off",
      "@typescript-eslint/no-unsafe-member-access": "off",
      "@typescript-eslint/no-unsafe-call": "off",
    },
  },
]);