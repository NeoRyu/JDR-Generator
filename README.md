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
* ...

## Pré-requis

* Github
* Node.js
* JDK 11
* MySQL Workbench
* npm ou yarn
* Docker Desktop
* GIT Bash

## Notes Importantes

* **Variables d'environnement :** La configuration correcte des variables d'environnement est cruciale pour le bon fonctionnement du projet. Consultez les fichiers `.env.example` (ou équivalents) dans chaque répertoire pour connaître les variables requises.
* **Dépendances Java :** Assurez-vous d'avoir un JDK et un outil de build Java (Maven ou Gradle) correctement installés et configurés.
* **Conflits de ports :** Les différentes parties du projet (API Java, API NestJS, Web) peuvent utiliser des ports différents. Si vous rencontrez des conflits, vous devrez peut-être modifier les configurations de port.
* **Documentation Supplémentaire :** Chaque module (api, gemini, web) a son propre README avec des instructions plus détaillées.
* **Scripts NestJS :** Les commandes `clean`, `build` et `start` pour l'API NestJS font référence aux scripts définis dans le fichier `package.json` du répertoire `gemini`.
* **Fichiers Docker Compose :** Ce projet utilise deux fichiers `docker-compose` distincts :
    * `docker-compose.yml` : Configuration pour le déploiement sur AWS Elastic Beanstalk.
    * `docker-compose.local.yml` : Configuration optimisée pour l'exécution locale.

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

## Structure du Projet

```
JDR-Generator/
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
│       │   ├── agent
│       │   │   └── Dockerfile             <-- 1. docker build -t eli256/jenkins-docker-image-agent:latest .
│       │   ├── code-quality
│       │   │   ├── Jenjenkins_logs.txt
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   ├── database-export
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   └── Dockerfile                 <-- 2. docker build -t eli256/jenkins-docker-image .
│       ├── localhost/
│       │   ├── docker-push.bat          <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── docker-start-app.sh      <-- Script shell linux deployant l'app dockerisée via docker-compose.local.yml
│       │   ├── init.sql                 <-- Script sql gérant le password d'un user root pour docker-compose.local.yml
│       │   └── ...
│       ├── code-quality.yml             <-- Script Gituhub Actions : Gestion de la qualité de code (modifiez le pom.xml du module api en : <skip.quality.code>false</skip.quality.code> )
│       ├── docker-push.yml              <-- Script Gituhub Actions : Si des changes ont eu lieu dans l'un des modules, va builder, créer une image docker et la deployer sur Docker Hub
│       └── qodana_code_quality.yml      <-- Script Gituhub Actions : Permet de consulter la santé et qualité du code de l'app
└── LICENSE                              <-- Apache License
```


## Utilisation de Docker

Ce projet peut être déployé et exécuté à l'aide de Docker et Docker Compose. 
Cela simplifie la configuration de l'environnement et assure une cohérence entre les différents déploiements.


### Configuration requise du démon Docker sur l'hôte (pour les utilisateurs Windows/Docker Desktop)

Si vous utilisez Docker Desktop sur Windows et que vous rencontrez des erreurs de "permission denied" lors de l'exécution de pipelines Jenkins 
qui tentent d'interagir avec Docker (par exemple, pour exporter des bases de données ou construire des images), cela est généralement dû au fait 
que le socket Docker (`/var/run/docker.sock`) n'a pas les permissions de groupe adéquates par défaut.

Pour résoudre ce problème de manière persistante, vous devez configurer le démon Docker via l'interface de Docker Desktop :

1.  **Ouvrez Docker Desktop.**
2.  Cliquez sur l'icône **Paramètres** (la roue dentée) en haut à droite.
3.  Dans le menu de gauche, sélectionnez **"Docker Engine"**.
4.  Dans l'éditeur de texte JSON qui apparaît, **ajoutez ou assurez-vous que la ligne `"group": "docker"` est présente** au sein de l'objet JSON principal. Votre configuration pourrait ressembler à ceci :

    ```json
    {
      "group": "docker",
      
      "builder": {
        "gc": {
          "defaultKeepStorage": "20GB",
          "enabled": true
        }
      },
      "experimental": false
    }
    ```
    Assurez-vous que la syntaxe JSON est correcte (les virgules entre les éléments sont importantes). Laissez les autres configurations si elles existent. Comme dans l'exemple ci-dessus.

