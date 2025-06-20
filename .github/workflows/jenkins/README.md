# JDR-Generator

1.  [Structure du Projet](#structure-du-projet)
2.  [Configuration et Utilisation de Jenkins avec Docker](#configuration-et-utilisation-de-jenkins-avec-docker)
    * [Étapes d'Installation et Configuration](#étapes-dinstallation-et-configuration)
        1.  [Création et accès au répertoire de stockage de Jenkins](#création-et-accès-au-répertoire-de-stockage-de-jenkins)
        2.  [Téléchargement d'une image Docker de Jenkins contenant docker](#téléchargement-dune-image-docker-de-jenkins-contenant-docker-construite-à-partir-du-dockerfile-dans-le-dossier)
        3.  [Montage et exécution du conteneur Jenkins](#montage-et-exécution-du-conteneur-jenkins)
        4.  [Accès à l'application web Jenkins](#accès-à-lapplication-web-jenkins)
        5.  [Déverrouillage de Jenkins (si nécessaire)](#déverrouillage-de-jenkins-si-nécessaire)
        6.  [Configuration initiale de Jenkins](#configuration-initiale-de-jenkins)
        7.  [Configuration d'un Personal Access Token (PAT) GitHub pour Jenkins](#configuration-dun-personal-access-token-pat-github-pour-jenkins)
        8.  [Installation manuelle de plugins](#installation-manuelle-de-plugins)
        9.  [Redémarrage de Jenkins (si possible)](#redémarrage-de-jenkins-si-possible)
        10. [Suppression et recréation du conteneur Jenkins](#suppression-et-recréation-du-conteneur-jenkins)
        11. [Création d'un Pipeline Jenkins avec Jenkinsfile](#création-dun-pipeline-jenkins-avec-jenkinsfile)
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
│           └── application-localdocker.yml         <-- pour le build de l'image docker locale
├── freepik/ ...
├── gemini/ ...
├── openai/ ...
├── web/ ...
├── .github/
│   └── workflows/
│       ├── aws/ ...
│       ├── jenkins/
│       │   ├── README.md                <-- VOUS ETES ICI
│       │   ├── agent
│       │   │   └── Dockerfile             <-- 1. docker build -t eli256/jenkins-docker-image-agent:latest .
│       │   ├── code-quality
│       │   │   ├── Jenjenkins_logs.txt
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   ├── database-export
│       │   │   └── Jenkinsfile                 <-- jobs pipeline a créer : http://localhost:8080/job
│       │   └── Dockerfile                 <-- 2. docker build -t eli256/jenkins-docker-image .
│       ├── kubernetes/...
│       ├── localhost/
│       │   ├── docker-push.bat   <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── docker-start-app.sh      <-- Script shell linux deployant l'app dockerisée via docker-compose.local.yml
│       │   ├── init.sql                 <-- Script sql gérant le password d'un user root pour docker-compose.local.yml
│       │   └── ...
│       └─── ...
└── LICENSE                              <-- Apache License
```


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


___________
# Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
