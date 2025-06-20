import {Request, Response} from "express";
import {GenerateContentResult, GenerativeModel, GoogleGenerativeAI,} from "@google/generative-ai";
import dotenv from "dotenv";
import fs from "fs";

dotenv.config();

// --- Configuration requise du modèle Gemini ---

/**
 * Instance du client GoogleGenerativeAI.
 * Initialisée avec la clé API depuis les variables d'environnement.
 * @type {GoogleGenerativeAI}
 */
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(''+process.env.API_KEY);

/**
 * Nombre maximum de tokens pour la réponse générée.
 * Récupéré depuis la variable d'environnement `MAX_TOKENS`, ou 2048 par défaut.
 * @type {number}
 */
const configMaxOutputTokens: number = +(process.env.MAX_TOKENS ?? '2048');

/**
 * Instance du modèle génératif Gemini.
 * Configuré avec le modèle de texte spécifié dans les variables d'environnement
 * et les paramètres de génération (maxOutputTokens, temperature, topP, topK).
 * @type {GenerativeModel}
 */
const model: GenerativeModel = genAI.getGenerativeModel({
  model: ''+process.env.AI_TEXT_MODEL,
  generationConfig: {
    maxOutputTokens: configMaxOutputTokens,
    temperature: 0.4,
    topP: 0.6,
    topK: 20,
  },
});

/**
 * Tableau pour conserver l'historique des conversations (prompt et réponse).
 * Note: Actuellement, cet historique n'est pas utilisé pour influencer les générations futures dans ce contrôleur.
 * @type {any[]}
 */
const conversationContext: any[] = [];

// --- Prompts pour la génération de statistiques ---

/**
 * Prompt de base fournissant le contexte initial à l'IA.
 * Décrit le rôle de l'IA en tant qu'expert en JDR pour la création de personnages.
 * @type {string}
 */
const basePrompt = `You are an RPG expert with extensive knowledge of various game systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be provided with information on the game system, the character's race and class, and any other information needed to define them, as well as their stats, skills, disciplines, and equipment, in the target game world, to inspire you. If the character doesn't have a world, invent one; otherwise, don't invent it yourself: be faithful and only give answers you are certain of. Here's the character's background:`;

/**
 * Prompt spécifiant les instructions de formatage et de contenu pour la réponse de l'IA.
 * Demande une réponse en JSON valide, en français, sans les informations de background déjà connues,
 * et en insistant sur l'absence de blocs de code formatés (backticks).
 * @type {string}
 */
const askedPrompt =
  "Your answer must not be based solely on previous exchanges, must be in valid JSON format and must contain the required data (statistics, skills, disciplines, equipment, and other requested information). The response should be in French (fr-FR), including attribute names. As much as possible, the generated information should be based on the most recent version of the game system. I only need the missing information required to complete a player sheet that I am requesting. I already possess all the character's background information, so please omit it from your response (background / history / relationships). **Important: The response must be pure JSON, without any formatted code blocks. Do not use backticks (``). Use only double quotes (\") to delimit JSON keys and values.**";


// --- Fonction Contrôleur ---

/**
 * @fileoverview Contrôleur pour la génération de statistiques de personnages de JDR via l'API Gemini.
 */

/**
 * Génère des statistiques de personnage, des compétences, de l'équipement, etc.,
 * en utilisant l'API Gemini, basé sur un background de personnage fourni.
 * La réponse est attendue en format JSON et est sauvegardée localement en mode développement.
 *
 * @async
 * @param {Request} req - L'objet requête Express. Le corps de la requête doit contenir :
 *                        `req.body.data`: Le background et les informations du personnage.
 *                        `req.body.prompt`: (Optionnel, actuellement utilisé pour le contexte de `conversationContext`)
 * @param {Response} res - L'objet réponse Express.
 * @returns {Promise<void>} Une promesse qui se résout lorsque la réponse a été envoyée.
 *                          En cas de succès, envoie un JSON avec la propriété `response` contenant les statistiques générées.
 *                          En cas d'erreur, envoie un statut 500 avec un message d'erreur.
 */
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
