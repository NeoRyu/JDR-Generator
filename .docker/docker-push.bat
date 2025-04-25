cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator
docker login
cd api
docker pull maven:3.8.5-openjdk-17
docker pull eclipse-temurin:17-jre-alpine
docker build -t eli256/jdr-generator-api:latest .
docker push eli256/jdr-generator-api:latest
cd ..
cd web
docker build -t eli256/jdr-generator-web:latest .
docker push eli256/jdr-generator-web:latest
cd ..
cd gemini
docker build -t eli256/jdr-generator-gemini:latest .
docker push eli256/jdr-generator-gemini:latest
cd ..