5.  Cliquez sur le bouton **"Apply & Restart"** (Appliquer et Redémarrer) en bas à droite de la fenêtre des paramètres.

Docker Desktop redémarrera son moteur, et le socket `/var/run/docker.sock` aura désormais le groupe `docker` comme propriétaire, permettant à votre agent Jenkins (configuré avec `--group-add 999`) d'y accéder.

**Pour vérifier (facultatif, mais recommandé après redémarrage) :**
Ouvrez le terminal de votre distribution WSL2 (ex: Ubuntu) et exécutez :
```bash
ls -l /var/run/docker.sock
```
Le résultat devrait maintenant ressembler à ```srw-rw---- 1 root docker ... /var/run/docker.sock.```



**Analyse des Images Docker :**

* Le module **web** utilise un processus de build en deux étapes :
    * Une première image Node.js est utilisée pour builder l'application React avec Vite.
    * Les fichiers statiques buildés sont ensuite copiés dans une image Nginx, qui sert l'application web.
* Les modules **gemini**, **openai** et **freepik** utilisent des images Node.js pour exécuter leurs applications NestJS (TypeScript) après la compilation.
* Le module **api** utilise une image Maven pour builder l'application Java (qui inclut également du code Scala). L'image finale pour l'exécution sera une image JRE (Java Runtime Environment).

**Pré-requis :**

* Docker
* Docker Compose

**Configuration :**

* Assurez-vous d'avoir configuré correctement les variables d'environnement (comme mentionné dans la section "Installation") car elles peuvent être nécessaires dans les conteneurs Docker.

**Commandes Docker Utiles :**

**Note importante :** Adaptez les chemins d'accès aux répertoires si nécessaire. Les exemples ci-dessous concernent l'exécution locale et utilisent le fichier `docker-compose.local.yml`. Pour le déploiement sur AWS, référez-vous à la documentation Elastic Beanstalk concernant l'upload du fichier `docker-compose.yml`.

### 1. Construction et Démarrage des Images Docker

Cette commande construit toutes les images Docker définies dans le fichier `docker-compose.local.yml` et les démarre en mode détaché (-d). Elle supprime également les conteneurs et les volumes existants pour repartir d'une base propre.

```bash
cd C:\<projects_repositories_path>\JDR-Generator\.github\workflows
clear
docker-compose -f docker-compose.local.yml down # Arrête et supprime les conteneurs locaux
docker volume rm workflows_mysql-data # Supprime le volume MySQL local (attention : perd les données !)
docker-compose -f docker-compose.local.yml up --build -d # Construit les images et démarre les conteneurs locaux
```

Attention : La suppression du volume workflows_mysql-data entraîne la perte de toutes les données de la base de données locale. Utilisez cette commande avec précaution.

### 2. API Backend Java

* **Construction de l'image Docker :**

```bash
cd C:\<projects_repositories_path>\JDR-Generator\api
mvn clean install package # Compile et package l'application Java
cd C:\<projects_repositories_path>\JDR-Generator\.github\workflows
docker-compose -f docker-compose.local.yml build --no-cache api-container # Construit l'image Docker locale pour l'API Java
```

* **Démarrage du conteneur :**

```bash
docker-compose -f docker-compose.local.yml up -d api-container # Démarre le conteneur local de l'API Java
```

* **Affichage des logs :**

```bash
clear
docker-compose -f docker-compose.local.yml logs api-container # Affiche les logs du conteneur local de l'API Java
```

* **Accès au shell du conteneur :**

```bash
docker-compose -f docker-compose.local.yml exec api-container sh # Ouvre un shell dans le conteneur local
env # Affiche les variables d'environnement
exit # Quitte le shell
```

### 3. Gemini API

* **Construction de l'image Docker :**

```bash
docker-compose -f docker-compose.local.yml build --no-cache gemini-container # Construit l'image Docker locale pour l'API Gemini
```

* **Démarrage du conteneur :**

```bash
docker-compose -f docker-compose.local.yml up -d gemini-container # Démarre le conteneur local de l'API Gemini
```

* **Affichage des logs :**

```bash
clear
docker-compose -f docker-compose.local.yml logs gemini-container # Affiche les logs du conteneur local de l'API Gemini
```

### 4. Open AI API

* **Construction de l'image Docker :**

```bash
docker-compose -f docker-compose.local.yml build --no-cache openai-container # Construit l'image Docker locale pour l'API OpenAI
```

* **Démarrage du conteneur :**

