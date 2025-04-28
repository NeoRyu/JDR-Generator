REM Assurez-vous d'être connecté à Docker Hub
docker login
REM Naviguation vers le répertoire de docker-compose.yml
cd C:/Users/fredericcoupez/IdeaProjects/JDR-Generator/.github/workflows
REM Construit toutes les images définies dans docker-compose.yml
docker-compose build
REM Pousse chaque image en utilisant les noms définis dans docker-compose.yml
docker push eli256/jdr-generator-api:latest
docker push eli256/jdr-generator-web:latest
docker push eli256/jdr-generator-gemini:latest
echo "Toutes les images ont été poussées vers le Docker Hub https://hub.docker.com/repositories/eli256 !"
