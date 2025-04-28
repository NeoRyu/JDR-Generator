REM Arrête et supprime les conteneurs existants
docker stop jdr-web jdr-api jdr-gemini jdr-mysql
docker rm jdr-web jdr-api jdr-gemini jdr-mysql

REM Supprime le réseau existant (ignore l'erreur si le réseau n'existe pas)
docker network rm jdr-generator-net || true

REM Se déplace vers le répertoire de base du projet
cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator\

REM Construction des images Docker
cd api
docker build -t eli256/jdr-generator-api:latest .
cd ..
cd gemini
docker build -t eli256/jdr-generator-gemini:latest .
cd ..
cd web
docker build -t eli256/jdr-generator-web:latest .
cd ..

REM Création du réseau Docker
docker network create jdr-generator-net

REM Démarrage des conteneurs
docker run -d --name jdr-mysql --network jdr-generator-net -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jdr_generator mysql:latest
docker run -d --name jdr-api --network jdr-generator-net -p 8080:8080 -e SPRING_PROFILES_ACTIVE=docker -e SPRING_DATASOURCE_URL="jdbc:mysql://jdr-mysql:3306/jdr_generator?verifyServerCertificate=false&autoReconnect=true&useSSL=false&requireSSL=false&serverTimezone=Europe/Amsterdam&port=3307" -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root eli256/jdr-generator-api:latest
docker run -d --name jdr-gemini --network jdr-generator-net -p 3000:3000 eli256/jdr-generator-gemini:latest
docker run -d --name jdr-web --network jdr-generator-net -p 80:80 -e VITE_API_URL=http://jdr-api:8080 eli256/jdr-generator-web:latest
