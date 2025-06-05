import {Request, Response} from "express";
import {GenerateContentResult, GenerativeModel, GoogleGenerativeAI,} from "@google/generative-ai";
import dotenv from "dotenv";
import fs from "fs";

dotenv.config();

// Configuration requise
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(''+process.env.API_KEY);
const configMaxOutputTokens: number = +(process.env.MAX_TOKENS ?? '2048');
const model: GenerativeModel = genAI.getGenerativeModel({
  model: ''+process.env.AI_TEXT_MODEL,
  generationConfig: {
    maxOutputTokens: configMaxOutputTokens,
    temperature: 0.4,
    topP: 0.6,
    topK: 20,
  },
});

// Ce tableau permet de conserver l'historique des conversations
const conversationContext: any[] = [];

// Les prompts qui seront envoyés par la suite pour générer une réponse attendue
const basePrompt = `You are an RPG expert with extensive knowledge of various game systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be provided with information on the game system, the character's race and class, and any other information needed to define them, as well as their stats, skills, disciplines, and equipment, in the target game world, to inspire you. If the character doesn't have a world, invent one; otherwise, don't invent it yourself: be faithful and only give answers you are certain of. Here's the character's background:`;
const askedPrompt =
  "Your answer must not be based solely on previous exchanges, must be in valid JSON format and must contain the required data (statistics, skills, disciplines, equipment, and other requested information). The response should be in French (fr-FR), including attribute names. As much as possible, the generated information should be based on the most recent version of the game system. I only need the missing information required to complete a player sheet that I am requesting. I already possess all the character's background information, so please omit it from your response (background / history / relationships). **Important: The response must be pure JSON, without any formatted code blocks. Do not use backticks (``). Use only double quotes (\") to delimit JSON keys and values.**";

// Fonction contrôleur pour gérer les conversations
export const generateStats = async (req: Request, res: Response) => {
  const pathSrc: string =
    "C:\\Users\\" +
    process.env.USER_WINDOW +
    "\\Downloads\\" +
    process.env.DOWNLOAD_FOLDER +
    "\\";
  const txtName: string =
    process.env.DOWNLOAD_FOLDER +
    "-gemini_" +
    Math.floor(Date.now() / 1000) +
    "_stats.txt";
  try {
    const { prompt } = req.body;
    console.log("Stats pour ::", req.body.data);

    const result: GenerateContentResult = await model.generateContent(
      basePrompt + req.body.data + askedPrompt,
    );
    let responseText: string = result.response.text();

    // Correction du JSON : suppression des guillemets inversés et autres problèmes de formatage
    responseText = responseText.replace(/``json\n/g, "");
    responseText = responseText.replace(/\n``/g, "");
    responseText = responseText.replace(/`json\n/g, "");
    responseText = responseText.replace(/\n`/g, "");
    responseText = responseText.replace(/`/g, ""); // enlever les guillemets inversés qui trainent.

    try {
      const parsedJson = JSON.parse(responseText);
      responseText = JSON.stringify(parsedJson);

      // Stocke la conversation
      conversationContext.push([prompt, responseText]);
      res.send({ response: responseText });

      fs.writeFileSync(pathSrc + txtName, responseText);
    } catch (jsonError) {
      console.error("Erreur d'analyse JSON après correction :", jsonError);
      console.error("Texte JSON invalide :", responseText);
      res
        .status(500)
        .json({ message: "Erreur lors de l'analyse du JSON généré." });
    }
  } catch (err) {
    console.error("Erreur de génération de contenu :", err);
    res.status(500).json({ message: "Internal server error" });
  }
};
