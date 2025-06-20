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
