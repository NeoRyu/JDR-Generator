import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import pluginReact from "eslint-plugin-react";
import {defineConfig} from "eslint/config";

export default defineConfig([
  // Règles JavaScript de base (et TypeScript)
  {
    files: ["**/*.{js,ts,jsx,tsx}"], // Tous les fichiers pertinents
    plugins: { js },
    extends: ["eslint:recommended"], // Utiliser eslint:recommended
    languageOptions: {
      ecmaVersion: "latest", // Ou une version spécifique
      sourceType: "module", // Modules ES
      globals: { ...globals.browser }, // Environnement navigateur
    },
  },

  // Règles TypeScript
  {
    files: ["**/*.{ts,tsx}"], // Fichiers TypeScript/TSX uniquement
    plugins: { "@typescript-eslint": tseslint.plugin },
    extends: [
      "plugin:@typescript-eslint/recommended-type-checked", // Plus strict
      "plugin:@typescript-eslint/stylistic-type-checked",
    ],
    languageOptions: {
      parser: tseslint.parser, // Parser TypeScript
      sourceType: "module",
      ecmaVersion: "latest",
      projectFolder: __dirname,
    },
    parserOptions: {
      project: true, // Activer les règles qui nécessitent des informations de type
      tsconfigRootDir: __dirname,
    },
  },

  // Règles React
  {
    files: ["**/*.{jsx,tsx}"], // Fichiers JSX/TSX uniquement
    plugins: { react: pluginReact },
    extends: ["plugin:react/recommended", "plugin:react/jsx-runtime"],
    settings: { react: { version: "detect" } }, // Détecter la version de React
  },

  // Intégration de Prettier
  {
    files: ["**/*.{js,ts,jsx,tsx}"],
    extends: ["eslint-config-prettier"],
    rules: {},
  },
]);