```bash
docker-compose -f docker-compose.local.yml up -d openai-container # Démarre le conteneur local de l'API OpenAI
```

* **Affichage des logs :**

```bash
clear
docker-compose -f docker-compose.local.yml logs openai-container # Affiche les logs du conteneur local de l'API OpenAI
```

* **Exécution de commandes dans le conteneur et appel à l'API OpenAI :**

```bash
cd C:\<projects_repositories_path>\JDR-Generator\openai
docker exec -it workflows-openai-container-1 sh # Ouvre un shell interactif dans le conteneur local
apk update # Met à jour la liste des paquets (peut être nécessaire)
apk add curl --no-cache # Installe curl (outil de ligne de commande pour faire des requêtes HTTP)

# Exemple d'appel à l'API OpenAI pour générer une image (DALL-E)
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_API_KEY" # Remplacez YOUR_API_KEY par votre clé API OpenAI
  -H "OpenAI-Version: 2020-10-01" \
  -H "OpenAI-Organization: YOUR_ORG_ID" # Remplacez YOUR_ORG_ID par votre ID d'organisation OpenAI (si applicable)
  -d '{
	"model": "dall-e-2",
	"prompt": "A simple red cube.",
	"n": 1,
	"response_format": "b64_json"
  }' \
  [https://api.openai.com/v1/images/generations](https://api.openai.com/v1/images/generations)
```

**Important :** Remplacez `YOUR_API_KEY` et `YOUR_ORG_ID` par vos propres informations d'identification OpenAI.

**Mini-Tutoriel : Configuration OpenAI**

Pour utiliser correctement l'API OpenAI, suivez ces étapes :

1.  **Créez un compte OpenAI :** Rendez-vous sur [https://platform.openai.com/signup](https://platform.openai.com/signup) et créez un compte.
2.  **Accédez aux paramètres de l'organisation :** Une fois connecté, allez dans les paramètres de votre compte et sélectionnez "Organization" (voir l'image {EC98F7BA-3A74-42F6-8D7C-FDB32F1696F0}.png).
3.  **Récupérez votre Organization ID :** Copiez votre "Organization ID" (par exemple, `org-2EMhFJ8w5cK51s0L9dQgxMk`). Vous en aurez besoin si votre code l'exige.
4.  **Générez une clé API :** Dans les paramètres de votre compte, allez dans la section "API keys" et créez une nouvelle clé API. Copiez cette clé précieusement, car vous ne pourrez pas la revoir après sa création.
5.  **Vérifiez vos limites d'utilisation :** Consultez la section "Usage" pour comprendre vos limites d'utilisation et mettre en place des alertes ou des limites de dépenses si nécessaire (voir l'image {6E7B2422-3B57-4E67-BB4C-DBDE3D9BD638}.png).
6.  **Configurez les variables d'environnement :** Dans votre projet, assurez-vous d'avoir un fichier `.env` ou une méthode similaire pour stocker vos informations sensibles. Ajoutez-y votre clé API OpenAI et votre Organization ID (si nécessaire). Exemple :
```
API_KEY=sk-your-openai-api-key
ORG_ID=org-your-organization-id
```

7.  **Sécurisez votre clé API :** Ne partagez jamais votre clé API publiquement (par exemple, dans votre code source). Utilisez toujours des variables d'environnement pour la stocker en toute sécurité.


### 5. Web Frontend UX

* **Construction de l'image Docker :**

```bash
docker-compose -f docker-compose.local.yml build web-container # Construit l'image Docker locale pour l'interface web
```

### 6. Commandes Docker Diverses

* **Liste des conteneurs en cours d'exécution :**

```bash
docker-compose -f docker-compose.local.yml ps # Liste les conteneurs locaux
```

* **Redémarrage des conteneurs :**

```bash
docker-compose -f docker-compose.local.yml restart # Redémarre les conteneurs locaux
```

* **Construction et taggage d'une image Docker (exemple pour l'API Java) :**

```bash
docker build -t eli256/jdr-generator-api:latest -f api/Dockerfile . # Construit et taggue l'image locale de l'API Java
```

### 7. PowerShell Policy (si nécessaire)

Si vous rencontrez des problèmes d'exécution de scripts PowerShell, vous devrez peut-être ajuster la stratégie d'exécution :

```powershell
Get-ExecutionPolicy -List # Affiche les stratégies d'exécution actuelles
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```


