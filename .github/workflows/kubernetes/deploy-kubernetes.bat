@echo off
setlocal

REM ########################################################################
REM #                                                                      #
REM # Script de Déploiement pour Kubernetes depuis Kind via Docker Desktop #
REM #                                                                      #
REM ########################################################################

REM Ce script automatise les étapes de déploiement de l'application JDR Generator sur Kubernetes :
REM - Vous devez avoir Docker Desktop avec Kubernetes activé.
REM - kubectl est installé et configuré pour se connecter à votre cluster Docker Desktop.
REM - Les fichiers YAML (.github\workflows\kubernetes\*.yaml) sont au même niveau que ce fichier.
REM - Utiliser les dernières image Docker construite et poussée (eli256/jdr-generator-***:latest).

echo.
echo ====================================================================
echo == Démarrage du processus de déploiement Kubernetes JDR Generator ==
echo ====================================================================
echo.

REM --- ÉTAPE 1: Suppression des services existants par leurs anciens noms ---
echo.
echo --- ÉTAPE 1: Suppression des services existants par leurs anciens noms ---
echo.
echo Les erreurs "not found" sont normales si certains services n'existent pas encore.
kubectl delete service jdr-generator-api --ignore-not-found=true
kubectl delete service jdr-generator-mysql --ignore-not-found=true
kubectl delete service jdr-generator-gemini --ignore-not-found=true
kubectl delete service jdr-generator-openai --ignore-not-found=true
kubectl delete service jdr-generator-freepik --ignore-not-found=true
echo.
echo Services anciens supprimés ou non trouvés.

echo.
echo Suppression des images docker
echo docker-compose -p jdr-generator -f docker-compose.local.yml down --volumes --rmi all
echo docker rmi eli256/jdr-generator-web:latest
echo docker rmi eli256/jdr-generator-api:latest

REM --- ÉTAPE 2: Application des Secrets ---
echo.
echo --- ÉTAPE 2: Application des Secrets (00-secrets.yaml) ---
echo.
echo Assurez-vous que 00-secrets.yaml contient vos VRAIES clés/mots de passe encodés en Base64.
kubectl apply -f 00-secrets.yaml
if %errorlevel% neq 0 (
    echo ERREUR: Échec de l'application de 00-secrets.yaml. Arrêt du script.
    goto :eof
)
echo Secrets appliqués : kubectl get secrets

REM --- ÉTAPE 3: Application des ConfigMaps mises à jour ---
echo.
echo --- ÉTAPE 3: Application des ConfigMaps mises a jour ---
echo.
echo Application de 01-configmaps.yaml (contient les noms de services pour les calls inter-modules)
kubectl apply -f 01-configmaps.yaml
if %errorlevel% neq 0 (
    echo ERREUR: Échec de l'application de 01-configmaps.yaml. Arrêt du script.
    goto :eof
)
echo ConfigMaps appliquées : kubectl get configmaps

REM --- ÉTAPE 4: Application des déploiements et services mis à jour ---
echo.
echo --- ÉTAPE 4: Application des déploiements et services mis a jour ---
echo.
echo Application des fichiers de déploiement et service.
kubectl apply -f 02-mysql.yaml
if %errorlevel% neq 0 (echo ERREUR: Échec de 02-mysql.yaml. Arrêt. & goto :eof)
echo Verification des PersistentVolumeClaims (PVC) :
kubectl get pvc
echo.
kubectl apply -f 03-api.yaml
if %errorlevel% neq 0 (echo ERREUR: Échec de 03-api.yaml. Arrêt. & goto :eof)
echo.
kubectl apply -f 04-ai-modules.yaml
if %errorlevel% neq 0 (echo ERREUR: Échec de 04-ai-modules.yaml. Arrêt. & goto :eof)
echo.
kubectl apply -f 05-frontend.yaml
if %errorlevel% neq 0 (echo ERREUR: Échec de 05-frontend.yaml. Arrêt. & goto :eof)
echo.
echo Déploiements et Services appliqués.

