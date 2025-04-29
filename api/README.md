# JDR-Generator API (Java)

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
    docker-compose up -d api
    ```

## Licence

```markdown
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```