# JDR-Generator API (OpenAI)

## Description

Ce module est une application Node.js/Express/TypeScript conçue pour fournir une API REST permettant de générer des images à l'aide de l'API DALL-E d'OpenAI. Il sert d'alternative au module Gemini pour la génération d'images, offrant une flexibilité basée sur la disponibilité et les fonctionnalités de l'API.

## Caractéristiques

- **Génération d'images avec DALL-E :** Fournit un point de terminaison `/illustrate` pour générer des images à partir d'une invite textuelle.
- **Configuration flexible :** Configurable via des variables d'environnement (PORT, API_KEY, DOWNLOAD_FOLDER).
- **Intégration facile :** Peut être intégré dans des projets Node.js ou utilisé comme service autonome.

## Technologies Utilisées

- Node.js
- Express (pour le serveur web)
- TypeScript
- OpenAI API (DALL-E)

## Configuration

1.  **Obtenir une clé API OpenAI :**

- Rendez-vous sur la plateforme OpenAI : [https://platform.openai.com/](https://platform.openai.com/)
- Créez un compte ou connectez-vous.
- Générez une nouvelle clé secrète dans la section "API keys".
- **Important :** Conservez cette clé en sécurité et ne la partagez jamais publiquement.

2.  **Variables d'environnement :**

- Créez un fichier `.env` à la racine du répertoire de ce module.
- Vous pouvez également configurer ces variables dans votre fichier `docker-compose.yml` si vous utilisez Docker.
- Ajoutez les variables suivantes au fichier `.env` :

  - `PORT`: Le port sur lequel le serveur Express écoutera (par exemple, 3002).
  - `API_KEY`: Votre clé API OpenAI secrète.
  - `DOWNLOAD_FOLDER`: Le nom du sous-dossier dans `/app/downloads/` où les images générées seront enregistrées à l'intérieur du conteneur Docker (doit correspondre à la configuration de votre service API et aux montages de volume, si applicable). Si vous n'utilisez pas Docker, ce sera le chemin relatif dans votre système de fichiers.

- Exemple de fichier `.env` :

  ```
  PORT=3002
  API_KEY=sk-votre-cle-api-openai
  DOWNLOAD_FOLDER=jdr-generator
  ```

3.  **Installer les dépendances :**

- Assurez-vous que Node.js et npm (ou yarn) sont installés.
- Ouvrez un terminal dans le répertoire du module et exécutez :

  ```bash
  npm install
  ```

## Utilisation

### Exécution de l'application

1.  **Compiler le code TypeScript (si nécessaire) :**

- Si vous n'avez pas encore compilé le code TypeScript, exécutez :

  ```bash
  npm run build # ou tsc
  ```

2.  **Démarrer le serveur :**

- Exécutez la commande suivante pour démarrer le serveur Express :

  ```bash
  npm run start # ou node dist/index.js (si vous avez compilé manuellement)
  ```

- Le serveur écoutera sur le port spécifié dans la variable d'environnement `PORT` (par défaut, 3002).

### API Endpoint

- **`POST /illustrate`**

  - **Description :** Génère une image à partir d'une invite textuelle.
  - **Méthode :** `POST`
  - **URL :** `http://localhost:{PORT}/illustrate` (Remplacez `{PORT}` par le port configuré, par exemple, `http://localhost:3002/illustrate`)
  - **Corps de la requête :**

    - **Format :** JSON
    - **Exemple :**

      ```json
      {
        "prompt": "Un portrait d'un guerrier elfe avec une armure dorée, éclairé par la lumière du soleil couchant."
      }
      ```

  - **Réponse :**

    - **Format :** JSON
    - **Contenu :** Un objet JSON contenant des informations sur l'image générée. Cela peut inclure :
      - `imageUrl`: Une URL vers l'image générée.
      - `imageData`: Les données de l'image encodées (par exemple, en base64).
      - `localFilePath`: Le chemin du fichier où l'image a été enregistrée (si l'API l'enregistre localement).
    - **Exemple de réponse :**

      ```json
      {
        "imageUrl": "[https://example.com/images/image_123.png](https://example.com/images/image_123.png)",
        "localFilePath": "/app/downloads/jdr-generator/image_123.png"
      }
      ```

### Gestion des erreurs

- L'API doit renvoyer des codes d'état HTTP appropriés en cas d'erreur (par exemple, 400 Bad Request pour une requête invalide, 500 Internal Server Error en cas d'erreur côté serveur).
- Les réponses d'erreur doivent inclure un message JSON descriptif.

  - Exemple de réponse d'erreur :

    ```json
    {
      "error": "L'invite est requise.",
      "code": "MISSING_PROMPT"
    }
    ```

