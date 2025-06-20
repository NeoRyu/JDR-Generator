@echo off
setlocal

REM ######################################################################
REM #-- Script de Déploiement de JDR-GENERATOR depuis Kubernetes HELM  --#
REM # Automatisation de : test, nettoyage, déploiement et port-forwarding#
REM #                                                                    #
REM # Prérequis :                                                        #
REM # - Docker Desktop avec Kubernetes activé et en cours d'exécution.   #
REM # - kubectl configuré pour se connecter à votre cluster Kubernetes.  #
REM # - Helm installé et configuré.                                      #
REM # - Exécuter ce script depuis le répertoire 'jdr-generator-chart'.   #
REM ######################################################################

echo.
echo ===================================================================
echo == Démarrage du processus de Déploiement Helm de JDR Generator   ==
echo ===================================================================
echo.

echo =============================
echo --- PHASE DE TESTS : HELM ---
echo =============================
echo.
echo Exécution de helm lint...
helm lint .
if %errorlevel% neq 0 (
    echo ERREUR: helm lint a échoué. Veuillez corriger les erreurs avant de continuer.
    pause
    goto :eof
)
echo helm lint terminé avec succès.
echo.
echo Génération du YAML rendu par Helm (output.yaml)...
helm template . > output.yaml
echo Fichier output.yaml généré.
REM OPTIONNEL: Générer output.yaml avec les secrets (NE PAS COMMITER)
REM echo.
REM echo Génération de output_with_secrets.yaml (avec valeurs de secrets - NE PAS COMMITER SUR GIT !)...
REM helm template . -f values.yaml -f ../ai-secrets-values.yaml > output_with_secrets.yaml --debug
REM echo Fichier output_with_secrets.yaml généré.

echo.
echo ==============================================
echo --- PHASE DE NETTOYAGE : KUBERNETES / HELM ---
echo ==============================================
echo.
echo INFO : Pour désinstaller complètement la release Helm 'jdr-generator' :
echo        helm uninstall jdr-generator --ignore-not-found=true
echo        kubectl delete pvc jdr-generator-jdr-generator-chart-mysql-pv-claim
REM # helm get all jdr-generator # pour vérifier la suppression
echo ATTENTION : Cela provoquera une perte irréversible des personnages générés !!!
echo.
echo        ---------------------------------------
echo.
echo Début du nettoyage des ressources Kubernetes existantes pour éviter les conflits...
echo.
REM Nettoyage explicite des services de l'ancien déploiement manuel (qui n'incluent pas 'jdr-generator' dans leur nom)
echo Suppression des services d'un déploiement Kubernetes "vanilla" si existants...
kubectl delete service api-container freepik-container gemini-container mysql-container openai-container web-container --ignore-not-found=true
echo.
REM Nettoyage dynamique de TOUTES les ressources liées à jdr-generator qui pourraient subsister.
REM Utilise findstr pour filtrer et une boucle for /f pour itérer sur les noms de ressources.
echo Identification et suppression dynamique des ressources résiduelles 'jdr-generator' (Pods, Services, Deployments, ReplicaSets, Secrets, ConfigMaps)...
for /f "tokens=*" %%a in ('kubectl get all -o name ^| findstr "jdr-generator"') do (
    echo Suppression de %%a
    kubectl delete %%a --ignore-not-found=true
)
REM Nettoyage explicite des reliquat HELM du précédent déploiement :
kubectl delete deployment jdr-generator-jdr-generator-chart-freepik --ignore-not-found=true
kubectl delete deployment jdr-generator-jdr-generator-chart-gemini --ignore-not-found=true
kubectl delete deployment jdr-generator-jdr-generator-chart-openai --ignore-not-found=true
kubectl delete deployment jdr-generator-jdr-generator-chart-mysql --ignore-not-found=true
kubectl delete deployment jdr-generator-jdr-generator-chart-api --ignore-not-found=true
kubectl delete deployment jdr-generator-jdr-generator-chart-web --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-freepik-service --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-gemini-service --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-openai-service --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-mysql-service --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-api-service --ignore-not-found=true
kubectl delete service jdr-generator-jdr-generator-chart-web-service --ignore-not-found=true
echo.
REM Nettoyage explicite des secrets qui pourraient persister et causer des problèmes si non supprimés par 'get all'
REM Note: 'get all' n'inclut pas toujours tous les types de ressources et les secrets spécifiques peuvent être problématiques.
echo Suppression des secrets
kubectl delete secret jdr-generator-mysql-secrets --ignore-not-found=true
kubectl delete secret jdr-generator-ai-secrets --ignore-not-found=true
echo.
echo.
echo Identification des PersistentVolumeClaims 'jdr-generator'...
for /f "tokens=*" %%a in ('kubectl get pvc -o name ^| findstr "jdr-generator"') do (
    echo INFO : Suppression possible de %%a via :
    echo # kubectl delete %%a --ignore-not-found=true
)
echo.
echo Attente de 10 secondes pour le nettoyage des ressources...
ping -n 11 127.0.0.1 > nul
echo.
echo Ressources 'jdr-generator' non supprimées :
kubectl get all -o name | findstr "jdr-generator"
kubectl get pvc -o name | findstr "jdr-generator"
echo.
echo Nettoyage terminé.

echo.
echo ===================================
echo --- PHASE DE DÉPLOIEMENT : HELM ---
echo ===================================
echo.
echo Packaging du chart Helm...
helm package .
if %errorlevel% neq 0 (
    echo ERREUR: Le packaging du chart Helm a échoué.
    pause
    goto :eof
)
echo Chart Helm packagé avec succès.
echo.

