cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator
cd api\
docker build -t eli256/jdr-generator-api:latest .
cd ..
cd gemini\
docker build -t eli256/jdr-generator-gemini:latest .
cd ..
cd web\
docker build -t eli256/jdr-generator-web:latest .
cd ..
