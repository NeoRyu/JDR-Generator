import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/gemini";
import {generateImage} from "./src/controllers/imagen";

dotenv.config();

const app = express();
const port = process.env.PORT;
app.use(bodyParser.json());

app.post("/generate", generateResponse);
app.post("/illustrate", generateImage);

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});