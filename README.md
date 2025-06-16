# JDR-Generator

NB : Branches tests pour acceder aux tests SCALA :
> Exemple : [https://github.com/NeoRyu/JDR-Generator/blob/tests/api/src/main/scala/jdr/generator/api/scala/tools/TypescriptOperators.scala](https://github.com/NeoRyu/JDR-Generator/blob/main/api/src/main/scala/jdr/generator/api/scala/tools/TypescriptOperators.scala)


## Description

JDR-Generator est une application complète pour la création de personnages de jeux de rôle (JDR). Ce projet est structuré comme un monorepo et est conçu pour fonctionner en local. Il comprend :

* **Backend Java :** Une API backend développée en Java (avec une intégration de Scala à titre d'exercice) qui gère la communication avec une base de données MySQL. Les migrations de la base de données sont gérées avec FlywayDB.
* **Backend NestJS :** Une API backend développée avec NestJS, dédiée à la communication avec les APIs de Google Gemini et OpenAI pour la génération de texte et d'images.
* **Frontend React/TypeScript :** Une interface utilisateur web développée en React et TypeScript, permettant aux utilisateurs de définir le contexte de création de personnage et de visualiser les résultats générés par les APIs.

## Caractéristiques Principales

* **Génération Automatisée de Personnages :** Création rapide de personnages pour divers systèmes de JDR.
* **Interface Interactive :** Définition aisée du contexte de création (race, classe, etc.) et visualisation claire des personnages générés.
* **Support Multi-Systèmes :** Adaptable à différents univers et règles de JDR.
* **Intégration d'IA :** Utilisation de Google Gemini et OpenAI pour la génération de texte descriptif et d'illustrations de personnages.
* **Architecture Monorepo :** Gestion unifiée du code pour les différentes parties du projet, facilitant le développement et la maintenance.

## Technologies Utilisées

* Java
* Scala
* NestJS (TypeScript)
* React (TypeScript)
* MySQL
* FlywayDB
* Google Gemini API (gemini / imagen)
* OpenAI API (chatgpt / dall-e)
* Freepik API (flux-dev)
* Maven / NPM
* Docker / Docker Hub
* Github Actions
* Jenkins
* Kubernetes
* ...

## Pré-requis

* Github
* Node.js
* JDK 11
* MySQL Workbench
* npm ou yarn
* Docker Desktop
* (activation de kubernetes kind)
* GIT Bash

## Notes Importantes

* **Variables d'environnement :** La configuration correcte des variables d'environnement est cruciale pour le bon fonctionnement du projet. Consultez les fichiers `.env.example` (ou équivalents) dans chaque répertoire pour connaître les variables requises.
* **Dépendances Java :** Assurez-vous d'avoir un JDK et un outil de build Java (Maven ou Gradle) correctement installés et configurés.
* **Conflits de ports :** Les différentes parties du projet (API Java, API NestJS, Web) peuvent utiliser des ports différents. Si vous rencontrez des conflits, vous devrez peut-être modifier les configurations de port.
* **Documentation Supplémentaire :** Chaque module (api, gemini, web) a son propre README avec des instructions plus détaillées.
* **Scripts NestJS :** Les commandes `clean`, `build` et `start` pour l'API NestJS font référence aux scripts définis dans le fichier `package.json` du répertoire `gemini`.
* **Fichier Docker Compose :** Ce projet utilise un fichier `docker-compose.local.yml` permettant une configuration optimisée pour l'exécution locale.

## Installation

1.  **Cloner le dépôt :**

    ```bash
    git clone [https://github.com/NeoRyu/JDR-Generator.git](https://github.com/NeoRyu/JDR-Generator.git)
    cd JDR-Generator
    ```

2.  **Configurer les variables d'environnement :**

    * Vous devrez configurer les variables d'environnement spécifiques à chaque partie du projet (API Java, API NestJS, Web). Des exemples de fichiers `.env.example` devraient être fournis dans chaque répertoire si possible.
    * Les variables d'environnement incluent généralement les clés d'API (Google Gemini et Imagen, Freepik Flux-dev, OpenAI Dall-E & ChatGPT), les informations de connexion à la base de données MySQL, et d'autres paramètres de configuration.

3.  **Installer les dépendances des API NestJS :**

    ```bash
    cd gemini
    npm install
    cd openai
    npm install
    cd freepik
    npm install
    ```

4.  **Installer les dépendances de l'interface Web (React) :**

    ```bash
    cd web
    npm install
    ```

5.  **Installer les dépendances de l'API Java :**

    * L'installation des dépendances Java se fait généralement via un outil de gestion de dépendances comme Maven ou Gradle. Assurez-vous d'avoir l'outil approprié installé et configurez le projet pour télécharger les dépendances. (Si vous utilisez IntelliJ IDEA, il gérera souvent cela automatiquement).
   * Si vous utilisez Maven, par exemple, vous pouvez construire le projet avec :

       ```bash
       cd api
       mvn clean install
       ```

## Exécution du Projet

1.  **Démarrer la base de données MySQL :**

    * Assurez-vous que votre serveur MySQL est en cours d'exécution.

2.  **Exécuter l'API NestJS (Gemini) :**

    * **Nettoyer le dossier de distribution :**

        ```bash
        cd gemini
        npm run clean # Utilise la commande "clean" définie dans package.json (rd /s /q dist)
        ```

    * **Compiler le code TypeScript :**

        ```bash
        npm run build # Utilise la commande "build" définie dans package.json (tsc)
        ```

    * **Démarrer le serveur :**

        ```bash
        npm run start # Utilise la commande "start" définie dans package.json (node dist/app.js)
        ```

      Ou pour le mode développement avec auto-rechargement :

        ```bash
        npm run start:dev
        ```

3.  **Exécuter l'API Java :**

    * Si vous utilisez un IDE comme IntelliJ IDEA, vous pouvez exécuter la classe principale de votre application Java directement depuis l'IDE.
    * Sinon, si vous avez construit avec Maven, vous pouvez exécuter l'application avec :

        ```bash
        cd api
        java -jar target/api-0.0.1-SNAPSHOT.jar 
        ```

4.  **Exécuter l'interface Web (React) :**

    ```bash
    cd web
    npm run start
    ```

    Ou pour le mode développement :

    ```bash
    cd web
    npm run start:dev
    ```

## Captures d'écran

![{F0A5CDFB-8C59-43A8-BB03-C3DF31FAEA74}](https://github.com/user-attachments/assets/f8825340-3e43-4de8-95ae-05641253d000)
![{8857CA56-EF92-4868-B4CB-4E9C6E2CEC95}](https://github.com/user-attachments/assets/f3972e04-553b-4e44-ac17-6f5fbbb517d4)
![{A8D02D44-A30A-41B4-A562-6219D38B19C1}](https://github.com/user-attachments/assets/78b3df51-f65c-483c-9311-a40c00f8344c)
![{E8893894-CADA-4F5E-96FE-9C47959FE2E9}](https://github.com/user-attachments/assets/58a7e538-0037-4ea4-93eb-a61fd597e1c1)
![{CE9E8BF3-B49E-4F1D-9233-77F1BD3D2E04}](https://github.com/user-attachments/assets/9781b1b9-d458-491f-9edd-7d566c5b3536)
![{20240EB7-0A44-45C2-9244-2EBCF16F60B5}](https://github.com/user-attachments/assets/35336cfb-e2b8-445d-905a-cbe03c6d761a)
![{98F73FCB-130C-4BEA-82B7-C5D0ABEC9266}](https://github.com/user-attachments/assets/254f9fe1-bb5f-4dda-a683-226c26a7d452)
![{90DCBF62-C6FC-4F1F-AEFD-F95E6F257B82}](https://github.com/user-attachments/assets/e1bfb0bd-8f70-4b66-b7b3-dda68f52739a)

Et voici un exemple de fiche de profil PDF pouvant être générée depuis l'application :
[JDR-Generator - Mona Sinclair.pdf](https://github.com/user-attachments/files/20611246/JDR-Generator.-.Mona.Sinclair.pdf)
![{62A7A714-B53A-4C73-980C-8932EFAD81C9}](https://github.com/user-attachments/assets/4b79b567-72de-4d4e-ad97-dcd0eef29e8b)

## Structure du Projet

```
JDR-Generator/
├── README.md                         <-- VOUS ETES ICI
├── .env
├── .gitignore
├── docker-compose.local.yml           <-- Fichier Docker Compose principal
├── JDR-Generator.iml
├── api/
│   ├── README.md
│   ├── Dockerfile                     <-- Dockerfile pour le service API
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── scala/
│       │   │   └── ...                <-- Inutile actuellement, permettra plus tard un decoupage
│       │   └── java/
│       │       ├── characters/
│       │       │   ├── context/
│       │       │   │   ├── CharacterContextEntity.java
│       │       │   │   ├── CharacterContextModel.java
│       │       │   │   ├── CharacterContextRepository.java
│       │       │   │   ├── CharacterContextService.java
│       │       │   │   ├── CharacterContextServiceImpl.java
│       │       │   │   ├── CharacterContextJson.java
│       │       │   │   └── IllustrationDrawStyle.java     <-- Enum des prompts de style de portrait
│       │       │   ├── details/
│       │       │   │   ├── CharacterDetailsEntity.java
│       │       │   │   ├── CharacterDetailsModel.java
│       │       │   │   ├── CharacterDetailsNotFoundException.java
│       │       │   │   ├── CharacterDetailsRepository.java
│       │       │   │   ├── CharacterDetailsService.java
│       │       │   │   ├── CharacterDetailsServiceImpl.java
│       │       │   │   ├── CharacterBooleanDeserializer.java
│       │       │   │   └── CharacterIntegerDeserializer.java
│       │       │   ├── illustration/
│       │       │   │   ├── CharacterIllustrationEntity.java
│       │       │   │   ├── CharacterIllustrationModel.java
│       │       │   │   ├── CharacterIllustrationRepository.java
│       │       │   │   ├── CharacterIllustrationService.java
│       │       │   │   ├── CharacterIllustrationServiceImpl.java
│       │       │   │   └── RegenerateIllustrationRequestDto.java  <-- payload pour regen d'image
│       │       │   ├── stats/
│       │       │   │   ├── CharacterJsonDataEntity.java
│       │       │   │   ├── CharacterJsonDataModel.java
│       │       │   │   ├── CharacterJsonDataRepository.java
│       │       │   │   ├── CharacterJsonDataService.java
│       │       │   │   └── CharacterJsonDataServiceImpl.java
│       │       │   ├── CharacterFullModel.java
│       │       │   ├── CharacterController.java                <-- controlleur principal
│       │       │   ├── FreepikService.java                     <-- utilisé pour illustrate
│       │       │   ├── GeminiService.java                      <-- utilisé pour generate et stats
│       │       │   ├── OpenaiService.java                      <-- inutilisé actuellement
│       │       │   └── PdfGeneratorService.java                <-- service gérant la création PDF
│       │       ├── config/
│       │       │   ├── CorsConfig.java
│       │       │   ├── GeminiGenerationConfiguration.java
│       │       │   ├── ModelMapperConfig.java
│       │       │   └── RestTemplateConfig.java
│       │       ├── tools/
│       │       │   └── m0v.py          <-- Petit script python evitant la mise en veille de l'ecran
│       │       ├── ApiApplication.java                         <-- classe principale
│       │       ├── FlywayDatabaseConfig.java                   <-- creation de la base de donnée
│       │       ├── InvalidContextException.java
│       │       └── RestPreconditions.java 
│       └── resources/
│           ├── db/
│           │   └── migration/                                  <-- fichiers SQL pour FlywayDB
│           │       ├── V00001__initial_schema.sql
│           │       └── ...
│           ├── banner.txt
│           ├── application.yml                     <-- pour le build localhost de l'application
│           └── application-localdocker.yml         <-- pour le build de l'image docker en localhost
├── freepik/
│   ├── dist/
│   ├── node_modules/
│   ├── src/
│   │   └── controllers/
│   │       ├── illustration.ts             <-- Utilisé par défault. 0.01 € l'image (100 gratuites).
│   ├── .ebignore
│   ├── .env                                <-- Mettre votre clef d'API ici
│   ├── .gitignore
│   ├── app.ts
│   ├── Dockerfile                          <-- Dockerfile pour le service Freepik
│   ├── eslint.config.js
│   ├── package.json
│   ├── package-lock.json
│   ├── README.md
│   └── tsconfig.json
├── gemini/
│   ├── dist/
│   ├── node_modules/
│   ├── src/
│   │   └── controllers/
│   │       ├── background.ts               <-- Gratuit. Utilisé par défault
│   │       ├── illustration.ts             <-- Devenu inutilisable en Europe, sinon gratuit.
│   │       └── statistiques.ts             <-- Gratuit. Utilisé par défault
│   ├── .ebignore
│   ├── .env                                <-- Mettre votre clef d'API ici
│   ├── .gitignore
│   ├── app.ts
│   ├── Dockerfile                          <-- Dockerfile pour le service Gemini
│   ├── eslint.config.js
│   ├── package.json
│   ├── package-lock.json
│   ├── README.md
│   └── tsconfig.json
├── openai/
│   ├── dist/
│   ├── node_modules/
│   ├── src/
│   │   └── controllers/
│   │       ├── background.ts               <-- Payant, prix prohibitif...
│   │       ├── illustration.ts             <-- Payant, prix prohibitif...
│   │       └── statistiques.ts             <-- Payant, prix prohibitif...
│   ├── .ebignore
│   ├── .env                                <-- Mettre votre clef d'API ici
│   ├── .gitignore
│   ├── app.ts
│   ├── Dockerfile                          <-- Dockerfile pour le service OpenAI
│   ├── eslint.config.js
│   ├── package.json
│   ├── package-lock.json
│   ├── README.md
│   └── tsconfig.json
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
│   │   │   ├── ui
│   │   │   │   └── ...                      <-- divers elements .tsx pour l'interface web
│   │   │   └── theme-provider.tsx
│   │   ├── lib/ 
│   │   │   └── utils.ts
│   │   ├── pages/ (home/)
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
│   │   │       └── deleteCharacterContent.tsx              <-- Suppression definitive d'un perso
│   │   ├── services/                       <-- C'est ici que sont fait les call vers le module 'api'
│   │   │   ├── getListCharacterFull.service.ts
│   │   │   ├── createCharacter.service.ts
│   │   │   ├── updateCharacter.service.ts
│   │   │   ├── illustrateCharacter.service.ts
│   │   │   └── deleteCharacter.service.ts
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── vite-env.d.ts
│   ├── vite.config.ts
│   ├── tailwind.config.ts
│   ├── postcss.config.ts
│   ├── tsconfig.json
│   ├── package.json
│   ├── index.html
│   ├── Dockerfile                          <-- Dockerfile pour le service Web
│   └── component.json
├── .github/
│   └── workflows/
│       ├── aws/                         <-- TODO : non fonctionnel, laissé pour y regarder plus tard...
│       │    ├── docker-compose.yml
│       │    ├── Dockerrun.aws.json
│       │    ├── deploy-aws.yml
│       │    └── aws.zip
│       ├── jenkins/                     <-- workflows pour controlleur Jenkins dockerisé avec agent
│       │   ├── README.md
│       │   ├── agent
│       │   │   └── Dockerfile             <-- 1. docker build -t eli256/jenkins-docker-image-agent:latest .
│       │   ├── code-quality
│       │   │   ├── Jenjenkins_logs.txt
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   ├── database-export
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   └── Dockerfile                 <-- 2. docker build -t eli256/jenkins-docker-image .
│       ├── kubernetes/
│       │   ├── README.md
│       │   ├── 00-secrets.yaml
│       │   ├── 01-configmaps.yaml
│       │   ├── 02-mysql.yaml
│       │   ├── 03-api.yaml
│       │   ├── 04-ai-modules.yaml
│       │   ├── 05-frontend.yaml
│       │   └── deploy-kubernetes.bat
│       ├── localhost/
│       │   ├── README.md
│       │   ├── docker-push.bat          <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── docker-start-app.sh      <-- Script shell linux deployant l'app dockerisée via docker-compose.local.yml
│       │   ├── init.sql                 <-- Script sql gérant le password d'un user root pour docker-compose.local.yml
│       │   └── ...
│       ├── README.md
│       ├── code-quality.yml             <-- Script Gituhub Actions : Gestion de la qualité de code (modifiez le pom.xml du module api en : <skip.quality.code>false</skip.quality.code> )
│       ├── docker-push.yml              <-- Script Gituhub Actions : Si des changes ont eu lieu dans l'un des modules, va builder, créer une image docker et la deployer sur Docker Hub
│       └── qodana_code_quality.yml      <-- Script Gituhub Actions : Permet de consulter la santé et qualité du code de l'app
└── LICENSE                              <-- Apache License
```

___________

## Exécution Locale avec Docker et Docker Compose

L'application JDR-Generator est conçue pour être facilement exécutée en environnement de développement local grâce à **Docker** et **Docker Compose**. Cette méthode permet de lancer rapidement tous les services nécessaires (API, modules d'IA, frontend, base de données MySQL) dans des conteneurs isolés et préconfigurés, garantissant une cohérence de l'environnement de travail.

Cette section fournit un bref aperçu de l'utilisation de Docker pour le développement local. Pour des instructions complètes sur les **prérequis**, les **commandes de démarrage**, la **gestion des services**, les **scripts utilitaires locaux** et le **dépannage**, veuillez consulter la documentation dédiée :

➡️ **[Documentation Complète pour Docker Local](./.github/workflows/localhost/README.md)**

### Vue d'ensemble rapide

* **`docker-compose.local.yml`** : Fichier principal définissant tous les services Docker de l'application (API, Frontend, Gemini, OpenAI, Freepik, MySQL).
* **Images Docker** : Les services sont conteneurisés et peuvent être construits localement ou tirés d'un registre Docker (par exemple, Docker Hub).
* **Scripts Locaux** : Le dossier `.github/workflows/localhost/` contient des scripts utiles pour le démarrage (`docker-start-app.sh`), le push d'images (`docker-push.bat`), et l'initialisation de la base de données (`init.sql`).

**Pour démarrer l'application localement avec Docker Compose :**

Assurez-vous d'avoir Docker Desktop (ou un environnement Docker compatible) installé et en cours d'exécution.

1.  **Depuis la racine du projet**, construisez et démarrez tous les services :
    ```bash
    docker-compose -p jdr-generator -f docker-compose.local.yml up --build -d
    ```
    (Le `-d` pour exécuter en arrière-plan peut être omis si vous voulez voir les logs directement.)

2.  Une fois les conteneurs démarrés, l'application sera accessible via `http://localhost:3000` (pour le frontend) et `http://localhost:8080` (pour l'API, si configuré ainsi via docker-compose).

Pour toutes les informations détaillées sur l'installation, les différentes commandes, la persistance des données MySQL et le dépannage, **référez-vous au [README du dossier `localhost/`](./.github/workflows/localhost/README.md)**.

___________

## Intégration Continue / Déploiement Continu (CI/CD) avec GitHub Actions

Ce projet exploite **GitHub Actions** pour automatiser les processus d'intégration continue et de déploiement continu. Cela inclut la vérification de la qualité du code et la publication des images Docker vers Docker Hub, garantissant ainsi un cycle de développement efficace et automatisé.

Cette section offre un aperçu de nos workflows GitHub Actions. Pour des informations complètes sur la **configuration des workflows**, les **détails des vérifications de qualité de code**, et le **processus de déploiement des images Docker**, veuillez consulter la documentation dédiée :

➡️ **[Documentation Complète de GitHub Actions](./.github/workflows/README.md)** (ou `./github-actions/README.md` si vous créez un nouveau dossier pour ce README)

### Vue d'ensemble rapide

Nous utilisons principalement deux types de workflows :

* **Vérification de la Qualité du Code** (`.github/workflows/code-quality.yml`) :
    * S'exécute à chaque *push* et *pull request* sur toutes les branches.
    * Effectue des vérifications de linting, compilation, et formatage pour les modules Node.js et Java.
    * Les résultats sont consultables directement dans l'interface GitHub Actions.

* **Déploiement Continu avec Docker** (`.github/workflows/docker-push.yml`) :
    * Construit et pousse les images Docker vers Docker Hub.
    * Déclenché par les *push* sur les branches spécifiées (ex: `githubactions`, `main`), ou manuellement.
    * Optimisé pour ne construire et pousser que les images dont les dossiers source ont été modifiés.
    * Les images sont taguées avec le SHA du commit et `latest`, puis poussées vers [eli256 sur Docker Hub](https://hub.docker.com/repositories/eli256).

Ces workflows garantissent une validation rapide du code et une mise à jour efficace des images Docker, même si les clés API pour les services d'IA sont gérées séparément pour des raisons de sécurité.

___________

## Intégration Continue avec Jenkins via Docker

Ce projet utilise Jenkins pour l'intégration continue (CI) et la qualité du code, notamment via des pipelines définis dans des `Jenkinsfile`. Jenkins est déployé en tant que conteneur Docker.

Cette section offre un aperçu de l'intégration Jenkins. Pour des instructions détaillées sur l'**installation et la configuration de Jenkins avec Docker**, la gestion des **Personal Access Tokens (PAT) GitHub**, la **création et la gestion des pipelines**, veuillez consulter la documentation dédiée :

➡️ **[Documentation Complète de Jenkins](./.github/workflows/jenkins/README.md)**

### Vue d'ensemble rapide

Le répertoire `.github/workflows/jenkins/` contient les éléments clés pour l'intégration Jenkins :

* **`Dockerfile`** (dans le dossier `jenkins/` et `jenkins/agent/`) : Utilisés pour construire les images Docker personnalisées de Jenkins et de son agent, incluant les outils nécessaires (comme Docker client pour les "Docker-out-of-Docker").
* **`Jenkinsfile`** (dans `code-quality/` et `database-export/`) : Fichiers définissant les pipelines de CI/CD (qualité du code, exportation de base de données, etc.).
* **`agent/Dockerfile`**: Dockerfile pour l'image de l'agent Jenkins.

**Pour mettre en place et utiliser Jenkins :**

1.  **Construisez les images Docker** de Jenkins et de son agent.
2.  **Lancez le conteneur Jenkins** en mappant les ports nécessaires et en montant le volume de persistance pour les données de Jenkins.
3.  Accédez à l'interface web de Jenkins (`http://localhost:8080/`) et suivez les étapes de déverrouillage et de configuration initiale.
4.  Configurez un **Personal Access Token (PAT) GitHub** dans Jenkins pour permettre l'accès à votre dépôt.
5.  Créez les pipelines Jenkins en important les `Jenkinsfile` depuis le dépôt.

Pour toutes les informations approfondies, y compris le dépannage de l'installation et la gestion des jobs, **référez-vous au [README du dossier `jenkins/`](./.github/workflows/jenkins/README.md)**.

___________

## Déploiement sur Kubernetes

L'application JDR-Generator peut être déployée sur un **cluster Kubernetes** pour bénéficier d'une gestion conteneurisée et d'une meilleure scalabilité. L'ensemble des configurations (fichiers YAML) et des scripts de déploiement dédiés à Kubernetes se trouvent dans le répertoire `kubernetes/` de ce projet.

Cette section fournit un aperçu rapide du processus. Pour obtenir des instructions détaillées sur les **prérequis**, l'**installation et la configuration de `kubectl`**, le **processus de déploiement pas à pas**, l'accès à l'application, la consultation des logs, et la communication inter-services, veuillez consulter la documentation dédiée :

➡️ **[Documentation Complète du Déploiement Kubernetes](./kubernetes/README.md)**

### Vue d'ensemble rapide

Le déploiement sur Kubernetes utilise les composants suivants, définis dans les fichiers YAML du répertoire `kubernetes/` :

* **`00-secrets.yaml`** : Pour la gestion sécurisée des clés API et des identifiants de base de données.
* **`01-configmaps.yaml`** : Pour la configuration non-sensible des services.
* **`02-mysql.yaml`** : Déploiement de la base de données MySQL avec persistance.
* **`03-api.yaml`** : Déploiement de l'API Backend (Java).
* **`04-ai-modules.yaml`** : Déploiement des services d'IA (Gemini, OpenAI, Freepik).
* **`05-frontend.yaml`** : Déploiement de l'interface utilisateur web (React/TypeScript).
* **`deploy-kubernetes.bat`** : Un script qui automatise l'application de toutes ces configurations.

**Pour lancer le déploiement :**

1.  Assurez-vous d'avoir un cluster Kubernetes fonctionnel et `kubectl` correctement configuré.
2.  Assurez-vous que vos images Docker sont poussées vers un registre accessible par votre cluster (voir les instructions dans le README détaillé).
3.  Exécutez le script de déploiement **depuis la racine de votre projet** :
    ```bash
    ./kubernetes/deploy-kubernetes.bat
    ```

Pour toutes les informations approfondies et les étapes de dépannage, **référez-vous au [README du dossier `kubernetes/`](./kubernetes/README.md)**.

___________
# Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
