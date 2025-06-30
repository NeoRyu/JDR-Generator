# Utilise une image Node.js comme base pour l'étape de build
FROM node:20-alpine AS builder
# Définit le répertoire de travail dans le conteneur pour l'étape de build
WORKDIR /app
# Copie les fichiers package.json et package-lock.json (ou yarn.lock)
# Le chemin est maintenant relatif au répertoire 'web'
COPY package*.json ./
# Installe les dépendances
RUN npm install

# Supprime le répertoire dist avant la construction
RUN rm -rf dist

# Copie tout le code source de l'application web
# Le chemin est maintenant relatif au répertoire 'web'
COPY . .
# Définit une variable d'environnement VITE_API_URL pendant le build
# ARG VITE_API_URL
# Affiche la valeur de l'argument de build de test (pour diagnostic)
# RUN echo "La valeur de VITE_API_URL est : ${VITE_API_URL}"
# Build l'application React avec Vite en s'assurant que la variable d'environnement est passée
# RUN sh -c "VITE_API_URL=${VITE_API_URL} npx tsc && VITE_API_URL=${VITE_API_URL} npx vite build"

# Build l'application React avec Vite en mode production (.env.prod si présent et configuré)
RUN npx tsc && npx vite build

# Utilise une image Nginx pour servir les fichiers statiques buildés
FROM nginx:stable-alpine
# Copie les fichiers buildés depuis l'étape de build précédente
# Ce chemin est correct car il référence le système de fichiers de l'étape 'builder'
COPY --from=builder /app/dist /usr/share/nginx/html
# Expose le port 80 pour Nginx (port par défaut pour le web)
EXPOSE 80

# La configuration par défaut de Nginx devrait suffire pour servir des fichiers statiques
# Si vous avez une configuration Nginx personnalisée, vous devriez la copier ici aussi.
# Exemple :
# COPY nginx.conf /etc/nginx/nginx.conf