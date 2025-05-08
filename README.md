# JDR-Generator

NB : Branches tests pour acceder aux tests SCALA :
> Exemple : https://github.com/NeoRyu/JDR-Generator/blob/tests/api/src/main/scala/jdr/generator/api/scala/tools/TypescriptOperators.scala


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
* Google Gemini API
* OpenAI API
* npm ou yarn
* Docker (pour le déploiement)

## Pré-requis

* Node.js (pour NestJS et React)
* JDK (Java Development Kit) 8 ou supérieur (pour l'API Java)
* MySQL Server
* npm ou yarn
* Docker (si vous utilisez les commandes Docker fournies)

## Notes Importantes

* **Variables d'environnement :** La configuration correcte des variables d'environnement est cruciale pour le bon fonctionnement du projet. Consultez les fichiers `.env.example` (ou équivalents) dans chaque répertoire pour connaître les variables requises.
* **Dépendances Java :** Assurez-vous d'avoir un JDK et un outil de build Java (Maven ou Gradle) correctement installés et configurés.
* **Conflits de ports :** Les différentes parties du projet (API Java, API NestJS, Web) peuvent utiliser des ports différents. Si vous rencontrez des conflits, vous devrez peut-être modifier les configurations de port.
* **Documentation Supplémentaire :** Chaque module (api, gemini, web) devrait avoir son propre README avec des instructions plus détaillées.
* **Scripts NestJS :** Les commandes `clean`, `build` et `start` pour l'API NestJS font référence aux scripts définis dans le fichier `package.json` du répertoire `gemini`.
* **Fichiers Docker Compose :** Ce projet utilise deux fichiers `docker-compose` distincts :
    * `docker-compose.yml` : Configuration pour le déploiement sur AWS Elastic Beanstalk.
    * `docker-compose.local.yml` : Configuration optimisée pour l'exécution locale.

## Captures d'écran

![{A9C4900F-274B-451F-8097-A1AAAA3B500F}](https://github.com/user-attachments/assets/d60becac-93b7-4940-a3fc-5e1d26516053)
![{8857CA56-EF92-4868-B4CB-4E9C6E2CEC95}](https://github.com/user-attachments/assets/f3972e04-553b-4e44-ac17-6f5fbbb517d4)
![{A8D02D44-A30A-41B4-A562-6219D38B19C1}](https://github.com/user-attachments/assets/78b3df51-f65c-483c-9311-a40c00f8344c)
![{E8893894-CADA-4F5E-96FE-9C47959FE2E9}](https://github.com/user-attachments/assets/58a7e538-0037-4ea4-93eb-a61fd597e1c1)
![{CE9E8BF3-B49E-4F1D-9233-77F1BD3D2E04}](https://github.com/user-attachments/assets/9781b1b9-d458-491f-9edd-7d566c5b3536)
![{20240EB7-0A44-45C2-9244-2EBCF16F60B5}](https://github.com/user-attachments/assets/35336cfb-e2b8-445d-905a-cbe03c6d761a)
![{90DCBF62-C6FC-4F1F-AEFD-F95E6F257B82}](https://github.com/user-attachments/assets/e1bfb0bd-8f70-4b66-b7b3-dda68f52739a)

## Installation

1.  **Cloner le dépôt :**

    ```bash
    git clone [https://github.com/NeoRyu/JDR-Generator.git](https://github.com/NeoRyu/JDR-Generator.git)
    cd JDR-Generator
    ```

2.  **Configurer les variables d'environnement :**

    * Vous devrez configurer les variables d'environnement spécifiques à chaque partie du projet (API Java, API NestJS, Web). Des exemples de fichiers `.env.example` devraient être fournis dans chaque répertoire si possible.
    * Les variables d'environnement incluent généralement les clés d'API (Google Gemini, OpenAI), les informations de connexion à la base de données MySQL, et d'autres paramètres de configuration.

3.  **Installer les dépendances de l'API NestJS (Gemini) :**

    ```bash
    cd gemini
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
        java -jar target/nom-de-votre-application.jar  # Remplacez nom-de-votre-application.jar par le nom réel du fichier JAR
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

JDR-Generator/
├── api/          # API Backend Java (avec Scala)
│   ├── src/
│   ├── pom.xml   # (ou build.gradle)
│   └── ...
├── gemini/       # API Backend NestJS (pour Gemini/OpenAI)
│   ├── src/
│   ├── package.json
│   └── ...
├── web/          # Frontend React/TypeScript
│   ├── src/
│   ├── package.json
│   └── ...
├── README.md     # Fichier README principal
└── ...


## Utilisation de Docker

Ce projet peut être déployé et exécuté à l'aide de Docker et Docker Compose. Cela simplifie la configuration de l'environnement et assure une cohérence entre les différents déploiements.

**Analyse des Images Docker :**

* Le module **web** utilise un processus de build en deux étapes :
    * Une première image Node.js est utilisée pour builder l'application React avec Vite.
    * Les fichiers statiques buildés sont ensuite copiés dans une image Nginx, qui sert l'application web.
* Les modules **gemini** et **openai** utilisent des images Node.js pour exécuter leurs applications NestJS (TypeScript) après la compilation.
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
docker build -t <votre_nom_utilisateur_docker>/jdr-generator-api:latest -f api/Dockerfile . # Construit et taggue l'image locale de l'API Java
```

### 7. PowerShell Policy (si nécessaire)

Si vous rencontrez des problèmes d'exécution de scripts PowerShell, vous devrez peut-être ajuster la stratégie d'exécution :

```powershell
Get-ExecutionPolicy -List # Affiche les stratégies d'exécution actuelles
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```


## Intégration de GitHub Actions

Ce projet utilise GitHub Actions pour automatiser la construction et la publication des images Docker vers Docker Hub lors de chaque push sur les branches spécifiées (actuellement `githubactions` et `main`). 
Le workflow est défini dans le fichier `.github/workflows/docker-push.yml`, ce fichier est automatiquement détecté par Github.
Les dernières images dockerisée sont disponible ici : https://hub.docker.com/repositories/eli256

**Fonctionnement actuel :**

Le workflow actuel est configuré pour s'exécuter lors de chaque push sur les branches `githubactions` et `main`, mais **ne construira et ne poussera une image Docker que si des modifications ont été détectées dans le dossier source correspondant** (api/, web/, gemini/, openai/).
Il est également possible de déclencher manuellement le workflow via l'interface GitHub Actions avec une option pour forcer la reconstruction de toutes les images.
Grâce à cette configuration, seuls les modules ayant subi des modifications verront leur image Docker reconstruite et poussée, optimisant ainsi le temps d'exécution du workflow et l'utilisation des ressources.

Lors d'un push, le workflow effectue les étapes suivantes :
1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Connexion à Docker Hub :** Utilise les secrets GitHub `DOCKERHUB_USERNAME` et `DOCKERHUB_TOKEN` pour se connecter au compte Docker Hub.
3.  **Build et push des images :** Pour chaque module (`web`, `api`, `gemini`, `openai`), l'image Docker est construite et taguée avec le SHA du commit actuel ainsi que le tag `latest`, puis les deux tags sont poussés vers Docker Hub.


## Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```