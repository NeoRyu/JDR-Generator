import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background";
import {generateImage} from "./src/controllers/illustration";
import {generateStats} from "./src/controllers/statistiques";


dotenv.config();

const app = express();
const port = process.env.PORT || 3002;
app.use(bodyParser.json());

app.post("/openai/generate", generateResponse);
app.post("/openai/illustrate", generateImage);
app.post("/openai/stats", generateStats);
app.get('/openai/healthcheck', (req, res) => {
    res.status(200).json({ status: 'OK', port: parseInt(process.env.PORT || '3002', 10) });
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});