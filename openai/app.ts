import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background.js";
import {generateImage} from "./src/controllers/illustration.js";
import {generateStats} from "./src/controllers/statistiques.js";
import path from "path";
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// CETTE LIGNE DOIT ÊTRE ICI ET EN PREMIER POUR CHARGER LE .ENV
dotenv.config({ path: path.resolve(__dirname, '../.env') });

const app = express();
const port = process.env.PORT ? parseInt(process.env.PORT, 10) : 3002;

app.use(bodyParser.json());

app.post("/openai/generate", generateResponse);
app.post("/openai/illustrate", generateImage);
app.post("/openai/stats", generateStats);
app.get("/openai/healthcheck", (_req, res) => {
  res
    .status(200)
    .json({ status: "OK", port: port });
});

app.listen(port, '0.0.0.0', () => {
  console.log(`OpenAI API Server running on http://0.0.0.0:${port}`);
  if (process.env.API_KEY) {
    console.log(`API_KEY loaded successfully.`);
  } else {
    console.error("ERROR: API_KEY environment variable is NOT set. OpenAI API calls will fail.");
  }
});