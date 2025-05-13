# JDR-Generator API (Gemini)

## Description

Ce module contient l'API backend développée avec NestJS, dédiée à l'interaction avec les APIs de Google Gemini. Son rôle principal est de permettre la génération de texte (descriptions de personnages, statistiques) et d'images pour les personnages de jeux de rôle, enrichissant ainsi l'expérience utilisateur de JDR-Generator.

## Caractéristiques Principales

- **Interaction avec Google Gemini :** Utilise les fonctionnalités de Google Gemini pour la génération de contenu.
- **API REST :** Fournit une API RESTful pour accéder aux fonctionnalités de génération.
- **Génération de Texte :** Crée des descriptions détaillées et des statistiques de personnages basées sur un contexte donné.
- **Génération d'Images :** Génère des illustrations de personnages.
- **Développement avec NestJS :** Utilise NestJS (Node.js et TypeScript) pour une architecture robuste et évolutive.

## Technologies Utilisées

- Node.js
- NestJS (TypeScript)
- Google Gemini API

## Notes Importantes

- **Sécurité des clés API :** Traitez votre clé API Google Gemini avec la plus grande précaution. Ne la partagez jamais publiquement. Utilisez toujours des variables d'environnement pour la stocker.

## Configuration Requise

1.  **Installer Node.js :** Téléchargez et installez Node.js depuis [https://nodejs.org/en/download/](https://nodejs.org/en/download/) (Version recommandée : LTS).

2.  **Installer TypeScript (globalement) :**

    ```sh
    npm install -g typescript
    ```

3.  **Installer les dépendances du projet :**

    ```sh
    npm install
    ```

## Configuration

1.  **Obtenir une clé d'API Google pour Gemini :**

    - Accédez à la documentation de Google Gemini pour les détails sur l'obtention d'une clé d'API : [https://ai.google.dev/gemini-api/docs/pricing?hl=fr](https://ai.google.dev/gemini-api/docs/pricing?hl=fr)
    - Vous pouvez généralement obtenir votre clé API depuis la console Google Cloud ou un service similaire (par exemple, Google AI Studio).

2.  **Configurer les variables d'environnement :**

    - Créez un fichier `.env` à la racine du projet (s'il n'existe pas déjà).
    - Ajoutez les variables d'environnement suivantes à ce fichier :

      ```
      GOOGLE_API_KEY=YOUR_GEMINI_API_KEY  # Remplacez par votre clé API Gemini
      DOWNLOAD_FOLDER=jdr-generator       # Nom du dossier de téléchargement (peut être personnalisé)
      # WINDOWS_USERNAME=your_windows_username # (Optionnel, si nécessaire pour les chemins)
      ```

    - **Important :** Ne commitez jamais votre fichier `.env` contenant des clés API dans un dépôt public.

3.  **Configurer le dossier de téléchargement :**

    - Par défaut, les JSON et les images générés seront enregistrés dans un dossier nommé `jdr-generator` dans votre dossier de téléchargements.
    - Créez ce dossier manuellement si vous ne l'avez pas déjà :

      - Windows : `C:\Users\{your_windows_username}\Downloads\jdr-generator\` (Remplacez `{your_windows_username}` par votre nom d'utilisateur Windows)

    - Pour personnaliser l'emplacement du dossier de téléchargement, modifiez la variable `DOWNLOAD_FOLDER` dans le fichier `.env`.
    - **Note :** Si vous personnalisez le dossier de téléchargement, assurez-vous d'ajuster les chemins en conséquence dans les fichiers du dossier `controllers` (si nécessaire).

## Générer et Exécuter

1.  **Supprimer le dossier `dist` (avant une nouvelle compilation) :**

    ```sh
    rd /s /q dist
    ```

    - (Cette commande est pour Windows. Pour d'autres systèmes d'exploitation, utilisez l'équivalent `rm -rf dist`.)

2.  **Compiler le code TypeScript :**

    ```sh
    tsc
    ```

    - Cela compile le code TypeScript en JavaScript et place le résultat dans le dossier `dist`.

3.  **Démarrer le serveur :**

    ```sh
    node dist/app.js
    ```

    - Cela exécute le serveur NestJS à partir du code JavaScript compilé.

## API Endpoints

(À compléter avec la liste complète des points de terminaison de l'API, leurs méthodes HTTP, les paramètres attendus, les formats de requête et de réponse, et des exemples. Ceci est crucial pour l'intégration avec le frontend et pour les tests.)

Voici une version plus détaillée basée sur votre exemple :

- **`POST /generate`**

  - **Description :** Génère les détails d'un personnage RPG à partir d'un contexte fourni.
  - **Requête :**

    - **Méthode :** `POST`
    - **URL :** `http://localhost:3001/generate`
    - **Corps (raw/JSON) :**

      ```json
      {
        "prompt_system": "Nom du système de jeu (ex: Dungeons & Dragons)",
        "prompt_race": "Race du personnage",
        "prompt_gender": "Genre du personnage",
        "prompt_class": "Classe du personnage",
        "prompt_description": "Description du contexte du personnage"
      }
      ```

  - **Réponse :**

    - **Format :** JSON
    - **Contenu :** Un objet JSON contenant les détails du personnage générés (nom, âge, histoire, etc.).
    - **Exemple :** (Voir l'exemple de réponse que vous avez fourni dans votre description)

- **`POST /illustrate`**

  - **Description :** Génère une illustration pour le personnage en utilisant la description d'image générée précédemment.
  - **Requête :**

    - **Méthode :** `POST`
    - **URL :** `http://localhost:3002/illustrate`
    - **Corps (raw/JSON) :**

      ```json
      {
        "prompt": "Prompt pour la génération d'image (basé sur l'attribut 'image' généré précédemment)"
      }
      ```

  - **Réponse :**

    - **Format :** JSON (peut contenir une URL vers l'image, ou les données de l'image encodées)

- **`POST /stats`**

  - **Description :** Génère les statistiques du personnage (force, dextérité, etc.) en fonction du contexte et des détails du personnage générés.
  - **Requête :**

    - **Méthode :** `POST`
    - **URL :** `http://localhost:3001/stats`
    - **Corps (raw/JSON) :**

      ```json
      {
        "prompt": "Objet JSON contenant les détails du personnage (voir l'exemple que vous avez fourni)"
      }
      ```

  - **Réponse :**

    - **Format :** JSON
    - **Contenu :** Un objet JSON contenant les statistiques du personnage (force, dextérité, etc.). (Voir l'exemple de réponse que vous avez fourni)

## Sortie des Données

- Les données générées (JSON et images) sont enregistrées sous forme de fichiers dans le dossier de téléchargement configuré (`DOWNLOAD_FOLDER` dans le fichier `.env`).
- Par défaut, ce dossier est `jdr-generator` dans le dossier de téléchargements de l'utilisateur.

## Docker (Optionnel - Exécution Locale)

### Construction de l'image Docker

1.  **Construire l'image Docker locale :**

    ```bash
    cd C:\<projects_repositories_path>\JDR-Generator\openai
    docker build -t jdr-generator-openai -f Dockerfile .
    ```

    (Assurez-vous d'être dans le répertoire `openai`.)

### Exécution du Conteneur Docker Local

1.  **Exécuter le conteneur Docker local :**

    ```bash
    docker run -p 3001:3001 \
               -e PORT=3001 \
               -e API_KEY=sk-votre-cle-api-openai \
               -e DOWNLOAD_FOLDER=jdr-generator \
               -v /chemin/vers/vos/telechargements:/app/downloads \
               jdr-generator-openai
    ```

    - Remplacez `/chemin/vers/vos/telechargements` par le chemin réel sur votre machine hôte où vous souhaitez enregistrer les images.
    - Assurez-vous que le port (`3001` sur l'hôte mappé à `3001` dans le conteneur dans cet exemple) correspond à la variable `PORT` et aux ports exposés.

### Docker Compose Local

1.  **S'assurer que le service `openai-container` est correctement configuré dans `docker-compose.local.yml` :**

    ```yaml
    services:
      openai-container:
        build:
          context: ./openai
          dockerfile: Dockerfile
        ports:
          - "3001:3001"
        environment:
          PORT: 3001
          API_KEY: sk-votre-cle-api-openai
          DOWNLOAD_FOLDER: jdr-generator
        volumes:
          - /chemin/vers/vos/telechargements:/app/downloads
        # depends_on: # Si nécessaire
        #   - ...
    ```

2.  **Démarrer le service via Docker Compose local :**

    ```bash
    docker-compose -f docker-compose.local.yml up -d openai-container
    ```

## Sécurité

- **Clé API :** Sécurisez votre clé API OpenAI. Ne l'incluez jamais directement dans votre code. Utilisez toujours des variables d'environnement.
- **Validation :** Validez les données d'entrée (par exemple, la longueur et le format de l'invite) pour éviter les erreurs et les abus.
- **Limitation de débit :** Envisagez d'implémenter une limitation de débit pour éviter la surcharge de l'API OpenAI et les coûts excessifs.

## Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
