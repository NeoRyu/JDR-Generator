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


##  Intégration de GitHub Actions

Ce projet utilise GitHub Actions pour automatiser à la fois les vérifications de la qualité du code et le déploiement des images Docker.
Cette approche permet d'assurer un code de haute qualité et de simplifier le processus de déploiement.

###  Workflow de Qualité du Code

La qualité du code est vérifiée automatiquement à chaque *push* et *pull request* grâce à un workflow GitHub Actions défini dans le fichier `.github/workflows/code-quality.yml`.
Ce workflow contient une section dédiée à la partie du projet `nodejs-code-quality-gemini` et s'exécute depuis toutes les branches.

**Fonctionnement :**

* Le workflow vérifie la qualité du code à chaque *push* et *pull request* sur toutes les branches.
* Des vérifications spécifiques sont exécutées pour ce module Node.js.
* Les résultats des vérifications sont disponibles dans l'interface GitHub Actions.

**Étapes du workflow de qualité du code :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Configuration de Node.js :** Configure l'environnement Node.js avec la version spécifiée.
3.  **Installation des dépendances :** Installe les dépendances du projet.
4.  **Vérifications :**
    * **Linting :** ESLint est utilisé pour vérifier le style et la qualité du code.
    * **Formatage :** Prettier est utilisé pour formater le code de manière cohérente.
    * **Compilation :** Le code TypeScript est compilé pour détecter les erreurs de typage.

###  Workflow de Déploiement Continu avec Docker

Le déploiement des images Docker est également automatisé via GitHub Actions avec le workflow défini dans `.github/workflows/docker-push.yml`.
Ce workflow construit et publie les images Docker vers Docker Hub lorsqu'un *push* est effectué sur les branches spécifiées (actuellement `githubactions` et `main`).

**Fonctionnement :**

* Le workflow ne construit et ne pousse une image Docker que si des modifications sont détectées dans le dossier source correspondant (ici gemini/).
* Il est possible de déclencher manuellement le workflow pour forcer la reconstruction de toutes les images.
* Cette approche optimise le temps d'exécution et l'utilisation des ressources.

**Étapes du workflow de déploiement :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Connexion à Docker Hub :** Utilise les secrets GitHub `DOCKERHUB_USERNAME` et `DOCKERHUB_TOKEN` pour se connecter au compte Docker Hub.
3.  **Build et push des images :** Pour ce module 'gemini', l'image Docker est construite et taguée avec le SHA du commit actuel ainsi que le tag `latest`, puis les deux tags sont poussés vers Docker Hub. Les images sont disponibles sur ce repo : [https://hub.docker.com/repositories/eli256](https://hub.docker.com/repositories/eli256)

En combinant ces deux workflows GitHub Actions, cela assure à la fois la qualité du code et un déploiement efficace et automatisé de l'application.

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
