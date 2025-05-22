REM ====================================================
REM Étape 1 : Connexion à Docker Hub
REM ====================================================
echo.
echo Tentative de connexion à Docker Hub...
docker login || (
    echo Erreur lors de la connexion à Docker Hub. Abandon.
    GOTO :EOF
)
echo Connexion Docker Hub réussie.
echo.

REM ====================================================
REM Étape 2 : Navigation vers le répertoire correct
REM ====================================================
echo Navigation vers le répertoire de docker-compose.local.yml...
cd C:/Users/fredericcoupez/IdeaProjects/JDR-Generator/ || (
    echo Erreur : Impossible de naviguer vers le répertoire de travail. Abandon.
    GOTO :EOF
)
echo Répertoire actuel : %CD%
echo.

REM ====================================================
REM Étape 3 : Génération du tag (AAAAMMJJ_HHMMSS)
REM ====================================================
echo Génération du tag temporel (cross-platform) via Docker...
REM Utilise un petit conteneur busybox pour obtenir la date/heure en utilisant le fuseau horaire de la machine hôte
for /f %%i in ('docker run --rm busybox /bin/sh -c "TZ=GMT-2 date +%%Y%%m%%d_%%H%%M%%S"') do set TAG=%%i
echo Utilisation du tag : %TAG%
echo.

REM ====================================================
REM Étape 4 : Construction des images Docker Compose
REM ====================================================
REM Construit toutes les images définies dans docker-compose.local.yml
docker-compose -p jdr-generator -f docker-compose.local.yml build || (
    echo Erreur lors de la construction des images localhost Docker : Abandon...
    GOTO :EOF
)
echo Construction des images terminée.
echo.

REM ====================================================
REM Étape 5 : Tagging des images construites
REM ====================================================
echo Tagging des images pour Docker Hub...
docker tag eli256/jdr-generator-api:latest eli256/jdr-generator-api:%TAG%
docker tag eli256/jdr-generator-web:latest eli256/jdr-generator-web:%TAG%
docker tag eli256/jdr-generator-gemini:latest eli256/jdr-generator-gemini:%TAG%
docker tag eli256/jdr-generator-openai:latest eli256/jdr-generator-openai:%TAG%
echo Tagging des images terminé.
echo.

REM ====================================================
REM Étape 6 : Poussée des images vers Docker Hub
REM ====================================================
echo Poussée des images vers Docker Hub...
docker push eli256/jdr-generator-api:%TAG% || (echo Erreur de push api. && GOTO :EOF)
docker push eli256/jdr-generator-api:latest || (echo Erreur de push api. && GOTO :EOF)
docker push eli256/jdr-generator-web:%TAG% || (echo Erreur de push web. && GOTO :EOF)
docker push eli256/jdr-generator-web:latest || (echo Erreur de push web. && GOTO :EOF)
docker push eli256/jdr-generator-gemini:%TAG% || (echo Erreur de push gemini. && GOTO :EOF)
docker push eli256/jdr-generator-gemini:latest || (echo Erreur de push gemini. && GOTO :EOF)
docker push eli256/jdr-generator-openai:%TAG% || (echo Erreur de push openai. && GOTO :EOF)
docker push eli256/jdr-generator-openai:latest || (echo Erreur de push openai. && GOTO :EOF)
echo Poussée des images terminée.
echo.
echo Toutes les images ont été poussées vers le Docker Hub (TAG : %TAG%)
echo Vérifier sur : https://hub.docker.com/repositories/eli256/
echo.