# JDR-Generator - Module API (Backend Java)

## Description

Ce module contient l'API backend principale de JDR-Generator, développée en Java. Son rôle principal est de gérer les données persistantes du système, notamment les informations sur les personnages, les utilisateurs et les règles du jeu.

Elle utilise une base de données MySQL pour stocker ces informations et FlywayDB pour gérer les migrations de la base de données, assurant ainsi une évolution structurée du schéma. Bien que l'API soit principalement écrite en Java, Scala a été intégré à titre d'exercice et d'exploration polyglotte.

## Caractéristiques Principales

* **Gestion des Données Persistantes :** Stockage et récupération des informations essentielles du jeu (personnages, utilisateurs, etc.).
* **Interaction avec la Base de Données :** Communication avec MySQL pour un stockage fiable et efficace des données.
* **Migrations de la Base de Données :** Utilisation de FlywayDB pour versionner et appliquer les changements de schéma de la base de données.
* **Développement en Java :** Langage principal de l'API, garantissant performance et robustesse.
* **Exploration Scala :** Intégration partielle de Scala pour explorer ses avantages et sa compatibilité avec Java.

## Technologies Utilisées

* Java 17
* Maven
* MySQL
* FlywayDB
* Scala (pour certaines parties)

## Pré-requis

* Java Development Kit (JDK) 17 ou supérieur
* Maven
* MySQL Server (en cours d'exécution et accessible)

## Installation

1.  **Cloner le dépôt :**

    ```bash
    git clone [https://github.com/NeoRyu/JDR-Generator.git](https://github.com/NeoRyu/JDR-Generator.git)
    cd JDR-Generator
    ```

2.  **Accéder au répertoire de l'API :**

    ```bash
    cd api
    ```

3.  **Configurer la connexion à la base de données :**

    * Assurez-vous que votre serveur MySQL est en cours d'exécution.
    * Configurez les paramètres de connexion à la base de données (URL, nom d'utilisateur, mot de passe) dans le fichier de configuration approprié (par exemple, `src/main/resources/application.properties` ou `application.yml`).

4.  **Synchroniser le projet Maven et télécharger les dépendances :**

    * Si vous utilisez un IDE (comme IntelliJ IDEA, Eclipse), il gérera généralement cela automatiquement.
    * Sinon, exécutez la commande Maven suivante :

        ```bash
        mvn clean install
        ```

5.  **Construire l'API :**

    * Exécutez la commande Maven pour compiler et packager l'API :

        ```bash
        mvn clean package
        ```

## Exécution de l'API

1.  **Exécuter l'application Java :**

    * Si vous utilisez un IDE, vous pouvez exécuter la classe principale `jdr.generator.api.ApiApplication` directement.
    * Sinon, vous pouvez exécuter le fichier JAR créé dans le répertoire `target` :

        ```bash
        cd C:\<projects_repositories_path>\JDR-Generator\api
        java -jar target/nom-de-l-application.jar
        ```

      (Remplacez `nom-de-l-application.jar` par le nom réel du fichier JAR.)

## API Endpoints

(À compléter avec la liste des points de terminaison de l'API, leurs méthodes HTTP, les paramètres attendus et les réponses possibles. Ceci est crucial pour les développeurs frontend et les testeurs.)

Exemple :

* `GET /characters` : Récupère la liste de tous les personnages.
* `POST /characters` : Crée un nouveau personnage. Paramètres : `name`, `class`, `race`, etc.
* `GET /characters/{id}` : Récupère un personnage spécifique par son ID.
* `PUT /characters/{id}` : Met à jour un personnage existant.
* `DELETE /characters/{id}` : Supprime un personnage.

## Docker

### Construction de l'image Docker

1.  **Construire l'application Java :**

    ```bash
    mvn clean install package
    ```

2.  **Construire l'image Docker :**

    ```bash
    docker build -t jdr-generator-api .
    ```

    (Assurez-vous d'être dans le répertoire `api` ou spécifiez le chemin du `Dockerfile`.)

### Exécution du Conteneur Docker

1.  **Exécuter le conteneur :**

    ```bash
    docker run -p 8080:8080 jdr-generator-api
    ```

    (Cela expose l'API sur le port 8080 de votre machine hôte. Ajustez le port si nécessaire.)

2.  **Variables d'environnement :**

    * Si votre API nécessite des variables d'environnement (par exemple, pour la connexion à la base de données), vous pouvez les passer à Docker avec l'option `-e` :

        ```bash
        docker run -p 8080:8080 \
                   -e DB_URL=jdbc:mysql://... \
                   -e DB_USER=... \
                   -e DB_PASSWORD=... \
                   jdr-generator-api
        ```

### Docker Compose (Si vous utilisez Docker Compose au niveau du monorepo)

1.  **S'assurer que le service `api` est correctement configuré dans `docker-compose.yml` :**

    ```yaml
    services:
      api:
        build:
          context: ./api
          dockerfile: Dockerfile
        ports:
          - "8080:8080"
        environment:
          DB_URL: jdbc:mysql://...
          DB_USER: ...
          DB_PASSWORD: ...
        depends_on:
          - mysql  # Si l'API dépend de la base de données
    ```

2.  **Démarrer le service :**

    ```bash
    docker-compose -f docker-compose.local.yml up -d api-container
    ```

##  Intégration de GitHub Actions

Ce projet utilise GitHub Actions pour automatiser à la fois les vérifications de la qualité du code et le déploiement des images Docker.
Cette approche permet d'assurer un code de haute qualité et de simplifier le processus de déploiement.

###  Workflow de Qualité du Code

La qualité du code est vérifiée automatiquement à chaque *push* et *pull request* grâce à un workflow GitHub Actions défini dans le fichier `.github/workflows/code-quality.yml`.
Ce workflow contient une section dédiée à la partie du projet `java-code-quality` et s'exécute depuis toutes les branches.

**Fonctionnement :**

* Le workflow vérifie la qualité du code à chaque *push* et *pull request* sur toutes les branches.
* Des vérifications spécifiques sont exécutées pour ce module Java.
* Les résultats des vérifications sont disponibles dans l'interface GitHub Actions.

**Étapes du workflow de qualité du code :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Configuration de Java :** Configure l'environnement Java avec la version spécifiée.
3.  **Vérifications :**
    * **Analyse de style :** (Optionnel) Checkstyle peut être utilisé pour vérifier le style du code Java.
    * **Analyse statique :** (Optionnel) SpotBugs peut être utilisé pour détecter les bugs potentiels.

###  Workflow de Déploiement Continu avec Docker

Le déploiement des images Docker est également automatisé via GitHub Actions avec le workflow défini dans `.github/workflows/docker-push.yml`.
Ce workflow construit et publie les images Docker vers Docker Hub lorsqu'un *push* est effectué sur les branches spécifiées (actuellement `githubactions` et `main`).

**Fonctionnement :**

* Le workflow ne construit et ne pousse une image Docker que si des modifications sont détectées dans le dossier source correspondant (ici api/).
* Il est possible de déclencher manuellement le workflow pour forcer la reconstruction de toutes les images.
* Cette approche optimise le temps d'exécution et l'utilisation des ressources.

**Étapes du workflow de déploiement :**

1.  **Checkout du code :** Récupère la dernière version du code source.
2.  **Connexion à Docker Hub :** Utilise les secrets GitHub `DOCKERHUB_USERNAME` et `DOCKERHUB_TOKEN` pour se connecter au compte Docker Hub.
3.  **Build et push des images :** Pour ce module 'api', l'image Docker est construite et taguée avec le SHA du commit actuel ainsi que le tag `latest`, puis les deux tags sont poussés vers Docker Hub. Les images sont disponibles sur ce repo : [https://hub.docker.com/repositories/eli256](https://hub.docker.com/repositories/eli256)

En combinant ces deux workflows GitHub Actions, cela assure à la fois la qualité du code et un déploiement efficace et automatisé de l'application.

## Licence

```markdown
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```