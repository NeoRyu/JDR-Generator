#!/bin/bash

# "======================================================"
# --- HOW TO USE IT ---
# "======================================================"
# Ouvrez un terminal sur Linux/Unix, ou Git Bash sur Windows.
# Git Bash : https://git-scm.com/downloads/win

# Naviguez jusqu'au répertoire où vous avez enregistré le fichier, par exemple :
# cd ~/Desktop

# Puis exécutez :
# chmod +x JDR-Generator_start_app_standalone-TODO.sh

# Exécutez le script :
# ./JDR-Generator_start_app_standalone-TODO.sh

# "======================================================"
# --- Configuration du projet ---
# "======================================================"
PROJECT_NAME="jdr-generator"
NETWORK_NAME="${PROJECT_NAME}-network"
MYSQL_VOLUME_NAME="${PROJECT_NAME}_mysql-data"

# Noms des conteneurs
WEB_CONTAINER_NAME="${PROJECT_NAME}-web-container"
API_CONTAINER_NAME="${PROJECT_NAME}-api-container"
GEMINI_CONTAINER_NAME="${PROJECT_NAME}-gemini-container"
OPENAI_CONTAINER_NAME="${PROJECT_NAME}-openai-container"
MYSQL_CONTAINER_NAME="${PROJECT_NAME}-mysql-container"

# Images Docker Hub
WEB_IMAGE="eli256/jdr-generator-web:latest"
API_IMAGE="eli256/jdr-generator-api:latest"
GEMINI_IMAGE="eli256/jdr-generator-gemini:latest"
OPENAI_IMAGE="eli256/jdr-generator-openai:latest"
MYSQL_IMAGE="mysql:5.7"

# Contenu du fichier init.sql pour MySQL
# Ce fichier est utilisé par MySQL pour initialiser la base de
# données et l'utilisateur au premier démarrage.
# Il est intégré directement dans le script pour être autonome.
read -r -d '' INIT_SQL_CONTENT << EOF
CREATE DATABASE IF NOT EXISTS jdr_generator_db;
CREATE USER IF NOT EXISTS 'jdr_user'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON jdr_generator_db.* TO 'jdr_user'@'%';
FLUSH PRIVILEGES;
EOF
INIT_SQL_FILE="jdr-init.sql"

# "======================================================"
# --- Début du script ---
# "======================================================"
echo "--- Démarrage autonome de l'application JDR-Generator ---"

# 1. Vérifier si Docker Desktop est en cours d'exécution
echo "======================================================"
echo "Vérification de l'état de Docker Desktop..."
echo "======================================================"
if ! docker info > /dev/null 2>&1; then
  echo "Erreur : Docker Desktop n'est pas en cours d'exécution."
  echo "Veuillez démarrer Docker Desktop ou votre démon Docker avant d'exécuter ce script."
  exit 1
fi
echo "Docker Desktop est en cours d'exécution."

# 2. Nettoyage : Arrêter et supprimer les conteneurs, le réseau et les volumes existants
# pour le projet '${PROJECT_NAME}'...
echo "======================================================"
echo "Nettoyage des conteneurs, réseau et volumes précédents"
echo "======================================================"
# Supprime les conteneurs s'ils existent et sont arrêtés ou en cours d'exécution
docker stop ${WEB_CONTAINER_NAME} ${API_CONTAINER_NAME} ${GEMINI_CONTAINER_NAME} ${OPENAI_CONTAINER_NAME} ${MYSQL_CONTAINER_NAME} > /dev/null 2>&1
docker rm ${WEB_CONTAINER_NAME} ${API_CONTAINER_NAME} ${GEMINI_CONTAINER_NAME} ${OPENAI_CONTAINER_NAME} ${MYSQL_CONTAINER_NAME} > /dev/null 2>&1
# Supprime le réseau et le volume s'ils existent
docker network rm ${NETWORK_NAME} > /dev/null 2>&1
docker volume rm ${MYSQL_VOLUME_NAME} > /dev/null 2>&1
echo "Nettoyage terminé."

# 3. Créer le réseau virtuel Docker
echo "======================================================"
echo "Création du réseau Docker '${NETWORK_NAME}'"
echo "======================================================"
docker network create ${NETWORK_NAME}
if [ $? -ne 0 ]; then
  echo "Erreur lors de la création du réseau Docker. Abandon."
  exit 1
fi

# 4. Créer le fichier init.sql temporaire
echo "======================================================"
echo "Génération du fichier MySQL '${INIT_SQL_FILE}'"
echo "======================================================"
echo "${INIT_SQL_CONTENT}" > "${INIT_SQL_FILE}"
if [ $? -ne 0 ]; then
  echo "Erreur lors de la création du fichier init.sql. Abandon."
  exit 1
fi

