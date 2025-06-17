import {Request, Response} from "express";
import * as fs from "fs";

/**
 * @file Contrôleur pour la génération d'images via l'API OpenAI (DALL-E).
 * @description Ce fichier gère la configuration de l'API DALL-E, l'appel à l'API,
 * le traitement de la réponse (Base64), la sauvegarde locale de l'image,
 * et une logique de tentatives multiples en cas d'erreurs serveur.
 */

// --- Global Configuration ---
/**
 * Modèle DALL-E à utiliser pour la génération d'images.
 * Récupéré depuis la variable d'environnement `AI_IMAGE_MODEL` ou utilise "dall-e-3" par défaut.
 * @type {string}
 */
const dallEModel = process.env.AI_IMAGE_MODEL || "dall-e-3";

/**
 * Nom du dossier de téléchargement pour les fichiers générés.
 * Récupéré depuis la variable d'environnement `DOWNLOAD_FOLDER` ou "default" par défaut.
 * @type {string}
 */
const downloadFolder = process.env.DOWNLOAD_FOLDER || "default";

/**
 * Nombre maximal de tentatives pour l'appel API en cas d'erreur serveur (500/503).
 * @type {number}
 */
const maxRetries = 3;

// --- OpenAI Configuration ---
/**
 * Vérifie si la clé API OpenAI est définie dans les variables d'environnement.
 * Log une erreur si elle est manquante.
 */
if (!process.env.API_KEY) {
  console.error(
    "API_KEY environment variable is not set. OpenAI API calls will fail.",
  );
}

/**
 * Interface décrivant la structure attendue de la réponse de l'API OpenAI
 * pour une génération d'image (DALL-E).
 */
interface OpenAIImageGenerationResponse {
  created: number;
  data?: {
    b64_json?: string;
    url?: string;
    revised_prompt?: string;
  }[];
  error?: {
    message: string;
    type: string;
    param?: any;
    code?: any;
  };
}

/**
 * Vérifie si l'application s'exécute dans un environnement de développement local.
 * @returns {boolean} True si `NODE_ENV` n'est pas "production", false sinon.
 */
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== "production";


// Controller function to generate the illustration
/**
 * Contrôleur pour générer une illustration en utilisant l'API OpenAI (DALL-E).
 * Gère l'appel à l'API, le traitement de la réponse Base64, la sauvegarde locale
 * en environnement de développement, et une logique de tentatives en cas d'erreurs serveur.
 *
 * @async
 * @param {Request} req - L'objet requête Express. Le corps de la requête (`req.body`)
 *                        doit contenir la propriété `prompt` (le texte décrivant l'image souhaitée).
 * @param {Response} res - L'objet réponse Express.
 * @returns {Promise<void>} Une promesse qui se résout lorsque la réponse a été envoyée.
 *                          En cas de succès, renvoie un JSON avec la propriété `image` (Base64) et un `message`.
 *                          En cas d'échec ou d'erreur, renvoie un statut d'erreur approprié avec un message.
 */
export const generateImage = async (
  req: Request,
  res: Response,
): Promise<void> => {
  const pathSrc: string = `/app/downloads/${downloadFolder}/`;
  const imgName: string = `${downloadFolder}-openai-image_${Math.floor(Date.now() / 1000)}.png`;
  let retryCount = 0;
  const openaiApiKey = "" + process.env.API_KEY;
  const openaiOrgId = "" + process.env.ORG_ID;

  // Create local download directory if needed
  if (isLocalEnvironment() && !fs.existsSync(pathSrc)) {
    try {
      fs.mkdirSync(pathSrc, { recursive: true, mode: 0o755 });
      console.log(`Local download directory created: ${pathSrc}`);
    } catch (e) {
      console.error(`Error creating local download directory ${pathSrc}:`, e);
    }
  }

  while (retryCount < maxRetries) {
    try {
      const fullPrompt = req.body.prompt;
      console.log("generateImage :: prompt received:", fullPrompt);

      if (!fullPrompt) {
        console.error("No prompt provided.");
        res.status(400).json({ message: "No prompt provided." });
        return;
      }

      console.log("generateImage :: fullPrompt being sent:", fullPrompt);
      console.log(`Calling OpenAI DALL-E API with model: ${dallEModel}`);

      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${openaiApiKey}`,
        "OpenAI-Organization": `${openaiOrgId}`,
      };

      const response = await fetch(
        "https://api.openai.com/v1/images/generations",
        {
          method: "POST",
          headers: headers,
          body: JSON.stringify({
            model: dallEModel,
            prompt: fullPrompt,
            n: 1,
            size: "1024x1024",
            response_format: "b64_json",
          }),
        },
      );

      const data: OpenAIImageGenerationResponse =
        (await response.json()) as OpenAIImageGenerationResponse; // Cast the response to our interface

      if (
        response.ok &&
        data.data &&
        data.data.length > 0 &&
        data.data[0].b64_json
      ) {
        const imageBase64 = data.data[0].b64_json;
        const buffer = Buffer.from(imageBase64, "base64");

        if (isLocalEnvironment()) {
          try {
            fs.writeFileSync(pathSrc + imgName, buffer);
            console.log(`> Local image saved to: ${pathSrc}${imgName}`);
            console.log(
              `> Image data obtained (Base64), size: ${buffer.length} bytes.`,
            );
          } catch (writeError) {
            console.error(
              `Error writing local image file ${pathSrc}${imgName}:`,
              writeError,
            );
          }
        } else {
          console.log(
            "> Running in non-local environment, skipping local image save.",
          );
        }

        res.status(200).json({
          image: imageBase64,
          message: "Image generation processed by DALL-E",
        });
        return;
      } else {
        console.error("OpenAI API error:", data);
        if (response.status === 500 || response.status === 503) {
          console.log(
            `OpenAI API Server Error (${response.status}), retrying (${retryCount + 1}/${maxRetries})...`,
          );
          await new Promise((resolve) =>
            setTimeout(resolve, (retryCount + 1) * 1000),
          );
          retryCount++;
          continue;
        } else {
          res.status(response.status || 500).json({
            message: "Image generation failed",
            error: data.error ? data.error.message : "Unknown error",
          });
          return;
        }
      }
    } catch (err: any) {
      console.error("Error during generateImage API call:", err);
      res
        .status(500)
        .json({ message: "An unexpected error occurred", error: err.message });
      return;
    }
  }

  console.error(`Image generation failed after ${maxRetries} retries.`);
  res.status(500).json({
    message: "Image generation service unavailable after multiple retries.",
  });
};
