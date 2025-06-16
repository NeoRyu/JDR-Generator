@echo off
setlocal ENABLEDELAYEDEXPANSION

REM ========================
REM Configuration des Images
REM ========================
set "ERROR_COUNT=0"
REM Définition de la liste des images
set "IMAGES_TO_PROCESS=api-container web-container gemini-container openai-container freepik-container"

REM ================================
REM Étape 1 : Connexion à Docker Hub
REM ================================
echo.
echo Tentative de connexion à Docker Hub...
docker login || (
    echo ERREUR : Erreur lors de la connexion à Docker Hub. Abandon.
    set /a ERROR_COUNT+=1
    GOTO :EOF
)
echo Connexion Docker Hub réussie !
echo.

REM ===============================================
REM Étape 2 : Navigation vers le répertoire correct
REM ===============================================
echo Navigation vers le répertoire de docker-compose.local.yml...
cd C:/Users/fredericcoupez/IdeaProjects/JDR-Generator/ || (
    echo ERREUR : Impossible de naviguer vers le répertoire de travail. Abandon.
    set /a ERROR_COUNT+=1
    GOTO :EOF
)
echo Répertoire actuel : %CD%
echo.

REM =============================================
REM Étape 3 : Génération du tag (AAAAMMJJ_HHMMSS)
REM =============================================
echo Génération du tag temporel (cross-platform) via Docker...
for /f %%i in ('docker run --rm busybox /bin/sh -c "TZ=GMT-2 date +%%Y%%m%%d_%%H%%M%%S"') do set TAG=%%i
echo Utilisation du tag : %TAG%
echo.

REM ========================================
REM Étape 4 : Sélection des images à traiter
REM ========================================
echo.
echo ====================================================
echo == Choix des images a construire et pousser ==
echo ====================================================
echo.
echo Voici les services disponibles : %IMAGES_TO_PROCESS%
echo.

:ask_choice_again
set /p CHOICE="Voulez-vous construire et pousser TOUTES les images (T) ou SELECTIONNER (S) ? [T/S]: "
echo.

set "SELECTED_IMAGES_TEMP="
if /i "%CHOICE%"=="T" (
    echo Choix : Toutes les images.
    set "SELECTED_IMAGES_TEMP=%IMAGES_TO_PROCESS%"
) else if /i "%CHOICE%"=="S" (
    echo Choix : Selection manuelle.
    echo.
    for %%A in (%IMAGES_TO_PROCESS%) do (
        set /p PUSH_THIS="Voulez-vous construire et pousser 'eli256/jdr-generator-%%A' ? [O/N]: "
        if /i "!PUSH_THIS!"=="O" (
            set "SELECTED_IMAGES_TEMP=!SELECTED_IMAGES_TEMP! %%A"
        )
    )
    if "!SELECTED_IMAGES_TEMP!"=="" (
        echo ERREUR : Aucune image sélectionnée. Abandon.
        set /a ERROR_COUNT+=1
        GOTO :EOF
    )
    echo Images sélectionnées : !SELECTED_IMAGES_TEMP!
) else (
    echo ERREUR : Choix invalide. Abandon.
    set /a ERROR_COUNT+=1
    GOTO :EOF
)
echo.

set "ACTUAL_SELECTED_IMAGES=!SELECTED_IMAGES_TEMP!"

REM ==========================================================
REM Étape 5 : Construction des images Docker Compose (filtrée)
REM ==========================================================
REM Initialisation de la liste des services qui ont été construits avec succès
set "SUCCESSFULLY_BUILT_SERVICES="
echo Construction des images sélectionnées...
for %%A in (!ACTUAL_SELECTED_IMAGES!) do (
    echo * Construction de eli256/jdr-generator-%%A...
    docker-compose -p jdr-generator -f docker-compose.local.yml build %%A
    if !ERRORLEVEL! NEQ 0 (
        echo ERREUR : La construction du service %%A a échouée. Il ne sera pas poussé.
        set /a ERROR_COUNT+=1
    ) else (
        echo Construction du service %%A réussie.
        set "SUCCESSFULLY_BUILT_SERVICES=!SUCCESSFULLY_BUILT_SERVICES! %%A"
    )
)
echo Construction des images terminée.
echo.

REM =====================================================
REM Étape 6 : Tagging et Poussée des images sélectionnées
REM =====================================================
if "!SUCCESSFULLY_BUILT_SERVICES!"=="" (
    echo Aucun service n'a réussi à être construit : Aucune image a pousser. Abandon.
    echo.
    goto :EOF
) else (
    echo Tagging et Poussée des images build vers Docker Hub
    for %%A in (!SUCCESSFULLY_BUILT_SERVICES!) do (
        REM Initialise le drapeau pour cette itération
        set "FLAG_ERROR=0"
        set "SHORT_NAME="

        REM Extraction du nom de module depuis le service pour définir SHORT_NAME
        for /f "tokens=1 delims=-" %%B in ("%%A") do (
            set "SHORT_NAME=%%B"
        )

        if "!SHORT_NAME!"=="" (
            echo ERREUR : Impossible d'extraire SHORT_NAME pour %%A. Poursuite avec l'image suivante
            set /a ERROR_COUNT+=1
            set "FLAG_ERROR=1"
        )

        if "!FLAG_ERROR!"=="0" (
            echo Tagging de eli256/jdr-generator-!SHORT_NAME!
            docker tag eli256/jdr-generator-!SHORT_NAME!:latest eli256/jdr-generator-!SHORT_NAME!:%TAG%
            if !ERRORLEVEL! NEQ 0 (
                echo ERREUR : Erreur de tagging !SHORT_NAME!. Le push est ignoré pour cette image
                set /a ERROR_COUNT+=1
                set "FLAG_ERROR=1" REM On met le drapeau à 1 pour sauter les étapes suivantes
            )
        )

        if "!FLAG_ERROR!"=="0" (
            echo Poussée de eli256/jdr-generator-!SHORT_NAME! (:TAG)
            docker push eli256/jdr-generator-!SHORT_NAME!:%TAG%
            if !ERRORLEVEL! NEQ 0 (
                echo ERREUR : Erreur de push !SHORT_NAME! avec TAG
                set /a ERROR_COUNT+=1
                set "FLAG_ERROR=1"
            )
        )

        if "!FLAG_ERROR!"=="0" (
            echo Poussée de eli256/jdr-generator-!SHORT_NAME! (:LATEST)
            docker push eli256/jdr-generator-!SHORT_NAME!:latest
            if !ERRORLEVEL! NEQ 0 (
                echo ERREUR: Erreur de push !SHORT_NAME! avec LATEST
                set /a ERROR_COUNT+=1
                set "FLAG_ERROR=1"
            )
        )

        if "!FLAG_ERROR!"=="0" (
            echo Poussée de eli256/jdr-generator-!SHORT_NAME! terminée
            set "FLAG_ERROR=0"
        )
    )
    echo Poussée des images terminée
    set "FLAG_ERROR=0"
)
echo.
echo Toutes les images sélectionnées ont ete poussées vers le Docker Hub (TAG : %TAG%)
echo Vérifier sur : https://hub.docker.com/repositories/eli256/
echo.
echo Nombre total d'erreurs levées : %ERROR_COUNT%
echo.

endlocal
GOTO :EOF