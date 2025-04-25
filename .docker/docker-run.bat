docker network create jdr-generator-net
docker run -d --name jdr-mysql --network jdr-generator-net -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jdr_generator mysql:latest
docker run -d --name jdr-api --network jdr-generator-net -p 8080:8080 -e DB_HOST=jdr-mysql eli256/jdr-generator-api:latest --spring.profiles.active=docker
docker run -d --name jdr-gemini --network jdr-generator-net -p 3000:3000 eli256/jdr-generator-gemini:latest
docker run -d --name jdr-web --network jdr-generator-net -p 80:80 -e API_URL=http://jdr-api:8080 eli256/jdr-generator-web:latest