## Intégration de GitHub Actions

Ce projet utilise GitHub Actions pour automatiser à la fois les vérifications de la qualité du code et le déploiement des images Docker. 
Cette approche permet d'assurer un code de haute qualité et de simplifier le processus de déploiement.

### Vérification de la Qualité du Code

La qualité du code est vérifiée automatiquement à chaque *push* et *pull request* grâce à un workflow GitHub Actions défini dans le fichier `.github/workflows/code-quality.yml`. 
Ce workflow contient des sections dédiées à la partie du projet `nodejs-code-quality-web` et s'execute depuis toutes les branches.

**Fonctionnement :**

-   Le workflow vérifie la qualité du code à chaque *push* et *pull request* sur toutes les branches.
-   Des vérifications spécifiques sont exécutées en fonction du type de module (Node.js ou Java).
-   Les résultats des vérifications sont disponibles dans l'interface GitHub Actions.

**Étapes du workflow de qualité du code :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Configuration de Node.js :** Configure l'environnement Node.js avec la version spécifiée.
3.  **Installation des dépendances :** Installe les dépendances du projet.
4.  **Vérifications :**
    * **Linting :** ESLint est utilisé pour vérifier le style et la qualité du code (pour les modules Node.js).
    * **Compilation :** Le code TypeScript est compilé pour détecter les erreurs de typage (pour les modules NestJS et Web).
    * **Formatage :** Prettier est utilisé pour formater le code de manière cohérente (pour les modules Node.js).
    
### Déploiement Continu avec Docker

Le déploiement des images Docker est également automatisé via GitHub Actions avec le workflow défini dans `.github/workflows/docker-push.yml`. 
Ce workflow construit et publie les images Docker vers Docker Hub lorsqu'un *push* est effectué sur les branches spécifiées (actuellement `githubactions` et `main`).

**Fonctionnement :**

- Le workflow ne construit et ne pousse une image Docker que si des modifications sont détectées dans le dossier source correspondant (ici web/).
- Il est possible de déclencher manuellement le workflow pour forcer la reconstruction de toutes les images.
- Cette approche optimise le temps d'exécution et l'utilisation des ressources.

**Étapes du workflow de déploiement :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Connexion à Docker Hub :** Utilise les secrets GitHub `DOCKERHUB_USERNAME` et `DOCKERHUB_TOKEN` pour se connecter au compte Docker Hub.
3.  **Build et push des images :** Pour ce module 'web', l'image Docker est construite et taguée avec le SHA du commit actuel ainsi que le tag `latest`, puis les deux tags sont poussés vers Docker Hub. Les images sont disponibles sur ce repo : https://hub.docker.com/repositories/eli256

En combinant ces deux workflows GitHub Actions, cela assure à la fois la qualité du code et un déploiement automatisé de l'application (sans les api key cependant).


# Configuration et Utilisation de Jenkins avec Docker

Ce document décrit les étapes pour configurer et utiliser Jenkins avec Docker Desktop pour ce projet.

## Étapes d'Installation et Configuration

Obligatoire : Remplacez dans les commandes suivantes le `<projects_repositories_path>` par le chemin d'accès vers le repo de projet JDR-Generator, par exemple :
`c/<projects_repositories_path>/JDR-Generator` sur mon poste sera `c/Users/fredericcoupez/IdeaProjects/JDR-Generator`

