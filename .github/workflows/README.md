# JDR-Generator

## Table des Matières

1.  [Structure du Projet](#structure-du-projet)
2.  [Intégration de GitHub Actions](#intégration-de-github-actions)
    * [Vérification de la Qualité du Code](#vérification-de-la-qualité-du-code)
        * [Fonctionnement](#fonctionnement)
        * [Étapes du workflow de qualité du code](#étapes-du-workflow-de-qualité-du-code)
    * [Déploiement Continu avec Docker](#déploiement-continu-avec-docker)
        * [Fonctionnement](#fonctionnement-1)
        * [Étapes du workflow de déploiement](#étapes-du-workflow-de-déploiement)
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
│       ├── localhost/ ...
│       ├── README.md                <-- VOUS ETES ICI
│       ├── code-quality.yml             <-- Script Gituhub Actions : Gestion de la qualité de code (modifiez le pom.xml du module api en : <skip.quality.code>false</skip.quality.code> )
│       ├── docker-push.yml              <-- Script Gituhub Actions : Si des changes ont eu lieu dans l'un des modules, va builder, créer une image docker et la deployer sur Docker Hub
│       └── qodana_code_quality.yml      <-- Script Gituhub Actions : Permet de consulter la santé et qualité du code de l'app
└── LICENSE                              <-- Apache License
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

### Gestion Sécurisée des Clés API pour les Outils de Qualité du Code

Certains outils d'analyse de la qualité du code, comme OWASP Dependency-Check, nécessitent l'accès à des bases de données externes (telles que la National Vulnerability Database - NVD) via des API.
Pour des raisons de performance, de fiabilité et pour éviter les limitations de débit, il est fortement recommandé d'utiliser des clés API : https://nvd.nist.gov/developers/api-key-requested

Pour garantir la sécurité et éviter de commettre des secrets sensibles dans le dépôt Git public, ces clés API ne sont **jamais stockées directement dans les fichiers de configuration du projet (ex: `pom.xml`)**. Elles sont injectées de manière sécurisée selon l'environnement :

#### 1. Pour le Développement Local

Pour les exécutions Maven en local, la clé API NVD est configurée dans le fichier `settings.xml` de l'utilisateur, qui n'est pas versionné par Git.

* **Chemin du fichier `settings.xml` :**
    * Linux/macOS : `~/.m2/settings.xml`
    * Windows : `%USERPROFILE%\.m2\settings.xml`

  * **Configuration dans `settings.xml` (exemple) :**
      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
        <profiles>
          <profile>
            <id>nvd-api-key</id>
            <properties>
              <nvd.api.key>VOTRE_CLE_API_NVD_PRIVEE_ICI</nvd.api.key>
            </properties>
          </profile>
        </profiles>
        <activeProfiles>
          <activeProfile>nvd-api-key</activeProfile>
        </activeProfiles>
      </settings>
      ```
    Assurez-vous que votre IDE est configuré pour utiliser ce `settings.xml` utilisateur si vous rencontrez des problèmes.
    Il est possible de vérifier le settings utilisé via la commande `mvn help:effective-settings`, et le POM effectif via `mvn help:effective-pom` pour y vérifier la bonne intégration des key.


#### 2. Pour l'Intégration Continue (GitHub Actions)

Dans l'environnement de GitHub Actions, la clé API NVD est gérée comme un **secret de dépôt** et est injectée au moment de l'exécution du workflow.

* **Définition du Secret :**
  La clé API est stockée en tant que secret de dépôt GitHub (par exemple, `NVD_API_KEY`). Cela se fait dans les paramètres de votre dépôt GitHub (`Settings > Secrets and variables > Actions`).

* **Utilisation dans le Workflow (`.github/workflows/code-quality.yml`) :**
  Dans le workflow GitHub Actions, la clé est passée à la commande Maven via l'option `-D` (`-Dnvd.api.key`) en utilisant la syntaxe des secrets GitHub :
    ```yaml
    - name: API Maven Dependency Check
      run: cd api && mvn --batch-mode org.owasp:dependency-check-maven:check -Dnvd.api.key="${{ secrets.NVD_API_KEY }}"
    ```
  Cette méthode garantit que la clé est disponible pour le plugin lors des vérifications de qualité de code en CI/CD, sans être exposée publiquement.

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

___________
# Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