## Docker (Optionnel)

Si vous utilisez Docker pour déployer cette API, voici quelques instructions :

### Dockerfile

Voici un exemple de `Dockerfile` pour construire l'image Docker :

```dockerfile
FROM node:18-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

CMD [ "npm", "start" ]
```

### Construction de l'image Docker

```bash
cd C:\<projects_repositories_path>\JDR-Generator\api
docker build -t jdr-generator-openai -f Dockerfile .
```

### Exécution du conteneur Docker

```bash
docker run -p 3002:3002 \
           -e PORT=3002 \
           -e API_KEY=sk-votre-cle-api-openai \
           -e DOWNLOAD_FOLDER=jdr-generator \
           -v /chemin/vers/vos/telechargements:/app/downloads \
           jdr-generator-openai
```

- Remplacez `/chemin/vers/vos/telechargements` par le chemin réel sur votre machine hôte où vous souhaitez enregistrer les images.
- Assurez-vous que le port (`3002` dans cet exemple) correspond à la variable `PORT` et aux ports exposés.

### Docker Compose (Si vous utilisez Docker Compose au niveau du monorepo)

Exemple de configuration dans `docker-compose.yml` :

```yaml
services:
  openai:
    build:
      context: ./openai
      dockerfile: Dockerfile
    ports:
      - "3002:3002"
    environment:
      PORT: 3002
      API_KEY: sk-votre-cle-api-openai
      DOWNLOAD_FOLDER: jdr-generator
    volumes:
      - /chemin/vers/vos/telechargements:/app/downloads
    # depends_on: # Si nécessaire
    #   - ...
```

##  Intégration de GitHub Actions

Ce projet utilise GitHub Actions pour automatiser à la fois les vérifications de la qualité du code et le déploiement des images Docker.
Cette approche permet d'assurer un code de haute qualité et de simplifier le processus de déploiement.

###  Workflow de Qualité du Code

La qualité du code est vérifiée automatiquement à chaque *push* et *pull request* grâce à un workflow GitHub Actions défini dans le fichier `.github/workflows/code-quality.yml`.
Ce workflow contient une section dédiée à la partie du projet `nodejs-code-quality-openai` et s'exécute depuis toutes les branches.

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

* Le workflow ne construit et ne pousse une image Docker que si des modifications sont détectées dans le dossier source correspondant (ici openai/).
* Il est possible de déclencher manuellement le workflow pour forcer la reconstruction de toutes les images.
* Cette approche optimise le temps d'exécution et l'utilisation des ressources.

**Étapes du workflow de déploiement :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Connexion à Docker Hub :** Utilise les secrets GitHub `DOCKERHUB_USERNAME` et `DOCKERHUB_TOKEN` pour se connecter au compte Docker Hub.
3.  **Build et push des images :** Pour ce module 'openai', l'image Docker est construite et taguée avec le SHA du commit actuel ainsi que le tag `latest`, puis les deux tags sont poussés vers Docker Hub. Les images sont disponibles sur ce repo : [https://hub.docker.com/repositories/eli256](https://hub.docker.com/repositories/eli256)

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
