REM Création de l'environnement dockerisé
cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator\.github\workflows
docker-compose down
docker volume rm workflows_mysql-data
docker-compose up -d
REM docker-compose up --build -d web-container api-container mysql-container gemini-container

REM docker-compose ps
REM docker-compose logs api-container
REM docker-compose build web-container
