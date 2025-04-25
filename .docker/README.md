# JDR-Generator

## Description

JDR-Generator est une application interactive pour la création de personnages de Jeux de Rôle (JDR). 
Les fichiers présents dans ce dossier ont pour objectif de conteneuriser l'application à l'aide de Docker afin de créer un réseau virtuel isolé. 
L'application sera accessible depuis l'URL : 
-> [**Indiquer l'URL ici, par exemple : http://localhost:5173**] 


## Caractéristiques principales: 
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

## START

### Pré-requis
- **PowerShell** : Généralement inclus par défaut sur Windows. Le chemin indiqué (`C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe`) est l'emplacement standard. Assurez-vous qu'il est accessible depuis votre terminal.
- **Docker Desktop** : Doit être installé et en cours d'exécution sur votre système. Vous pouvez le télécharger depuis [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/).


## UTILISATION

1.  **Cloner le dépôt (si ce n'est pas déjà fait) :**
    ```bash
    git clone [https://github.com/NeoRyu/JDR-Generator](https://github.com/NeoRyu/JDR-Generator)
    cd JDR-Generator
    ```

2.  **Exécuter le script de build Docker :**
    Naviguez dans le dossier contenant les fichiers Docker (celui-ci) et exécutez le script PowerShell suivant pour construire les images Docker :
    ```powershell
    .\docker-build.bat
    ```
    Ce script construira les images pour l'API, l'interface web (frontend), et potentiellement d'autres services comme l'API Gemini et la base de données MySQL.

3.  **Exécuter le script de lancement Docker :**
    Après la construction des images, exécutez le script PowerShell suivant pour créer le réseau Docker et lancer les conteneurs :
    ```powershell
    .\docker-run.bat
    ```
    Ce script va :
    - Créer un réseau Docker nommé `jdr-generator-net`.
    - Démarrer un conteneur MySQL (si inclus) nommé `jdr-mysql`.
    - Démarrer le conteneur de l'API backend nommé `jdr-api`.
    - Démarrer le conteneur de l'API Gemini (si inclus) nommé `jdr-gemini`.
    - Démarrer le conteneur de l'interface web (frontend) nommé `jdr-web`, en le connectant aux autres services via le réseau Docker.

4.  **Accéder à l'application :**
    Une fois tous les conteneurs en cours d'exécution (vous pouvez vérifier avec la commande `docker ps`), l'application JDR-Generator sera accessible depuis votre navigateur à l'URL :
    ```
    http://localhost:5173
    ```
    Le port `5173` est celui exposé par le conteneur de l'interface web.

## Scripts PowerShell

Ce dossier contient les scripts PowerShell suivants pour faciliter la gestion de l'environnement Docker :

-   `docker-build.bat` : Construit les images Docker nécessaires pour l'application.
-   `docker-run.bat` : Crée le réseau Docker et lance tous les conteneurs de l'application.
-   `docker-stop.bat` : Arrête tous les conteneurs en cours d'exécution pour l'application.