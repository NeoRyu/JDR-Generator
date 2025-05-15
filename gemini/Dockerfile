# Utilise une image Node.js comme base
FROM node:20-alpine

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie les fichiers package.json et package-lock.json (ou yarn.lock)
# Le chemin est maintenant relatif au répertoire 'gemini'
COPY package*.json ./

# Installe les dépendances
RUN npm install

# Supprime le répertoire dist avant la construction
RUN rm -rf dist

# Copie tout le code source de l'API Gemini
# Le chemin est maintenant relatif au répertoire 'gemini'
COPY . .

# Build l'application TypeScript
RUN npm run build

# Définit la commande pour démarrer l'application
CMD ["node", "dist/app.js"]

# Expose le port sur lequel l'API Gemini s'exécute
EXPOSE 3001