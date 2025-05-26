#!/bin/bash

# "======================================================"
# --- HOW TO USE IT ---
# "======================================================"
# Ouvrez un terminal sur Linux/Unix, ou Git Bash sur Windows.
# Git Bash : https://git-scm.com/downloads/win

# Naviguez jusqu'au répertoire où vous avez enregistré le fichier, par exemple :
# cd ~/IdeaProjects/JDR-Generator/.github/workflows/localhost

# Puis exécutez :
# chmod +x docker-start-app.sh

# Exécutez le script :
# ./docker-start-app.sh

echo "Démarrage de JDR-Generator ..."
echo "======================================================"
echo "Étape 1 : Vérifier si Docker est en cours d'exécution"
echo "======================================================"
if ! docker info > /dev/null 2>&1; then
  echo "Erreur : Docker n'est pas en cours d'exécution. Veuillez démarrer Docker Desktop ou votre démon Docker."
  exit 1
fi

echo "======================================================"
echo "Étape 2 : Télécharge les images, celles qui ont "
echo "un contexte de build, le réseau et les volumes, "
echo "puis démarre les conteneurs docker."
echo "======================================================"
docker-compose -p jdr-generator -f ../../../docker-compose.local.yml up --build -d

if [ $? -eq 0 ]; then
  echo "Conteneurs Docker démarrés avec succès sous le projet 'jdr-generator'."
  echo "Pour voir le statut des conteneurs : docker-compose -p jdr-generator -f docker-compose.local.yml ps"
  echo "Pour voir les logs : docker-compose -p jdr-generator -f docker-compose.local.yml logs -f"
  echo "Pour arrêter les conteneurs : docker-compose -p jdr-generator -f docker-compose.local.yml down"
else
  echo "Une erreur est survenue lors du démarrage des conteneurs Docker."
fi
