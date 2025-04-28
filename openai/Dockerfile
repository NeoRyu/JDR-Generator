# Utilise une image Node.js comme base
FROM node:20-alpine

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie les fichiers package.json et package-lock.json (ou yarn.lock)
COPY package*.json ./

# Installe les dépendances
RUN npm install

# Copie tout le code source de l'API OpenAI
COPY . .

# Build l'application TypeScript
RUN npm run build

# Définit la commande pour démarrer l'application
CMD ["node", "dist/app.js"]

# Expose le port sur lequel l'API OpenAI s'exécute (port 3000 à l'int\u00E9rieur du conteneur)
EXPOSE 3000