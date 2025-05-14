import globals from "globals";
import tseslint from "typescript-eslint";
import js from "@eslint/js";

/** @type {import("eslint").Linter.FlatConfig[]} */
export default [
  js.configs.recommended,

  // Règles JavaScript et TypeScript de base
  {
    files: ["**/*.{js,ts}"],
    languageOptions: {
      ecmaVersion: "latest",
      sourceType: "module",
      globals: { ...globals.node },
    },
  },

  // Règles TypeScript
  {
    files: ["**/*.{ts}"],
    plugins: { "@typescript-eslint": tseslint.plugin },
    extends: [
      "@typescript-eslint/recommended-type-checked",
      "@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: '@typescript-eslint/parser',
    },
    rules: {
    },
  },
];