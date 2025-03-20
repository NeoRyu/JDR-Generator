# API : Google Generative AI

**Ce projet inclut le code source pour interagir avec Gemini à partir d'une API REST de base développée en Node.js avec Express et TypeScript.**

## Configuration requise

1. Installer Node.js : [https://nodejs.org/en/download/](https://nodejs.org/en/download/)

2. Installer TypeScript
    ```sh
    npm install -g typescript
    ```

3. Installer les dépendances requises
    ```sh
    npm install
    ```

4. Obtenir une clé d'API GOOGLE pour Gemini : [https://ai.google.dev/gemini-api/docs/pricing?hl=fr](https://ai.google.dev/gemini-api/docs/pricing?hl=fr)

5. Mettre à jour votre fichier `.env` avec votre clé API (https://aistudio.google.com/apikey)

## Générer et exécuter

- Pour supprimer le dossier `dist` (nécessaire avant un rebuild) :
    ```sh
    rd /s /q dist
    ```

- Pour compiler le code JS à partir du code source TypeScript.
    ```sh
    tsc
    ```

- Pour démarrer le serveur depuis le dossier compilé (`dist`).
    ```sh
    node dist/app.js
    ```

## Tester les endpoints

- Exécuter une requête POST sur http://localhost:3000/generate pour obtenir le JSON d'un personnage généré depuis Gemini
  > A noter que les JSON et images seront ajoutés dans le dossier configurer dans le fichier `.env`

- Exécuter une requête POST sur http://localhost:3000/illustrate en ajoutant dans le body en raw le contenu de l'attribut "image" obtenu précédemment pour générer une illustration, exemple :
    ```json
    { "prompt" : "Using Imagen3, generate an illustration in a heroic-fantasy style, but not realistic, close to that of the French illustrator Grosnez (https://www.artstation.com/grosnez) based on this prompt: Une femme élégante aux longs cheveux bruns et aux yeux verts, portant un chapeau d'aventurière et tenant une carte ancienne." }
    ```
