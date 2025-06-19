# JDR-Generator - Module WEB (Frontend React/TypeScript)

## Table des Matières

1.  [Description](#description)
2.  [Caractéristiques Principales](#caractéristiques-principales)
3.  [Technologies Utilisées](#technologies-utilisées)
4.  [Pré-requis](#pré-requis)
5.  [Structure du Projet et Logique de Communication](#structure-du-projet-et-logique-de-communication)
    * [Communication Inter-modules](#communication-inter-modules)
    * [Services Spécifiques desservis par le backend API](#services-spécifiques-desservis-par-le-backend-api)
6.  [Exécution Locale (sans Docker)](#exécution-locale-sans-docker)
7.  [Exécution locale Docker](#exécution-locale-docker)
    * [Pré-requis](#pré-requis-1)
    * [Fichiers Clés](#fichiers-clés)
    * [Docker Compose Local](#docker-compose-local)
8.  [Déploiement sur Kubernetes](#déploiement-sur-kubernetes-wip)
    * [Pré-requis](#pré-requis-2)
    * [Fichiers Clés](#fichiers-clés-1)
    * [Processus de Déploiement](#processus-de-déploiement)
    * [Accès à l'Application](#accès-à-lapplication)
9.  [Licence](#licence)

---

## Description

Ce module contient l'interface utilisateur web de JDR-Generator, développée avec React et TypeScript. Son rôle principal est de fournir une interface interactive permettant aux utilisateurs de définir le contexte de création de personnage et de visualiser les personnages générés par les APIs backend (descriptions détaillées, statistiques, illustrations uniques).

## Caractéristiques Principales

* **Interface Interactive :** Permet aux utilisateurs de saisir facilement les informations nécessaires pour générer des personnages (race, classe, système de jeu, etc.) et de modifier les personnages existants.
* **Visualisation des Personnages :** Affiche les détails complets des personnages générés (texte et images) de manière claire et conviviale.
* **Génération de PDF :** Permet de générer un PDF imprimable d'un personnage avec toutes ses informations et son illustration.
* **Développement avec React et TypeScript :** Utilise React pour la construction de l'interface utilisateur et TypeScript pour une meilleure gestion du code et des types.
* **Communication avec les APIs Backend :** Interagit avec l'API Java (`api`) qui elle même communique avec les APIs Node.js (`gemini`, `openai`, `freepik`) pour récupérer et afficher les données des personnages et gérer les générations.

## Technologies Utilisées

* React
* TypeScript
* Vite
* npm ou yarn
* Axios (pour les requêtes HTTP)
* React Query (pour la gestion des données asynchrones)
* Shadcn/ui (pour les composants UI)
* Tailwind CSS (pour le stylisme)
* Radix UI (pour les composants d'interface utilisateur non stylisés)
* dayjs (pour la manipulation des dates)
* lucide-react (pour les icônes)

## Pré-requis

* Node.js (version 20 ou supérieure recommandée)
* npm ou yarn

## Structure du Projet (Aperçu)

```
JDR-Generator/
├── README.md                         <-- VOUS ETES ICI
├── .env
├── .gitignore
├── docker-compose.local.yml           <-- Fichier Docker Compose principal
├── JDR-Generator.iml
├── api/ ...
├── freepik/ ...
├── gemini/ ...
├── openai/ ...
├── web/
│   └── src/
│   │   ├── components/
│   │   │   ├── form/
│   │   │   │   └── character-form.tsx      <-- Formulaire pour mise à jour des champs
│   │   │   ├── model/
│   │   │   │   ├── character-context.model.tsx
│   │   │   │   ├── character-details.model.tsx
│   │   │   │   ├── character-full.model.tsx
│   │   │   │   ├── character-illustration.model.tsx
│   │   │   │   └── character-json.model.tsx
│   │   │   ├── hooks/
│   │   │   │   └── use-toast.ts
│   │   │   ├── ui/ ...                      <-- divers elements .tsx pour l'interface web
│   │   │   └── theme-provider.tsx
│   │   ├── lib/ 
│   │   │   └── utils.ts
│   │   ├── pages/
│   │   │   └── home/
│   │   │       ├── listes/                  <-- Fourni des listes de présaisie (non limitative) pour le contexte
│   │   │       │   ├── characterClasses.tsx
│   │   │       │   ├── characterGenders.tsx
│   │   │       │   ├── characterRaces.tsx
│   │   │       │   ├── characterUniverses.tsx
│   │   │       │   └── illustrationDrawStyles.tsx
│   │   │       ├── home.tsx                                <-- Page principale
│   │   │       ├── characterRow.tsx                        <-- ligne de perso sur la page principale
│   │   │       ├── readCharacterContent.tsx                <-- Modale le visualisation du perso complet
│   │   │       ├── updateCharacterContent.tsx              <-- Modale de mise a jour du perso (cf: character-form)
│   │   │       ├── regenerateIllustrationButton.tsx        <-- Boutton mettant a jour le portrait du perso
│   │   │       ├── generatePdfButton.tsx                   <-- Boutton generant la fiche PDF du perso
│   │   │       └── deleteCharacterContent.tsx              <-- Suppression definitive d'un perso
│   │   ├── services/                       <-- C'est ici que sont fait les call vers le module 'api'
│   │   │   ├── getListCharacterFull.service.ts
│   │   │   ├── createCharacter.service.ts
│   │   │   ├── updateCharacter.service.ts
│   │   │   ├── illustrateCharacter.service.ts
│   │   │   ├── generatePdf.service.ts
│   │   │   └── deleteCharacter.service.ts
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   ├── vite-env.d.ts
│   │   ├── type.d.ts
│   │   └── global.css
│   ├── .gitignore
│   ├── .ebignore
│   ├── .env.local                          <-- VITE_API_URL=http://localhost:8080
│   ├── favicon (.ico/.png)
│   ├── nginx.conf
│   ├── vite.config.ts
│   ├── tailwind.config.ts
│   ├── postcss.config.ts
│   ├── eslint.config.js
│   ├── tsconfig.json
│   ├── tsconfig.node.json
│   ├── component.json
│   ├── package.json
│   ├── index.html
│   └── Dockerfile                          <-- Dockerfile pour le service Web
├── .documentation/ 
│     ├── ...
│     └── web/ 
│          ├── ...                          <-- divers fichiers et dossiers...
│          └── index.html                   <-- point d'entrée de la documentation technique
├── .github/
│   └── workflows/
│       ├── aws/ ...
│       ├── jenkins/ ...
│       ├── kubernetes/
│       │   ├── README.md
│       │   ├── 01-configmaps.yaml
│       │   ├── ...
│       │   ├── 05-frontend.yaml
│       │   ├── deploy-kubernetes.bat
│       │   └── helm/
│       │       └── jdr-generator-chart/
│       │           ├── .helmignore
│       │           ├── Chart.yaml
│       │           ├── values.yaml
│       │           ├── charts/
│       │           └── templates/
│       │               ├── _helpers.tpl
│       │               ├── configmaps.yaml
│       │               ├── ...
│       │               └── web.yaml
│       ├── localhost/
│       │   ├── README.md
│       │   ├── docker-push.bat          <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── docker-start-app.sh      <-- Script shell linux deployant l'app dockerisée via docker-compose.local.yml
│       │   ├── init.sql                 <-- Script sql gérant le password d'un user root pour docker-compose.local.yml
│       │   └── ...
│       └── ...
└── LICENSE                              <-- Apache License
```

### Communication Inter-modules

Le module `web` (Frontend React) communique **exclusivement** avec le module `api` (Backend Java). Le module `api` est le point d'entrée central pour toutes les requêtes du frontend et se charge d'orchestrer les appels vers les modules d'Intelligence Artificielle (`gemini`, `openai`, `freepik`) et de gérer la persistance des données via MySQL.

Les communications s'établissent comme suit :


1.  **`web` (Frontend) <-> `api` (Backend) :**
    
    * Le frontend effectue des requêtes HTTP (GET, POST, PUT, DELETE) vers l'API Java pour toutes ses opérations (générer un personnage, modifier, supprimer, lister, générer un PDF, régénérer une illustration ou des statistiques).
    * Le service comme `getListCharactersFull.service.ts` (par exemple) dans le module `web` utilise la variable d'environnement `VITE_API_URL` pour déterminer l'adresse de l'API.

    * **En développement local (via `npm run dev`) :**
        * `VITE_API_URL` est définie dans `web/.env.local` (ou `web/.env`) à `http://localhost:8080`.
        * Un proxy est configuré dans `web/vite.config.ts` pour rediriger les requêtes `/characters` du serveur de développement Vite (généralement sur `http://localhost:5173`) vers `http://localhost:8080`. Cela permet au développement local de l'application React d'intercepter les appels API `/characters` et de les rediriger vers l'API Java qui tourne potentiellement en direct sur `localhost:8080`, évitant ainsi les problèmes de CORS (Cross-Origin Resource Sharing).
        ```typescript
        // web/vite.config.ts
        export default defineConfig({
          // ...
          server: {
            proxy: {
              "/characters": {
                target: "http://localhost:8080", // Ce proxy est pour le serveur de dev seulement
                changeOrigin: true,
              },
            },
          },
        });
        ```

    * **En déploiement conteneurisé (Docker ou Kubernetes) :**
        * Le module `web` est servi par un serveur Nginx. C'est Nginx qui gère le routage des requêtes API vers le module `api` via une directive `proxy_pass`.
        * **Configuration Nginx (`nginx.conf`) :** Le fichier `nginx.conf` (qui est copié ou monté dans l'image Docker du module `web`) contient une directive `location` spécifique qui intercepte les requêtes ` /characters/` et les redirige vers le service `api-container` (ou son équivalent Kubernetes, comme `jdr-generator-api-service` ou `api-service` selon votre configuration).
            ```nginx
            # nginx.conf (extrait, à copier dans l'image Docker du module web)
            location /characters/ {
                proxy_pass http://api-container:8080/characters/;
                # ... autres headers
            }
            ```
            Ici, `api-container` est le nom du service (Docker Compose) ou du Service Kubernetes qui expose le module `api`. Il est crucial que le `nginx.conf` soit correctement intégré dans l'image Docker du `web-container` (via `COPY nginx.conf /etc/nginx/nginx.conf` dans le `Dockerfile` final ou monté via un volume dans `docker-compose.local.yml`) pour que cette redirection fonctionne en environnement conteneurisé. Le `/characters/` à la fin de `proxy_pass` est important pour conserver le chemin complet lors de la redirection.


2.  **`api` (Backend) <-> Modules AI (`gemini`, `openai`, `freepik`) :**
    
    * Le module `api` est configuré pour communiquer avec les APIs AI via leurs noms de service dans l'environnement de déploiement.
    * Ces URLs sont définies dans `api/src/main/resources/application-localdocker.yml` (pour Docker Compose) ou `api/src/main/resources/application-kubernetes.yml` (pour Kubernetes).
    * Exemple pour Kubernetes (`application-kubernetes.yml`):
        ```yaml
        # api/src/main/resources/application-kubernetes.yml
        GEMINI_API_URL: ${GEMINI_SERVICE_URL:-http://gemini-container:3001}/gemini
        OPENAI_API_URL: ${OPENAI_SERVICE_URL:-http://openai-container:3002}/openai
        FREEPIK_API_URL: ${FREEPIK_SERVICE_URL:-http://freepik-container:3003}/freepik
        ```
        * `gemini-container`, `openai-container`, `freepik-container` sont les noms des services au sein du réseau Docker Compose ou Kubernetes. Le `CharactersController.java` du module `api` fait appel aux services (`GeminiService.java`, `FreepikService.java`, etc.) qui utilisent ces URLs configurées.
        * Les CORS sont gérés au niveau du `CharactersController.java` via `@CrossOrigin(origins = webModuleHost)` pour autoriser les requêtes provenant des ports de développement et de port-forwarding de l'interface web (`http://localhost:5173`, `http://localhost:3080`).

### Services Spécifiques desservis par le backend API

L'interface web communique avec le backend pour gérer les données des personnages et déclencher les actions de génération.
La communication se fait via des requêtes HTTP et les données sont échangées au format JSON. 
La bibliothèque `axios` est utilisée pour effectuer ces requêtes, et `react-query` est utilisé pour 
gérer l'état des requêtes et mettre en cache les données.


**URL de base de l'API :** L'URL de base de l'API Java est définie dans la variable d'environnement `VITE_API_URL`. Si cette variable n'est pas définie, l'URL de repli est `http://localhost:8080`.

Voici une description détaillée des services et leurs fonctions :

- **`getListCharactersFull.service.ts` :**
  - Utilise `useQuery` pour récupérer la liste complète des personnages depuis l'API Java.
  - Effectue une requête `GET` à l'endpoint `/characters/full`.
  - Définit l'interface `CharactersResponse` pour typer la réponse de l'API.
  - Gère la mise en cache, le chargement et les erreurs grâce à `react-query`.


- **`deleteCharacter.service.ts` :**
  - Utilise `useMutation` pour supprimer un personnage via l'API Java.
  - Effectue une requête `DELETE` à l'endpoint `/characters/{id}`.
  - Gère les erreurs et affiche un message de succès en cas de réussite.


- **`createCharacter.service.ts` :**
  - Utilise `useMutation` pour créer un nouveau personnage en envoyant les informations de prompt à l'API Java.
  - Effectue une requête `POST` à l'endpoint `/characters/generate`.
  - La fonction `cleanData` est utilisée pour nettoyer les données avant de les envoyer à l'API.


- **`illustrateCharacters.service.ts` :**
  - Utilise `useMutation` pour générer une illustration pour un personnage en communiquant avec l'API NestJS.
  - Effectue une requête `POST` à l'endpoint `/characters/illustrate`.
  - Envoie le prompt d'image à l'API pour générer l'image.


- **`updateCharacter.service.ts` :**
  - Fournit une fonction `updateCharacter` pour mettre à jour les détails d'un personnage via l'API Java.
  - Effectue une requête `PUT` à l'endpoint `/characters/details/{id}`.
  - Utilise des fonctions `cleanData` et `validateData` pour nettoyer et valider les données avant de les envoyer à l'API.


- **`generatePdf.service.ts` :**
  - Utilise `useMutation` pour générer un PDF de la fiche du personnage, détaillée et illustrée via l'API Java et les données du SQL.
  - Effectue une requête `GET` à l'endpoint `/characters/pdf/generate/{id}`.
  - Le fichier PDF est alors automatiquement 'téléchargé' **ET/OU** 'ouvert' (depuis un nouvel onglet ou votre visionneuse PDF) ; selon votre browser et OS.

---

## Exécution Locale (sans Docker)

Cette section décrit comment lancer le module `web` en mode développement sur votre machine locale, sans utiliser Docker. C'est l'approche typique pour le développement frontend.

1.  **Naviguer vers le répertoire du module `web` :**
    
    Ouvrez votre terminal ou invite de commande et accédez au dossier `web` de votre projet :
    ```bash
    cd JDR-Generator/web
    ```


2.  **Installer les dépendances :**
    
    Si ce n'est pas déjà fait, installez toutes les dépendances du projet en utilisant `npm` :
    ```bash
    npm install
    ```
    Cela va lire le fichier `package.json` et télécharger toutes les bibliothèques nécessaires dans le dossier `node_modules/`.


3.  **Configurer l'URL de l'API (si nécessaire) :**
    
    Le module `web` utilise une variable d'environnement `VITE_API_URL` pour pointer vers l'API backend. 
    Pour une exécution locale sans Docker, cette API doit être accessible sur `http://localhost:8080`.
    * Créez un fichier `.env.local` (ou `.env`) à la racine du dossier `web` si ce n'est pas déjà fait.
    * Assurez-vous qu'il contient la ligne suivante :
        ```
        VITE_API_URL=http://localhost:8080
        ```
    * **Remarque importante :** Le fichier `vite.config.ts` contient une configuration de proxy pour le serveur de développement Vite. Cela signifie que toutes les requêtes du frontend vers `/characters` seront automatiquement redirigées vers `http://localhost:8080/characters` lorsque l'application est lancée avec `npm run dev`. Vous devez donc vous assurer que votre module `api` tourne bien sur ce port.


4.  **Construire l'application pour la production (build) :**
    
    Pour générer les fichiers statiques de l'application (optimisés pour la production), utilisez la commande suivante :
    ```bash
    npm run build
    ```
    Cette commande va compiler votre code TypeScript, minifier les assets, et placer les fichiers de production dans le répertoire `dist/` du module `web`. Vous pouvez prévisualiser le résultat de cette build avec `npm run preview`.


5.  **Démarrer l'application en mode développement :**
    
    Lancez le serveur de développement Vite :
    ```bash
    npm run start:dev
    ```
    L'application sera alors accessible dans votre navigateur à l'adresse `http://localhost:5173/` (le port par défaut de Vite).

---
  
## Exécution locale Docker

Pour déployer le module `web` en tant que conteneur Docker, vous pouvez l'intégrer à votre configuration Docker Compose principale (`docker-compose.local.yml`). Cela permet de lancer le frontend avec tous les services backend nécessaires (API, IA, MySQL) dans un environnement cohérent.

### Pré-requis

* Docker Desktop (incluant Docker Engine et Docker Compose) installé et fonctionnel sur votre machine.

### Fichiers Clés

* **`docker-compose.local.yml` (à la racine du projet) :** Orchestre le déploiement de tous les services, y compris le `web-container`.
* **`web/Dockerfile` :** Définit la construction de l'image Docker pour le module `web`. Il utilise Nginx pour servir l'application React buildée.
* **`web/nginx.conf` :** Contient la configuration Nginx qui permet de servir les fichiers statiques du frontend et, surtout, de rediriger les requêtes API (`/characters/`) vers le service `api-container` interne.

### Docker Compose Local

1.  **S'assurer que le service `web-container` est correctement configuré dans `docker-compose.local.yml` :**
    
    ```yml
    web-container:
        image: eli256/jdr-generator-web:latest
        build:
        context: ./web
        dockerfile: ./Dockerfile # Nom du Dockerfile dans le dossier 'web'
        args: # Ajout des arguments de build pour passer VITE_API_URL
          - 'VITE_API_URL=/'
        ports:
          - "80:80" # Mapping port hôte:port conteneur
        depends_on:
          api-container:
            condition: service_healthy # Dépend du conteneur API sain
        environment:
          VITE_API_URL: /
        volumes:
          - ./web/nginx.conf:/etc/nginx/nginx.conf:ro # Monte nginx.conf depuis ./web/ pour proxifier
        networks:
          - jdr-network
      ```
    **NOTE** : Bien que VITE_API_URL soit définie ici, c'est principalement la configuration proxy_pass dans nginx.conf qui gère la communication avec api-container lorsque le frontend est servi par Nginx. 
    La variable peut être utile pour d'autres usages ou si la build inclut des appels dynamiques qui ne passent pas par Nginx.


2.  **Construction de l'image Docker locale :**
    
    ```bash
    cd C:\<projects_repositories_path>\JDR-Generator\web
    docker build -t jdr-generator -f Dockerfile .
    ```


3.  **Exécution du Conteneur Docker Local**
    
    Démarrer le service via Docker Compose local :
    ```bash
    docker-compose -f docker-compose.local.yml up -d web-container
    ```


4.  **Accéder à l'application**
    
    Une fois les conteneurs démarrés, l'application web sera accessible via votre navigateur à l'adresse http://localhost/
    Nginx écoute sur le port 80 et sert le frontend, tout en redirigeant les appels API vers le api-container.


5.  **Arrêt et Nettoyage**
    
    - Pour arrêter et supprimer les conteneurs et les réseaux Docker créés par Docker Compose :
      ```bash
      docker-compose -f docker-compose.local.yml down
      ```
    - Pour supprimer également les volumes persistants (comme celui de MySQL) et toutes les images construites par Docker Compose :
      ```bash
      docker-compose -f docker-compose.local.yml down --volumes --rmi all
      ```

---

## Déploiement sur Kubernetes [WIP]

Le déploiement du module `web` sur Kubernetes s'inscrit dans l'architecture globale du projet JDR-Generator. Les manifestes Kubernetes et, à terme, les charts Helm, gèrent la mise en place de tous les services, y compris le frontend.

### Pré-requis

* Un cluster Kubernetes fonctionnel (par exemple, Kind, Minikube, ou un cluster cloud).
* `kubectl` installé et configuré pour interagir avec votre cluster.
* Les images Docker du module `web` (et des autres modules) doivent être disponibles dans un registre Docker accessible par votre cluster (par exemple, Docker Hub).

### Fichiers Clés

* **`.github/workflows/kubernetes/05-frontend.yaml` :** Ce manifeste YAML définit le `Deployment` et le `Service` Kubernetes pour le module `web`.
    * Le `Deployment` décrit comment le pod du frontend est créé et configuré, en s'appuyant sur l'image Docker du module `web`.
    * Le `Service` expose le port 80 du conteneur `web` au sein du cluster Kubernetes, permettant aux autres services du cluster de le contacter, et potentiellement une exposition externe.
* **`.github/workflows/kubernetes/01-configmaps.yaml` (et autres ConfigMaps) :** Contiennent les configurations des URLs des services (API, IA) que le module `api` utilisera pour communiquer, et non le `web` directement. Pour le `web` en Kubernetes, la redirection se fait toujours via Nginx vers le service interne de l'API.

### Processus de Déploiement

Le déploiement complet de l'application JDR-Generator sur Kubernetes est généralement orchestré par le script `deploy-kubernetes.bat` (situé dans `.github/workflows/kubernetes/`) qui applique l'ensemble des manifestes YAML.

1.  **Construire et Pousser les Images Docker :**

    Assurez-vous que l'image Docker de votre module `web` (et des autres modules) est construite et poussée vers un registre Docker (ex: `eli256/jdr-generator-web:latest`).
    * Exemple de commande pour pousser l'image (à adapter) :
        ```bash
        docker push eli256/jdr-generator-web:latest
        ```

2.  **Appliquer les Manifestes Kubernetes :**
    
    À partir de la racine de votre projet, vous pouvez appliquer les manifestes (ou utiliser le script `deploy-kubernetes.bat`) :
    ```bash
    # Exemple pour appliquer le manifeste du frontend
    kubectl apply -f .github/workflows/kubernetes/05-frontend.yaml
    # Ou pour tout le projet via le script
    ./.github/workflows/kubernetes/deploy-kubernetes.bat
    ```

### Accès à l'Application

Une fois le `Deployment` et le `Service` du frontend créés, vous pouvez y accéder :

1.  **Via `kubectl port-forward` (pour le développement local) :**
    
    C'est la méthode la plus courante en développement pour exposer un service Kubernetes sur votre machine locale. Le fichier `conversation.txt` mentionne l'utilisation de cette commande :
    ```bash
    kubectl port-forward svc/web-container 3080:80
    ```
    Cette commande redirige le trafic du port 3080 de votre machine locale vers le port 80 du service Kubernetes nommé `web-container`.
    L'application sera alors accessible dans votre navigateur à :
    ```
    http://localhost:3080/
    ```
    **Note importante :** Le service `web-container` (Nginx) gère la redirection des requêtes API vers le service `jdr-generator-api-service` (ou `api-service` selon votre `application-kubernetes.yml` dans le module `api`) au sein du cluster Kubernetes. Vous n'avez pas besoin de configurer `VITE_API_URL` spécifiquement pour Kubernetes au niveau du frontend, car Nginx s'en charge via son `proxy_pass`.

--- 

## Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
