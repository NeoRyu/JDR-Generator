# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un
monorepo tournant en localhost ; contenant à la fois deux API backend, l'un développé en JAVA pour communiquer avec la
database MySQL ajoutée via FlywayDb, l'autre avec NESTJS pour communiquer avec les API de Google Gemini, et une
interface frontend en React Typescript.

## Caractéristiques principales:
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

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

5. Mettre à jour votre fichier `.env` avec votre clé API (https://aistudio.google.com/apikey) et votre username de connection windows.

6. Ajouter un dossier nommé `jdr-generator` (ou tout autre nom que vous définirez dans le fichier `.env` pour `DOWNLOAD_FOLDER`) dans votre dossier de téléchargement 
(`C:\Users\`{windows_username}`\Downloads\jdr-generator\`)
   > Pour choisir un autre emplacement, modifiez les variables `pathSrc` dans les fichiers du dossier 'controllers'

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
  > A noter que les JSON et images seront ajoutés dans le dossier configuré dans le fichier `.env`

- Exécuter une requête POST sur http://localhost:3000/illustrate en ajoutant dans le body en raw le contenu de l'attribut "image" obtenu précédemment pour générer une illustration, exemple :
    ```json
    { "prompt" : "Using Imagen3, generate an illustration in a heroic-fantasy style, but not realistic, close to that of the French illustrator Grosnez (https://www.artstation.com/grosnez) based on this prompt: Une femme élégante aux longs cheveux bruns et aux yeux verts, portant un chapeau d'aventurière et tenant une carte ancienne." }
    ```

- Exécuter une requête POST sur http://localhost:3000/stats en ajoutant dans le body en raw un contenu plus complexe basé sur les détails obtenus précédemment pour générer un json contenant diverses traits de statistiques, exemple :
    ```json
    { "prompt" : "promptSystem: 'context.PromptSystem', promptRace: 'context.PromptRace', promptGender: 'context.PromptGender', promptClass: 'context.PromptClass', promptDescription: 'context.PromptDescription', name: 'details.Name', age: 'details.Age', education: 'details.Education', profession: 'details.Profession', reasonForProfession: 'details.ReasonForProfession', workPreferences: 'details.WorkPreferences', changeInSelf: 'details.ChangeInSelf', changeInWorld: 'details.ChangeInWorld', goal: 'details.Goal', reasonForGoal: 'details.ReasonForGoal'" }
    ```
