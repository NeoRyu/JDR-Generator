import {Request, Response} from "express";
import {
  GenerateContentResult,
  GenerativeModel,
  GoogleGenerativeAI,
  GoogleGenerativeAIFetchError,
  Part,
} from "@google/generative-ai";
import * as fs from "fs";

/**
 * @fileoverview Contrôleur pour la génération d'images via l'API Gemini (Imagen).
 * Gère la communication avec le modèle d'image de Gemini, le traitement de la réponse,
 * la sauvegarde de l'image et la gestion des erreurs, y compris une logique de tentatives multiples.
 */

// --- Configuration requise du modèle Gemini pour les images ---

/**
 * Instance du client GoogleGenerativeAI.
 * Initialisée avec la clé API depuis les variables d'environnement.
 * @type {GoogleGenerativeAI}
 */
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(''+process.env.API_KEY);

/**
 * Instance du modèle génératif Gemini pour les images (Imagen).
 * Configuré avec le modèle d'image spécifié dans les variables d'environnement.
 * Note: `responseModalities` est utilisé pour indiquer que nous attendons du texte et une image.
 * @type {GenerativeModel}
 */
const model: GenerativeModel = genAI.getGenerativeModel({
  model: ''+process.env.AI_IMAGE_MODEL,
  generationConfig: {
    // @ts-expect-error - Gemini API JS is missing this type
    responseModalities: ["Text", "Image"],
  },
});


// --- Fonction Contrôleur ---

/**
 * Génère une image en utilisant l'API Gemini (Imagen) basé sur un prompt fourni.
 * La fonction inclut une logique de tentatives multiples en cas d'erreur 503 (Service Unavailable).
 * L'image générée est renvoyée en Base64 et sauvegardée localement en mode développement.
 *
 * @async
 * @param {Request} req - L'objet requête Express. Le corps de la requête doit contenir :
 *                        `req.body.prompt`: Le prompt textuel pour la génération de l'image.
 * @param {Response} res - L'objet réponse Express.
 * @returns {Promise<void>} Une promesse qui se résout lorsque la réponse a été envoyée.
 *                          En cas de succès, envoie un JSON avec la propriété `image` (Base64) et un `message`.
 *                          En cas d'échec ou d'erreur, envoie un statut d'erreur approprié avec un message.
 */
export const generateImage = async (
  req: Request,
  res: Response,
): Promise<void> => {
  const pathSrc: string = `C:\\Users\\${process.env.USER_WINDOW}\\Downloads\\${process.env.DOWNLOAD_FOLDER}\\`;
  const imgName: string = `${process.env.DOWNLOAD_FOLDER}-imagen_${Math.floor(Date.now() / 1000)}.png`;
  const maxRetries = 3; // Nombre maximal de tentatives
  let retryCount = 0;

  while (retryCount < maxRetries) {
    try {
      console.log("generateImage :: prompt recu :", req.body.prompt); // Log du prompt reçu
      const prompt = req.body.prompt;
      if (!prompt) {
        console.error("No prompt provided.");
        res.status(400).json({ message: "No prompt provided." });
        return;
      }

      const result: GenerateContentResult = await model.generateContent({
        contents: [{ role: "user", parts: [{ text: prompt }] }],
      });
      console.log("Gemini API response:", result); // Log de la réponse de l'API Gemini

      if (
        result &&
        result.response &&
        result.response.candidates &&
        result.response.candidates.length > 0
      ) {
        let imageBase64: string | null = null; // Variable pour stocker le Blob Base64

        result.response.candidates[0].content.parts.forEach((part: Part) => {
          if (part.text) {
            console.log(part.text);
          } else if (part.inlineData) {
            const imageData: string = part.inlineData.data;
            const buffer: Buffer<ArrayBuffer> = Buffer.from(
              imageData,
              "base64",
            );
            // Convertir le buffer en Base64 pour l'envoi du Blob
            imageBase64 = buffer.toString("base64");
            // Enregistrer l'image dans un fichier
            fs.writeFileSync(pathSrc + imgName, buffer);
            console.log("> L'image enregistrée ici :", pathSrc + imgName);
          } else {
            console.log("Unexpected response part:", part);
          }
        });
        if (imageBase64) {
          res
            .status(200)
            .json({
              image: imageBase64,
              message: "Image generation processed",
            });
          return; // Succès, on sort de la boucle de réessai
        } else {
          res
            .status(500)
            .json({ message: "Image generation failed: no image data." });
          return;
        }
      } else {
        if (
          result &&
          result.response &&
          result.response.promptFeedback &&
          result.response.promptFeedback.blockReason
        ) {
          console.error(
            "Gemini API blocked the prompt:",
            result.response.promptFeedback.blockReason,
          );
          res
            .status(400)
            .json({
              message: "Prompt blocked by Gemini API",
              error: result.response.promptFeedback.blockReason,
            });
          return;
        }
        res
          .status(500)
          .json({ message: "Image generation failed: no candidates." });
        return;
      }
    } catch (err: any) {
      console.error(err);
      if (err instanceof GoogleGenerativeAIFetchError && err.status === 503) {
        console.log(
          `Service Unavailable (503), retrying (${retryCount + 1}/${maxRetries})...`,
        );
        await new Promise((resolve) =>
          setTimeout(resolve, (retryCount + 1) * 1000),
        ); // Délai exponentiel (1s, 2s, 3s)
        retryCount++;
      } else {
        // Autre type d'erreur, on arrête les réessais
        res
          .status(500)
          .json({
            message: "Error during generating image",
            error: err.message,
          });
        return;
      }
    }
  }

  // Si on arrive ici après maxRetries, cela signifie que toutes les tentatives ont échoué (probablement à cause du 503)
  res
    .status(503)
    .json({
      message: "Image generation service unavailable after multiple retries.",
    });
};
