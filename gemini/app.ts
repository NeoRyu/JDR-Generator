import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background";
import {generateImage} from "./src/controllers/illustration";
import {generateStats} from "./src/controllers/statistiques";

dotenv.config();

const app = express();
const port = process.env.PORT || 3001;
app.use(bodyParser.json());

app.post("/gemini/generate", generateResponse);
app.post("/gemini/illustrate", generateImage);
app.post("/gemini/stats", generateStats);
app.get("/gemini/healthcheck", (_req, res) => {
  res
    .status(200)
    .json({ status: "OK", port: parseInt(process.env.PORT || "3001", 10) });
});

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
