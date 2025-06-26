import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import { generateImage } from "./src/controllers/illustration.js";
import path from "path";
import { fileURLToPath } from "url";

/**
 * @file Fichier principal du serveur API Freepik.
 * @description Ce fichier initialise et configure le serveur Express pour l'API Freepik,
 * gère le chargement des variables d'environnement, définit les routes
 * et démarre le serveur.
 */

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
// CETTE LIGNE DOIT ÊTRE ICI ET EN PREMIER POUR CHARGER LE .ENV
dotenv.config({ path: path.resolve(__dirname, "../.env") });

/**
 * Instance de l'application Express
 * @type {Express}
 */
const app = express();

/**
 * Le port sur lequel le serveur API écoutera.
 * Récupéré depuis la variable d'environnement `PORT`, ou 3003 par défaut.
 * @type {number}
 */
const port = process.env.PORT ? parseInt(process.env.PORT, 10) : 3003;

// Middleware pour parser les corps de requête JSON.
app.use(bodyParser.json());

/**
 * @route POST /freepik/illustrate
 * @description Point de terminaison pour générer une image via l'API Freepik.
 * Fait appel au contrôleur {@link generateImage}.
 * @see {@link generateImage} pour la logique de traitement.
 */
app.post("/freepik/illustrate", generateImage);

/**
 * @route GET /freepik/healthcheck
 * @description Point de terminaison pour vérifier l'état de santé du service API Freepik.
 * @returns {object} 200 - Un objet JSON indiquant le statut "OK" et le port d'écoute.
 * @example
 * // Requête:
 * GET /freepik/healthcheck
 *
 * // Réponse:
 * {
 *   "status": "OK",
 *   "port": 3003
 * }
 */
app.get("/freepik/healthcheck", (_req, res) => {
  res.status(200).json({ status: "OK", port: port });
});

/**
 * Démarre le serveur Express et écoute les connexions entrantes sur le port spécifié.
 * Affiche également un message dans la console indiquant que le serveur est en cours d'exécution
 * et vérifie si la variable d'environnement API_KEY est chargée.
 * @param {number} port - Le port d'écoute.
 * @param {string} host - L'hôte sur lequel écouter (0.0.0.0 pour toutes les interfaces).
 * @param {Function} callback - Fonction exécutée une fois le serveur démarré.
 */
app.listen(port, "0.0.0.0", () => {
  console.log(`Freepik API Server running on http://0.0.0.0:${port}`);
  if (process.env.API_KEY) {
    console.log(`API_KEY loaded successfully.`);
  } else {
    console.error(
      "ERROR: API_KEY environment variable is NOT set. Freepik API calls will fail.",
    );
  }
});