# 5. Démarrer le conteneur MySQL
echo "======================================================"
echo "Démarrage du conteneur (${MYSQL_IMAGE})..."
echo "======================================================"
docker run -d \
  --name ${MYSQL_CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  -p 3307:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=jdr_generator_db \
  -e MYSQL_USER=jdr_user \
  -e MYSQL_PASSWORD=root \
  -v ${MYSQL_VOLUME_NAME}:/var/lib/mysql \
  -v "$(pwd)/${INIT_SQL_FILE}":/docker-entrypoint-initdb.d/init.sql \
  ${MYSQL_IMAGE}
if [ $? -ne 0 ]; then
  echo "Erreur lors du démarrage du conteneur MySQL. Abandon."
  exit 1
fi

# Attendre que MySQL soit prêt (vérification de port simple)
echo "Attente du démarrage de MySQL sur le port 3307 (cela peut prendre un moment)..."
until docker exec ${MYSQL_CONTAINER_NAME} mysqladmin ping -hlocalhost -uroot -proot > /dev/null 2>&1; do
  printf '.'
  sleep 2
done
echo -e "\nMySQL est prêt."

# 6. Démarrer le conteneur de l'API
echo "======================================================"
echo "Démarrage du conteneur (${API_IMAGE})..."
echo "======================================================"
docker run -d \
  --name ${API_CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  -p 8080:8080 \
  -e PORT=8080 \
  -e AI_API_MODEL=openai \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://${MYSQL_CONTAINER_NAME}:3306/jdr_generator_db" \
  -e SPRING_DATASOURCE_USERNAME="jdr_user" \
  -e SPRING_DATASOURCE_PASSWORD="root" \
  ${API_IMAGE}
if [ $? -ne 0 ]; then
  echo "Erreur lors du démarrage du conteneur API. Abandon."
  exit 1
fi

# 7. Démarrer le conteneur Gemini
echo "======================================================"
echo "Démarrage du conteneur (${GEMINI_IMAGE})..."
echo "======================================================"
docker run -d \
  --name ${GEMINI_CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  -p 3001:3001 \
  -e PORT=3001 \
  ${GEMINI_IMAGE}
if [ $? -ne 0 ]; then
  echo "Erreur lors du démarrage du conteneur Gemini. Abandon."
  exit 1
fi

# 8. Démarrer le conteneur OpenAI (les clés API sont intégrées dans l'image)
echo "======================================================"
echo "Démarrage du conteneur (${OPENAI_IMAGE})..."
echo "======================================================"
docker run -d \
  --name ${OPENAI_CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  -p 3002:3002 \
  -e PORT=3002 \
  ${OPENAI_IMAGE}
if [ $? -ne 0 ]; then
  echo "Erreur lors du démarrage du conteneur OpenAI. Abandon."
  exit 1
fi

# 9. Démarrer le conteneur Web (Frontend)
echo "======================================================"
echo "Démarrage du conteneur (${WEB_IMAGE})..."
echo "======================================================"
docker run -d \
  --name ${WEB_CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  -p 80:80 \
  --mount type=bind,source="$(pwd)"/web/nginx.conf,target=/etc/nginx/nginx.conf \
  ${WEB_IMAGE}
if [ $? -ne 0 ]; then
  echo "Erreur lors du démarrage du conteneur Web. Abandon."
  exit 1
fi

# 10. Nettoyer le fichier init.sql temporaire
echo "======================================================"
echo "Suppression du fichier temporaire '${INIT_SQL_FILE}'"
echo "======================================================"
rm -f "${INIT_SQL_FILE}"

echo "======================================================"
echo "- JDR-Generator : Tous les conteneurs sont démarrés! -"
echo "======================================================"
echo "L'application web (Frontend) est accessible sur : http://localhost"
echo "L'API principale (Backend) est accessible sur : http://localhost:8080"
echo "URL du service Gemini : http://localhost:3001"
echo "URL du service OpenAI : http://localhost:3002"
echo "MySQL est accessible sur localhost:3307"
echo ""
echo "- Pour vérifier l'état des conteneurs :"
echo "docker ps"
echo ""
echo "- Pour arrêter et supprimer tous les conteneurs, le réseau et les volumes liés à ce script :"
echo "docker stop ${WEB_CONTAINER_NAME} ${API_CONTAINER_NAME} ${GEMINI_CONTAINER_NAME} ${OPENAI_CONTAINER_NAME} ${MYSQL_CONTAINER_NAME}"
echo "docker rm ${WEB_CONTAINER_NAME} ${API_CONTAINER_NAME} ${GEMINI_CONTAINER_NAME} ${OPENAI_CONTAINER_NAME} ${MYSQL_CONTAINER_NAME}"
echo "docker network rm ${NETWORK_NAME}"
echo "docker volume rm ${MYSQL_VOLUME_NAME}"
echo ""