# JDR-Generator

1.  [Structure du Projet](#structure-du-projet)
2.  [Déploiement sur Kubernetes](#déploiement-sur-kubernetes)
    * [Prérequis](#prérequis)
        * [Cluster Kubernetes Fonctionnel](#cluster-kubernetes-fonctionnel)
        * [Installation de `kubectl`](#installation-de-kubectl)
        * [`kubectl` Configuré](#kubectl-configuré)
        * [Images Docker Disponibles](#images-docker-disponibles)
    * [Composants Déployés](#composants-déployés)
    * [Processus de Déploiement](#processus-de-déploiement)
    * [Accès à l'Application](#accès-à-lapplication)
    * [Consultation des Logs](#consultation-des-logs)
    * [Communication Inter-services](#communication-inter-services)
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
│           └── application-kubernetes.yml         <-- pour le build de l'image kubernetes
├── freepik/ ...
├── gemini/ ...
├── openai/ ...
├── web/ ...
├── .github/
│   └── workflows/
│       ├── aws/ ...
│       ├── jenkins/ ...
│       ├── kubernetes/
│       │   ├── README.md                <-- VOUS ETES ICI
│       │   ├── 00-secrets.yaml                   <-- mettre vos API_KEY ici
│       │   ├── 01-configmaps.yaml
│       │   ├── 02-mysql.yaml
│       │   ├── 03-api.yaml
│       │   ├── 04-ai-modules.yaml
│       │   ├── 05-frontend.yaml
│       │   └── deploy-kubernetes.bat             <-- Script de déploiement kubernetes local
│       ├── localhost/
│       │   ├── docker-push.bat   <-- Script shell ms-dos de push Docker Hub (via docker-compose.local.yml)
│       │   ├── ...
│       │   └── ...
│       └─── ...
└── LICENSE                              <-- Apache License
```


## Déploiement sur Kubernetes

Cette section décrit comment déployer l'application JDR-Generator sur un cluster Kubernetes. 
Le déploiement est orchestré via plusieurs fichiers YAML qui définissent les différents composants de l'architecture.

### Prérequis

Pour déployer l'application sur Kubernetes, assurez-vous de disposer des éléments suivants :

#### Cluster Kubernetes Fonctionnel

Vous devez disposer d'un cluster Kubernetes opérationnel et accessible. Cela peut être :
* **Minikube :** Idéal pour le développement et les tests locaux sur une seule machine.
* **Kubernetes de Docker Desktop :** Une option intégrée pour les utilisateurs de Docker Desktop.
* **Un cluster cloud :** Comme Amazon EKS (Elastic Kubernetes Service), Google GKE (Google Kubernetes Engine) ou Azure AKS (Azure Kubernetes Service) pour les déploiements en production.

Assurez-vous que votre cluster est démarré et en bonne santé avant de procéder.

![kubernetes_1-cluster](https://github.com/user-attachments/assets/59b11b1b-2910-43b0-9237-1743afc7189f)


#### Installation de `kubectl`

Si la commande `kubectl` n'est pas reconnue sur votre système après avoir suivi les prérequis de 
votre cluster Kubernetes local (Minikube, Docker Desktop, etc.), cela signifie qu'elle n'est pas 
correctement installée ou accessible via votre `PATH` système.

Pour installer `kubectl`, il est **fortement recommandé de suivre la documentation officielle de Kubernetes**,
car les étapes varient en fonction de votre système d'exploitation (Windows, macOS, Linux).

* **Documentation officielle pour l'installation de `kubectl` :**
  [https://kubernetes.io/docs/tasks/tools/install-kubectl/](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

Après l'installation, assurez-vous de rouvrir votre terminal ou de rafraîchir votre environnement 
pour que la commande soit reconnue, un redémarrage du PC est parfois nécessaire.

#### `kubectl` Configuré

`kubectl` est l'outil en ligne de commande officiel de Kubernetes, indispensable pour interagir avec votre cluster.
* Il doit être installé sur votre machine locale.
* Il doit être configuré pour pointer vers le cluster Kubernetes cible (généralement via le fichier `~/.kube/config`).
* Vous pouvez vérifier la connexion à votre cluster en exécutant :
    ```bash
    kubectl cluster-info
    ```
  Ou pour vérifier la version du client et du serveur Kubernetes :
    ```bash
    kubectl version --client --short
    ```
* **Changez de contexte (si nécessaire) :**
  Si la commande `current-context` ne retourne pas le nom de votre cluster souhaité, vous devrez changer de contexte.
    ```bash
    kubectl config use-context <NOM_DU_CONTEXTE>
    # Remplacez <NOM_DU_CONTEXTE> par le nom du contexte de votre cluster
    # (par exemple, 'minikube', 'docker-desktop', ou le nom d'un cluster cloud configuré)
    ```
* **Vérifiez la configuration `kubeconfig` :**
  Le fichier de configuration de `kubectl` se trouve par défaut dans `~/.kube/config`. 
* Assurez-vous qu'il existe et qu'il contient les informations correctes pour la connexion à votre cluster.

#### Images Docker Disponibles

Les **images Docker** des services (API, Web, Gemini, OpenAI, Freepik) doivent être disponibles 
dans un **registre Docker** accessible par votre cluster (par exemple, **Docker Hub**). 
Assurez-vous d'avoir poussé les dernières versions de vos images, en exécutant par exemple le script :
`.github/workflows/localhost/docker-push.bat`

### Composants Déployés

Le déploiement Kubernetes inclut les éléments suivants, définis dans les fichiers YAML du répertoire `kubernetes/` :

* **Secrets (`00-secrets.yaml`) :** Stocke de manière sécurisée les informations sensibles telles que les mots de passe de base de données et les clés API des services d'IA.
  **ATTENTION : Ne JAMAIS commit vos véritables clés API ou mots de passe dans ce fichier lors d'un push vers un dépôt public. Utilisez des placeholders pour la version contrôlée et remplacez-les localement.**
* **ConfigMaps (`01-configmaps.yaml`) :** Contient les configurations non sensibles pour les services, notamment les URLs de communication entre les microservices (ex: `GEMINI_SERVICE_URL`) et les profils Spring Boot actifs.
* **MySQL (`02-mysql.yaml`) :** Déploiement de la base de données MySQL avec persistance des données via un `PersistentVolumeClaim`.
* **API Backend (Java) (`03-api.yaml`) :** Déploiement de l'API principale qui interagit avec la base de données et les services d'IA. Cette API est configurée pour utiliser les variables d'environnement injectées par les ConfigMaps pour la communication inter-services.
* **Modules d'IA (NestJS) (`04-ai-modules.yaml`) :** Déploiement des services Gemini, OpenAI et Freepik, chacun exposé via un service Kubernetes pour une communication interne au cluster.
* **Frontend (React/TypeScript) (`05-frontend.yaml`) :** Déploiement de l'interface utilisateur web.

### Processus de Déploiement

Le script `deploy-kubernetes.bat` (situé dans `.github/workflows/kubernetes/` à la racine de votre 
projet) automatise l'application de tous les fichiers de configuration nécessaires.

1.  **Assurez-vous que vos images Docker sont à jour et poussées** vers un registre accessible par votre cluster Kubernetes.
    ```bash
    # Exemple pour l'API (à répéter pour web, gemini, openai, freepik si nécessaire)
    docker build -t eli256/jdr-generator-api:latest ./api
    docker push eli256/jdr-generator-api:latest
    ```
2.  **Appliquez les configurations Kubernetes :**
    Exécutez le script de déploiement (depuis la racine de votre projet) :
    ```bash
    ./.github/workflows/kubernetes/deploy-kubernetes.bat
    ```
    Ce script va successivement :
    * Supprimer les anciens services (si existants).
    * Appliquer les `Secrets`.
    * Appliquer les `ConfigMaps`.
    * Déployer MySQL.
    * Déployer l'API Backend.
    * Déployer les modules d'IA (Gemini, OpenAI, Freepik).
    * Déployer le Frontend.
    * Lancer les `port-forward` pour accéder à l'API et au Frontend depuis votre machine locale.

### Accès à l'Application

Une fois le déploiement terminé et les `port-forwards` actifs (lancés par `deploy-kubernetes.bat`), 
vous pouvez accéder à l'application :

* **Frontend Web :** `http://localhost:3080` 
* **API Backend :** `http://localhost:8080`

**Note Importante :** Les commandes `kubectl port-forward` bloquent le terminal où elles sont exécutées. 
Pour maintenir l'accès à l'application, ces fenêtres de terminal doivent rester ouvertes et les commandes actives. 
Ne les fermez pas tant que vous utilisez l'application !


![kubernetes--describe_port-forward](https://github.com/user-attachments/assets/d1699f4d-7eec-44be-97c9-8ee819d56213)


### Consultation des Logs

Pour diagnostiquer des problèmes ou simplement monitorer l'activité de vos applications, 
vous pouvez accéder aux logs des pods Kubernetes.

1.  **Obtenir le nom exact d'un pod :**
    Utilisez la commande `kubectl get pods` avec un sélecteur de label pour filtrer par 
    application et formater la sortie pour obtenir uniquement le nom du pod.
    ```bash
    kubectl get pods -l app=jdr-generator-api -o custom-columns=NAME:.metadata.name --no-headers
    # Exemples pour les autres modules :
    # kubectl get pods -l app=jdr-generator-gemini -o custom-columns=NAME:.metadata.name --no-headers
    # kubectl get pods -l app=jdr-generator-freepik -o custom-columns=NAME:.metadata.name --no-headers
    # kubectl get pods -l app=jdr-generator-openai -o custom-columns=NAME:.metadata.name --no-headers
    # kubectl get pods -l app=jdr-generator-web -o custom-columns=NAME:.metadata.name --no-headers
    ```

2.  **Afficher les logs d'un pod :**
    Une fois que vous avez le nom du pod (par exemple, `jdr-generator-api-f5464fc4-w4l9k`), vous 
    pouvez afficher ses logs en temps réel ou les exporter dans un fichier.
    * Afficher les logs en temps réel (stream) :
        ```bash
        kubectl logs -f <NOM_DU_POD>
        # Exemple : kubectl logs -f jdr-generator-api-f5464fc4-w4l9k
        ```
    * Exporter les logs dans un fichier (les fichiers s'ajouteront dans le path ou vous êtes situé) :
        ```bash
        kubectl logs <NOM_DU_POD> > nom_du_fichier_logs.txt
        # Exemple : kubectl logs jdr-generator-api-f5464fc4-w4l9k > api_logs.txt
        ```
    Remplacez `<NOM_DU_POD>` par le nom réel du pod que vous avez obtenu à l'étape précédente.

### Communication Inter-services

Les services communiquent entre eux au sein du cluster Kubernetes en utilisant leurs noms de service
définis dans les fichiers YAML (ex: `http://gemini-container:3001` pour le module Gemini). 
Les `ConfigMaps` et la configuration des applications Spring Boot 
(comme avec `GEMINI_API_URL: ${GEMINI_SERVICE_URL:-http://localhost:3001}/gemini`) assurent que les 
bonnes URLs sont utilisées en production dans Kubernetes tout en permettant un développement local facilité.


___________
# Licence

```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```