1.  **Création et accès au répertoire de stockage de Jenkins :**

    Ce setup Jenkins utilise un montage de volume (`-v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home`) pour persister les données de votre instance Jenkins (configurations, jobs, plugins, historique des builds, etc.) sur votre machine locale.
    
    **Pourquoi cette approche ?**
    La persistance des données est **fortement recommandée** et **essentielle** pour un environnement de développement ou de production. Sans cela, chaque fois que le conteneur Jenkins est supprimé (par exemple, lors d'une mise à jour de l'image Docker ou d'un nettoyage), toutes vos configurations et votre travail seraient perdus, vous obligeant à reconfigurer Jenkins à chaque fois.
    
    **Impact sur les performances au démarrage :**
    Le fait de persister de nombreuses données peut entraîner un temps de démarrage de Jenkins plus long, car l'instance doit lire et charger toutes ses configurations depuis le volume monté. Ce comportement est normal et est le prix de la rétention de vos données et de la simplicité de gestion.

    ```bash
    cd C:\<projects_repositories_path>\JDR-Generator\
    mkdir -p .jenkins
    ```

    * Ceci crée le répertoire où les données de Jenkins seront persistées.
    * **Important :** Assurez-vous que l'utilisateur qui exécute Docker a les droits de lecture et d'écriture dans ce répertoire.
    * **Note :** Ce répertoire est ajouté à `.gitignore` pour éviter de versionner les données sensibles.

2.  **Téléchargement d'une image Docker de Jenkins contenant docker (construite à partir du Dockerfile dans le dossier) :**
    
    ```bash
    cd .github/workflows/jenkins/agent/
    docker build -t eli256/jenkins-docker-image-agent:latest .
    cd .github/workflows/jenkins/
    docker stop jenkins-container
    docker rm jenkins-container
    docker build -t eli256/jenkins-docker-image .
    docker run -d --name jenkins-container -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home eli256/jenkins-docker-image:latest
    ```

    * Ceci construira l'image Docker personnalisée eli256/jenkins-docker-image à partir du Dockerfile situé dans le dossier actuel. Cette image sera basée sur jenkins/jenkins:lts-jdk17 et inclura le client Docker.
    * Jenkins sera accessible sur le port 8080 de votre hôte
    * Le port 50000 est utilisé pour les agents Jenkins (communication maître/esclave).
    * Cela va prendre quelques minutes la première fois, car Docker doit télécharger les couches de l'image de base et installer le client Docker.

3.  **Montage et exécution du conteneur Jenkins :**

    En exécutant la commande `docker images`, vous devriez voir l'image eli256/jenkins-docker-image dans la liste.

    ```bash
    docker run -d --name jenkins-container -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home eli256/jenkins-docker-image
    ```

    * `--name jenkins-container`: Nomme le conteneur Jenkins "jenkins-container".
    * `-d`: Exécute le conteneur en mode détaché (en arrière-plan).
    * `-p 8080:8080 -p 50000:50000`: Mappe les ports 8080 et 50000 du conteneur aux ports correspondants de l'hôte.
    * `-v /var/run/docker.sock:/var/run/docker.sock`: Monte le socket Docker de l'hôte pour permettre à Jenkins d'exécuter des commandes Docker (Docker-out-of-Docker).
    * `-v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home`: Path à éditer ; Monte le répertoire de stockage de Jenkins sur l'hôte dans le répertoire `/var/jenkins_home` du conteneur.
    * `eli256/jenkins-docker-image`: En utilisant l'image custom buildée (via le Dockerfile)

    **Alternative :**

    Si vous ne souhaitez pas persister les données de Jenkins et que vous préférez que chaque lancement du conteneur soit une instance "vierge" (par exemple, pour des tests très spécifiques et éphémères), vous pouvez **retirer le montage de volume** (`-v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home`) de la commande `docker run`.
    **AVERTISSEMENT** : L'utilisation de Jenkins sans persistance entraînera la perte de toutes les données du /var/jenkins_home (jobs, plugins, utilisateurs, historique de builds) à chaque suppression du conteneur.

    ```bash
    docker run -d --name jenkins-container --group-add 999 --group-add 0 -p 9000:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock eli256/jenkins-docker-image
    ```

4.  **Accès à l'application web Jenkins :**

    Accédez à Jenkins dans votre navigateur web via :

    ```
    http://localhost:8080/
    ```

5.  **Déverrouillage de Jenkins (si nécessaire) :**

    Si Jenkins est verrouillé, vous aurez besoin du mot de passe administrateur initial. Récupérez-le avec la commande :

    ```bash
    docker exec jenkins-container cat /var/jenkins_home/secrets/initialAdminPassword
    ```

    * Ceci affiche le mot de passe généré automatiquement. Collez-le dans le champ approprié sur la page web de Jenkins.

    * **Lecture des logs du conteneur Jenkins :**

        Le mot de passe initial peut également apparaître dans les logs de démarrage du conteneur Jenkins. Pour surveiller le démarrage ou diagnostiquer des problèmes avec le service Jenkins lui-même, utilisez la commande suivante :

       ```bash
       docker logs -f jenkins-container
       ```

       * Cette commande affiche les logs récents du conteneur Jenkins. Le mot de passe initial y est normallement imprimé lors du premier démarrage. Ces logs sont également essentiels pour le débogage général du service Jenkins.

6.  **Configuration initiale de Jenkins :**

    * Jenkins vous guidera à travers l'installation des plugins recommandés et la création du premier utilisateur administrateur.

7.  **Configuration d'un Personal Access Token (PAT) GitHub pour Jenkins :**

    1.  **Générez un Personal Access Token (PAT) sur GitHub :**

        * Allez dans les paramètres de votre compte GitHub ("Settings") : https://github.com/settings/profile.
        * Cliquez sur "Developer settings" puis "Personal access tokens" et "Fine-grained personal access tokens" : https://github.com/settings/personal-access-tokens
        * Cliquez sur "Generate new token".
        * Donnez un nom descriptif à votre token.
        * Choisissez les permissions minimales nécessaires : Pour cloner votre dépôt, la permission "Contents" avec l'accès "Read-only" suffit.
        * Cliquez sur "Generate token" et copiez et mémorisez le token immédiatement. Il sera impossible de le retrouver par la suite ! Ne le partagez pas, cela permettrait à une tierce personne d'utiliser vos secrets sur GitHub pour effectuer des actions directement.

    2.  **Ajoutez les identifiants à Jenkins :**

        * Dans Jenkins, allez dans "Administrer Jenkins" -> "Identifiants" -> "System" -> "Identifiants globaux (illimité)" -> "Add credentials".
        * Choisissez "Secret text" comme type d'identifiant.
        * Dans le champ "Secret", collez le Personal Access Token que vous avez généré sur Github.
        * Donnez un "ID" (par exemple, `github-pat-content-read`) et une "Description (par exemple, `Accès à GitHub avec Personal Access Token : Content en read-only`) à vos Credentials.
        * Cliquez sur "Create".

8.  **Installation manuelle de plugins :**

    1.  Allez dans "Administrer Jenkins" -> "Gérer les plugins".
    2.  Cliquez sur l'onglet "Disponible".
    3.  Recherchez le plugin souhaité et installez-le. 
    4.  Redémarrez Jenkins.

9.  **Redémarrage de Jenkins (si possible) :**

    Si Jenkins rencontre des problèmes, essayez de le redémarrer depuis le conteneur :

    ```bash
    docker exec jenkins-container /bin/jenkins.sh restart
    ```

    * Ceci tente un redémarrage en douceur de Jenkins.

10. **Suppression et recréation du conteneur Jenkins :**

    Si le redémarrage ne fonctionne pas automatiquement, relancer manuellement le container depuis Docker Desktop.
    Autrement, vous pouvez supprimer et recréer le conteneur :

    ```bash
    docker stop jenkins-container
    docker rm jenkins-container
    docker run -d --name jenkins-container -p 9000:8080 -p 50000:50000 --group-add 999 --group-add 0 -v /c/<projects_repositories_path>/JDR-Generator/.jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock eli256/jenkins-docker-image
    docker logs -f jenkins-container
    ```

    * Ceci supprimera le conteneur, mais vos données seront normalement conservées dans le répertoire monté (ex : `C:/<projects_repositories_path>/JDR-Generator/.jenkins`).

11. **Création d'un Pipeline Jenkins avec Jenkinsfile :**

    * Pour ce projet, nous utilisons un `Jenkinsfile` pour définir le pipeline de build. Le `Jenkinsfile` se trouve dans le repertoire .github/workflows/jenkins du dépôt et décrit les étapes du workflow de qualité du code.

    * **Créer une nouvelle Pipeline :**

        1.  Dans Jenkins, cliquez sur "Créer un nouveau Job".
        2.  Choisissez "Pipeline" et donnez-lui un nom (par exemple, "JDR-Generator-Code-Quality").
        3.  Dans la section "Définition", choisissez "Pipeline script from SCM".
        4.  Sélectionnez "Git" comme gestionnaire de code source.
        5.  Configurez l'URL du dépôt (par exemple, `https://github.com/NeoRyu/JDR-Generator`) et les informations d'identification (le PAT GitHub que vous avez configuré).
        6.  Spécifiez la branche à builder (par exemple, `*/main` et `*/jenkins`) le Jenkinsfile.
        7.  Assurez-vous que le "Script Path" est correct (ex `/.github/workflows/jenkins/Jenkinsfile`).
        8.  Configurez les autres options selon vos besoins (par exemple, les déclencheurs de build).
        9.  Sauvegardez le job, puis lancez le build. Si le script a des options, il risque d'échouer la première fois, mais mettra a jour votre configuration, relancez le build auquel cas.


## Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
