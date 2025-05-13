import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import {defineConfig} from "eslint/config";

export default defineConfig([
  // Règles JavaScript de base (même pour TypeScript)
  {
    files: ["**/*.ts"], //  On ne lint que du TS
    plugins: { js },
    extends: ["eslint:recommended"], // Utiliser "eslint:recommended"
    languageOptions: {
      ecmaVersion: "latest", // Ou une version spécifique
      sourceType: "module", // Si vous utilisez les modules ES
      globals: { ...globals.node }, // Environnement Node.js
    },
  },

  // Règles TypeScript
  {
    files: ["**/*.ts"],
    plugins: { "@typescript-eslint": tseslint.plugin },
    extends: [
      "plugin:@typescript-eslint/recommended-type-checked", //  Plus strict
      "plugin:@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: tseslint.parser, // Utiliser le parser de tseslint
      sourceType: "module",
      ecmaVersion: "latest",
    },
    parserOptions: {
      project: true, // Activer les règles qui nécessitent des informations de type
      tsconfigRootDir: __dirname,
    },
  },

  // Intégration de Prettier (optionnel, mais recommandé si vous utilisez Prettier)
  {
    files: ["**/*.ts"],
    extends: ["eslint-config-prettier"],
    rules: {}, // Peut être vide ou contenir des règles spécifiques
  },
]);
