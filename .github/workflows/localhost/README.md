# JDR-Generator

## Table des Matières

## Table des Matières

1.  [Structure du Projet](#structure-du-projet)
2.  [Exécution Locale avec Docker et Docker Compose](#exécution-locale-avec-docker-et-docker-compose)
    * [Prérequis](#prérequis)
    * [Fichiers Clés](#fichiers-clés)
    * [Démarrage de l'Application](#démarrage-de-lapplication)
        * [Compilation et Démarrage Initial](#compilation-et-démarrage-initial)
        * [Démarrage Rapide](#démarrage-rapide)
    * [Scripts Utilitaires Locaux](#scripts-utilitaires-locaux)
        * [`docker-push.bat`](#docker-pushbat)
        * [`docker-start-app.sh`](#docker-start-appsh)
        * [`init.sql`](#initsql)
    * [Accès aux Services](#accès-aux-services)
    * [Gestion et Dépannage](#gestion-et-dépannage)
        * [Arrêt et Suppression des Conteneurs/Volumes](#arrêt-et-suppression-des-conteneursvolumes)
        * [Consultation des Logs](#consultation-des-logs)
        * [Accès à la Base de Données MySQL](#accès-à-la-base-de-données-mysql)
        * [Problèmes Courants](#problèmes-courants)
3.  [Licence](#licence)

## Structure du Projet

```
JDR-Generator/
├── .env
├── docker-compose.local.yml           <-- Fichier Docker Compose principal
├── ...
├── api/
│   ├── ...
│   └── src/
│       ├── ...
│       └── resources/
│           ├── ...
│           └── application-localdocker.yml         <-- pour le build de l'image docker local
├── freepik/ ...
├── gemini/ ...
├── openai/ ...
├── web/ ...
├── .github/
│   └── workflows/
│       ├── aws/ ...
│       ├── jenkins/ ...
│       ├── kubernetes/ ...
│       ├── localhost/
│       │   ├── README.md                <-- VOUS ETES ICI
│       │   ├── docker-push.bat          <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── docker-start-app.sh      <-- Script shell linux deployant l'app dockerisée via docker-compose.local.yml
│       │   ├── init.sql                 <-- Script sql gérant le password d'un user root pour docker-compose.local.yml
│       │   └── ...
│       └── ...
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

___________
# Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