echo Saisie des clés API (non-silencieuse en Batch)
set /p GEMINI_API_KEY_INPUT="- Votre API KEY pour GEMINI : "

set /p FREEPIK_API_KEY_INPUT="- Votre API KEY pour FREEPIK : "

set /p OPENAI_API_KEY_INPUT="- Votre API KEY pour OPENAI : "

set /p OPENAI_ORG_ID_INPUT="-- Et OPENAI_ORG_ID associé : "
echo.

if not defined GEMINI_API_KEY_INPUT goto :missing_gemini_key
if not defined FREEPIK_API_KEY_INPUT goto :missing_freepik_key
if not defined OPENAI_API_KEY_INPUT goto :missing_openai_key
if not defined OPENAI_ORG_ID_INPUT goto :missing_openai_org_id_key
echo Utilisation des valeurs saisies pour injecter les clés API via --set
helm upgrade --install jdr-generator ./ ^
  -f values.yaml ^
  --set secrets.apiKeys.data.GEMINI_API_KEY="%GEMINI_API_KEY_INPUT%" ^
  --set secrets.apiKeys.data.FREEPIK_API_KEY="%FREEPIK_API_KEY_INPUT%" ^
  --set secrets.apiKeys.data.OPENAI_API_KEY="%OPENAI_API_KEY_INPUT%" ^
  --set secrets.apiKeys.data.OPENAI_ORG_ID="%OPENAI_ORG_ID_INPUT%"
if %errorlevel% neq 0 (
    echo ERREUR: L'installation du chart Helm a échouée ...
    pause
    goto :eof
)
echo Chart Helm installe avec succès !
echo.
goto :skip_key_error_checks
:missing_gemini_key
echo ERREUR: GEMINI_API_KEY non saisie. L'installation est annulée.
pause
goto :eof
:missing_freepik_key
echo ERREUR: FREEPIK_API_KEY non saisie. L'installation est annulée.
pause
goto :eof
:missing_openai_key
echo ERREUR: OPENAI_API_KEY non saisie. L'installation est annulée.
pause
goto :eof
:missing_openai_org_id_key
echo ERREUR: OPENAI_ORG_ID non saisie. L'installation est annulée.
pause
goto :eof
:skip_key_error_checks

echo Vérification de l'état initial des Deployments...
kubectl get deployments
echo.
echo En attente du déploiement complet (états stables et 'Ready') ...
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-freepik
if %errorlevel% neq 0 (echo ERREUR: Déploiement Freepik non prêt. && pause && goto :eof)
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-gemini
if %errorlevel% neq 0 (echo ERREUR: Déploiement Gemini non prêt. && pause && goto :eof)
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-openai
if %errorlevel% neq 0 (echo ERREUR: Déploiement OpenAI non prêt. && pause && goto :eof)
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-mysql
if %errorlevel% neq 0 (echo ERREUR: Déploiement MySQL non prêt. && pause && goto :eof)
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-api
if %errorlevel% neq 0 (echo ERREUR: Déploiement API non prêt. && pause && goto :eof)
kubectl rollout status deployment/jdr-generator-jdr-generator-chart-web
if %errorlevel% neq 0 (echo ERREUR: Déploiement WEB non prêt. && pause && goto :eof)
echo.
echo Tous les déploiements sont maintenant prêts et stables !

echo.
echo ====================================
echo --- PHASE D'ANALYSE : KUBERNETES ---
echo ====================================
echo.
echo Vérification de l'état des Deployments...
kubectl get deployments
echo.
echo Vérification de l'état des Services...
kubectl get services
echo.
echo Vérification de l'état des Pods...
kubectl get pods
echo.

echo.
echo ======================================
echo --- PHASE D'EXÉCUTION : KUBERNETES ---
echo ======================================
echo.
echo --- PORTS FORWARDING (dans des terminaux séparés) ---
echo.
echo Lancement du port-forward pour le backend API localhost (8080:8080)...
start powershell.exe -NoExit -Command "kubectl port-forward service/jdr-generator-jdr-generator-chart-api-service 8080:8080"
REM Le '-NoExit' maintient la fenêtre PowerShell ouverte après l'exécution de la commande.
echo Lancement du port-forward pour le frontend WEB localhost (3080:80)...
start powershell.exe -NoExit -Command "kubectl port-forward service/jdr-generator-jdr-generator-chart-web-service 3080:80"
echo.
echo Les fenêtres de port-forwarding devraient maintenant être ouvertes. NE LES FERMEZ PAS tant que vous utilisez l'application.
echo.
echo Vous pouvez maintenant accéder à l'application via : http://localhost:3080
echo ( L'écoute de services du backend API est accessible via : http://localhost:8080/ ...  )
echo.
echo --- DEBUGGING (Commandes utiles en cas de problème) ---
echo.
echo - Pour lister les pod du cluster Kubernetes avec un label spécifique (par exemple api) :
echo   kubectl get pods -l app.kubernetes.io/component=api
echo.
echo - Pour inspecter un pod qui ne démarre pas :
echo   kubectl describe pod ^<nom_du_pod^>
echo.
echo - Pour voir les logs d'un conteneur :
echo   kubectl logs ^<nom_du_pod^>
echo.
echo - Pour obtenir l'état détaillé de votre noeud :
echo   kubectl describe node desktop-worker
echo.
echo - Pour voir les événements récents dans le cluster :
echo   kubectl get events --sort-by^='.lastTimestamp^'
echo.

pause