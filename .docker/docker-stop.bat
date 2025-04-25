docker stop jdr-web jdr-api jdr-gemini jdr-mysql
docker rm jdr-web jdr-api jdr-gemini jdr-mysql
docker network rm jdr-generator-net || true
