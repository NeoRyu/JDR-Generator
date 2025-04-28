@echo off
REM Définition des variables
SET PROJ_DIR="C:\Users\fredericcoupez\IdeaProjects\JDR-Generator"
SET WORKFLOW_DIR=%PROJ_DIR%"\.github\workflows"
SET API_IMAGE="eli256/jdr-generator-api:latest"
SET GEMINI_IMAGE="eli256/jdr-generator-gemini:latest"
SET WEB_IMAGE="eli256/jdr-generator-web:latest"
@echo on

REM Arrêt et suppression des conteneurs existants
docker stop jdr-web jdr-api jdr-gemini jdr-mysql
docker rm jdr-web jdr-api jdr-gemini jdr-mysql

REM Suppression du réseau existant (ignore l'erreur si le réseau n'existe pas)
docker network rm jdr-generator-net || true

REM Création du réseau Docker
docker network create jdr-generator-net

REM Déploiement et exécution des conteneurs
cd %PROJ_DIR%

REM Construction de l'image 'eli256/jdr-generator-api' et lancement du conteneur
(
    cd api && docker build -t %API_IMAGE% .
) && (
    docker run -d --name jdr-api --network jdr-generator-net -p 8080:8080 -e SPRING_PROFILES_ACTIVE=docker -e SPRING_DATASOURCE_URL="jdbc:mysql://jdr-mysql:3306/jdr_generator?verifyServerCertificate=false&autoReconnect=true&useSSL=false&requireSSL=false&serverTimezone=Europe/Amsterdam&port=3307" -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root %API_IMAGE%
)

REM Construction de l'image 'eli256/jdr-generator-gemini' et lancement du conteneur
(
    cd gemini && docker build -t %GEMINI_IMAGE% .
) && (
    docker run -d --name jdr-gemini --network jdr-generator-net -p 3000:3000 %GEMINI_IMAGE%
)

REM Construction de l'image 'eli256/jdr-generator-web' et lancement du conteneur
(
    cd web && docker build -t %WEB_IMAGE% .
) && (
    docker run -d --name jdr-web --network jdr-generator-net -p 80:80 -e VITE_API_URL=http://jdr-api:8080 %WEB_IMAGE%
)

REM Push des images vers Docker Hub
cd %PROJ_DIR%
docker login

REM Pull des images de base pour optimiser la construction des images
docker pull maven:3.8.5-openjdk-17
docker pull eclipse-temurin:17-jre-alpine

(cd api && docker build -t %API_IMAGE% . && docker push %API_IMAGE%)
cd ..
(cd web && docker build -t %WEB_IMAGE% . && docker push %WEB_IMAGE%)
cd ..
(cd gemini && docker build -t %GEMINI_IMAGE% . && docker push %GEMINI_IMAGE%)
cd ..

REM Création de l'environnement dockerisé avec Docker Compose
cd %WORKFLOW_DIR%
docker-compose down
docker-compose up -d

REM Débogage Docker
docker ps
docker-compose ps
type docker-compose.yml
docker network ls

REM Inspection des logs et des conteneurs
docker logs jdr-mysql
docker inspect jdr-mysql
docker logs jdr-api
docker inspect jdr-api
docker logs jdr-gemini
docker inspect jdr-gemini
docker logs jdr-web
docker inspect jdr-web