REM --- ÉTAPE 5: Redémarrage de tous les déploiements ---
echo.
echo --- ÉTAPE 5: Redémarrage de tous les déploiements pour prendre en compte les changements ---
echo.
echo Ceci assure que les pods récupèrent les nouvelles ConfigMaps et les résolutions DNS.
kubectl rollout restart deployment/jdr-generator-api
kubectl rollout restart deployment/jdr-generator-mysql
kubectl rollout restart deployment/jdr-generator-gemini
kubectl rollout restart deployment/jdr-generator-openai
kubectl rollout restart deployment/jdr-generator-freepik
kubectl rollout restart deployment/jdr-generator-web
echo Redémarrages des déploiements initiés.
kubectl rollout status deployment/jdr-generator-gemini
kubectl rollout status deployment/jdr-generator-openai
kubectl rollout status deployment/jdr-generator-freepik
kubectl rollout status deployment/jdr-generator-mysql
kubectl rollout status deployment/jdr-generator-api
kubectl rollout status deployment/jdr-generator-web
echo "Redémarrages des déploiements terminé."


REM --- ÉTAPE 6: Vérification des états ---
echo.
echo --- ÉTAPE 6: Vérification des états ---
echo.
echo Vérification des Deployment (état 'READY' et 'AVAILABLE' pour chacun):
kubectl get deployments
echo.
echo Vérification des noms des Service :
kubectl get services
echo.
echo Vérification de l'état des Pods (tous devraient être 'Running' 1/1):
kubectl get pods
echo.
echo Vérification de l'état détaillé des noeuds (infrastructure du cluster):
kubectl get nodes -o wide
echo.

REM --- ÉTAPE 7: Lancement des port-forwards ---
echo.
echo ===================================================================
echo == Déploiement Terminé ! Lancement automatique des Port-Forwards ==
echo ===================================================================
echo.
echo --- ÉTAPE 7: Lancement des port-forwards ---
echo.

echo Lancement du port-forward pour le backend API dans un nouveau terminal...
echo - NOTE : Un instantané de 'describe' s'affichera, puis le terminal sera bloqué par 'port-forward'.
start powershell.exe -NoExit -Command "kubectl describe deployment/jdr-generator-api; kubectl port-forward svc/api-container 8080:8080"
REM
REM  L'application frontend a besoin de communiquer avec API pour récupérer et envoyer des données.
REM  Ce 'port-forward' mappe localhost:8080 de la machine au service API (api-container:8080)
REM  à l'intérieur du cluster Kubernetes, permettant ainsi au frontend local (via le proxy Nginx)
REM  d'atteindre le backend API.
REM
echo.

echo Lancement du port-forward pour le frontend WEB dans un autre nouveau terminal...
echo - NOTE : Un instantané de 'describe' s'affichera, puis le terminal sera bloqué par 'port-forward'.
start powershell.exe -NoExit -Command "kubectl describe deployment/jdr-generator-web; kubectl port-forward svc/web-container 3080:80"
REM
REM Pour que le navigateur puisse accéder à une application qui tourne dans Kubernetes, nous devons
REM transférer le trafic du port 3080 de la machine locale vers le port 80 du conteneur du frontend
REM dans le cluster. localhost:8080 sera alors le point d'entrée principal pour l'utilisateur final.
REM
echo.

REM
REM Pas de port-forward pour mysql, gemini, openai et freepik.
REM Le service API, lorsqu'il est execute a l'intérieur du cluster Kubernetes,
REM sait comment contacter ces services via leurs noms de service internes.
REM
echo.
echo Les fenêtres de port-forwarding devraient maintenant être ouvertes. Ne pas les fermer.
echo.

REM --- ÉTAPE 8 : Accès aux logs applicatif ---
echo.
echo =====================================================
echo == ÉTAPE 8 : Accès aux logs applicatifs pour debug ==
echo =====================================================
echo.
echo Pour accéder aux logs d'un pod (par ex API), utilisez la commande suivante pour obtenir son NAME :
echo kubectl get pods -l app=jdr-generator-api
echo.
echo Vous devriez obtenir une réponse similaire à cet exemple :
echo NAME                               READY   STATUS    RESTARTS   AGE
echo jdr-generator-api-84fd8cd8-rd56n   1/1     Running   0          72s
echo.
echo Suivi de cette commande en remplaçant par le bon NAME :
echo kubectl logs jdr-generator-api-84fd8cd8-rd56n
echo.

pause