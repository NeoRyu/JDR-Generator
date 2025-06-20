import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background.js";
import {generateImage} from "./src/controllers/illustration.js";
import {generateStats} from "./src/controllers/statistiques.js";
import path from "path";
import { fileURLToPath } from 'url';


/**
 * @file Fichier principal du serveur API OpenAI.
 * @description Ce fichier initialise et configure le serveur Express pour l'API de OpenAI,
 * gère le chargement des variables d'environnement et définit les routes pour la génération
 * de texte (via ChatGPT) et d'images (via DALL-E), et démarre le serveur.
 */

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// CETTE LIGNE DOIT ÊTRE ICI ET EN PREMIER POUR CHARGER LE .ENV
dotenv.config({ path: path.resolve(__dirname, '../.env') });


/**
 * Instance de l'application Express.
 * @type {Express}
 */
const app = express();

/**
 * Le port sur lequel le serveur API OpenAI écoutera.
 * Récupéré depuis la variable d'environnement `PORT` (spécifique à OpenAI si différent),
 * ou 3002 par défaut.
 * @type {number}
 */
const port = process.env.PORT ? parseInt(process.env.PORT, 10) : 3002;

// Middleware pour parser les corps de requête JSON.
app.use(bodyParser.json());

/**
 * @route POST /openai/generate
 * @description Point de terminaison pour générer du texte (ex: backgrounds) via l'API OpenAI (ChatGPT).
 * Fait appel au contrôleur {@link generateResponse}.
 * @see {@link generateResponse} pour la logique de traitement.
 */
app.post("/openai/generate", generateResponse);

/**
 * @route POST /openai/illustrate
 * @description Point de terminaison pour générer une image via l'API OpenAI (DALL-E).
 * Fait appel au contrôleur {@link generateImage}.
 * @see {@link generateImage} pour la logique de traitement.
 */
app.post("/openai/illustrate", generateImage);

/**
 * @route POST /openai/stats
 * @description Point de terminaison pour générer des statistiques de personnage via l'API OpenAI (ChatGPT).
 * Fait appel au contrôleur {@link generateStats}.
 * @see {@link generateStats} pour la logique de traitement.
 */
app.post("/openai/stats", generateStats);

/**
 * @route GET /openai/healthcheck
 * @description Point de terminaison pour vérifier l'état de santé du service API OpenAI.
 * @returns {object} 200 - Un objet JSON indiquant le statut "OK" et le port d'écoute.
 * @example
 * // Requête:
 * GET /openai/healthcheck
 *
 * // Réponse:
 * {
 *   "status": "OK",
 *   "port": 3002
 * }
 */
app.get("/openai/healthcheck", (_req, res) => {
  res
    .status(200)
    .json({ status: "OK", port: port });
});

/**
 * Démarre le serveur Express et écoute les connexions entrantes sur le port spécifié.
 * Affiche également un message dans la console indiquant que le serveur est en cours d'exécution
 * et vérifie si la variable d'environnement API_KEY est chargée (généralement OPENAI_API_KEY).
 * @param {number} port - Le port d'écoute.
 * @param {string} host - L'hôte sur lequel écouter (0.0.0.0 pour toutes les interfaces).
 * @param {Function} callback - Fonction exécutée une fois le serveur démarré.
 */
app.listen(port, '0.0.0.0', () => {
  console.log(`OpenAI API Server running on http://0.0.0.0:${port}`);
  if (process.env.API_KEY) {
    console.log(`API_KEY loaded successfully.`);
  } else {
    console.error("ERROR: API_KEY environment variable is NOT set. OpenAI API calls will fail.");
  }
});