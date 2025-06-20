import express, { Express, Request, Response } from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background.js";
import {generateImage} from "./src/controllers/illustration.js";
import {generateStats} from "./src/controllers/statistiques.js";
import path from "path";
import { fileURLToPath } from 'url';

/**
 * @file Fichier principal du serveur API Gemini.
 * @description Ce fichier initialise et configure le serveur Express pour l'API Gemini,
 * gère le chargement des variables d'environnement, définit les routes pour la génération
 * de texte, d'images et de statistiques, et démarre le serveur.
 */

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
// CETTE LIGNE DOIT ÊTRE ICI ET EN PREMIER POUR CHARGER LE .ENV
dotenv.config({ path: path.resolve(__dirname, '../.env') });

/**
 * Instance de l'application Express.
 * @type {Express}
 */
const app: Express = express();

/**
 * Le port sur lequel le serveur API Gemini écoutera.
 * Récupéré depuis la variable d'environnement `PORT`, ou 3001 par défaut.
 * @type {number}
 */
const port: number = process.env.PORT ? parseInt(process.env.PORT, 10) : 3001;

// Middleware pour parser les corps de requête JSON.
app.use(bodyParser.json());

/**
 * @route POST /gemini/generate
 * @description Point de terminaison pour générer du texte (ex: backgrounds) via l'API Gemini.
 * Fait appel au contrôleur {@link generateResponse}.
 * @see {@link generateResponse} pour la logique de traitement.
 */
app.post("/gemini/generate", generateResponse);

/**
 * @route POST /gemini/illustrate
 * @description Point de terminaison pour générer une image via l'API Gemini (Imagen).
 * Fait appel au contrôleur {@link generateImage}.
 * @see {@link generateImage} pour la logique de traitement.
 */
app.post("/gemini/illustrate", generateImage);

/**
 * @route POST /gemini/stats
 * @description Point de terminaison pour générer des statistiques de personnage via l'API Gemini.
 * Fait appel au contrôleur {@link generateStats}.
 * @see {@link generateStats} pour la logique de traitement.
 */
app.post("/gemini/stats", generateStats);

/**
 * @route GET /gemini/healthcheck
 * @description Point de terminaison pour vérifier l'état de santé du service API Gemini.
 * @returns {object} 200 - Un objet JSON indiquant le statut "OK" et le port d'écoute.
 * @example
 * // Requête:
 * GET /gemini/healthcheck
 *
 * // Réponse:
 * {
 *   "status": "OK",
 *   "port": 3001
 * }
 */
app.get("/gemini/healthcheck", (_req: Request, res: Response) => {
  res
      .status(200)
      .json({ status: "OK", port: port });
});

/**
 * Démarre le serveur Express et écoute les connexions entrantes sur le port spécifié.
 * Affiche également un message dans la console indiquant que le serveur est en cours d'exécution
 * et vérifie si la variable d'environnement API_KEY est chargée.
 * @param {number} port - Le port d'écoute.
 * @param {string} host - L'hôte sur lequel écouter (0.0.0.0 pour toutes les interfaces).
 * @param {Function} callback - Fonction exécutée une fois le serveur démarré.
 */
app.listen(port, '0.0.0.0', () => {
  console.log(`Gemini API Server running on http://0.0.0.0:${port}`);
  // Note: La variable d'environnement pour Gemini pourrait être GOOGLE_API_KEY ou similaire.
  // Adaptez la vérification ci-dessous si le nom de la clé est différent pour Gemini.
  if (process.env.API_KEY || process.env.GOOGLE_API_KEY) { // Vérifie API_KEY ou GOOGLE_API_KEY
    console.log(`API Key for Gemini loaded successfully.`);
  } else {
    console.error("ERROR: API_KEY (or GOOGLE_API_KEY) environment variable is NOT set. Gemini API calls will fail.");
  }
});