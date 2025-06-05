"use strict";
var __importDefault =
  (this && this.__importDefault) ||
  function (mod) {
    return mod && mod.__esModule ? mod : { default: mod };
  };
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const body_parser_1 = __importDefault(require("body-parser"));
const dotenv_1 = __importDefault(require("dotenv"));
const background_1 = require("./src/controllers/background");
const illustration_1 = require("./src/controllers/illustration");
const statistiques_1 = require("./src/controllers/statistiques");
dotenv_1.default.config();
const app = (0, express_1.default)();
const port = process.env.PORT || 3002;
app.use(body_parser_1.default.json());
app.post("/openai/generate", background_1.generateResponse);
app.post("/openai/illustrate", illustration_1.generateImage);
app.post("/openai/stats", statistiques_1.generateStats);
app.get("/openai/healthcheck", (_req, res) => {
  res
    .status(200)
    .json({ status: "OK", port: parseInt(process.env.PORT || "3002", 10) });
});
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
//# sourceMappingURL=app.js.map
