import express from "express";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import {generateImage} from "./src/controllers/illustration.js";

dotenv.config();

const app = express();
const port = process.env.PORT || 3003;
app.use(bodyParser.json());

app.post("/openai/illustrate", generateImage);
app.get("/openai/healthcheck", (_req, res) => {
  res
    .status(200)
    .json({ status: "OK", port: parseInt(process.env.PORT || "3003", 10) });
});

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
