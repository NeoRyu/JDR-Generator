import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import { generateImage } from "./src/controllers/illustration.js";
import path from "path";
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// C'EST CETTE LIGNE QUI DOIT ÊTRE ICI ET EN PREMIER POUR CHARGER LE .ENV
dotenv.config({ path: path.resolve(__dirname, '../.env') });

const app = express();
const port = process.env.FREEEPIK_PORT ? parseInt(process.env.FREEEPIK_PORT, 10) : 3003;

app.use(bodyParser.json());

app.post("/freepik/illustrate", generateImage);
app.get("/freepik/healthcheck", (_req, res) => {
  res
      .status(200)
      .json({ status: "OK", port: port });
});

app.listen(port, () => {
  console.log(`Freepik API Server running on port ${port}`);
  if (process.env.API_KEY) {
    console.log(`API_KEY loaded successfully.`);
  } else {
    console.error("ERROR: API_KEY environment variable is NOT set. Freepik API calls will fail.");
  }
});