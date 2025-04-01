import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateResponse} from "./src/controllers/background";
import {generateImage} from "./src/controllers/illustration";
import {generateStats} from "./src/controllers/statistiques";


dotenv.config();

const app = express();
const port = process.env.PORT;
app.use(bodyParser.json());

app.post("/generate", generateResponse);
app.post("/illustrate", generateImage);
app.post("/stats", generateStats);

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});