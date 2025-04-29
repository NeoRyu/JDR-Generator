import {Request, Response} from "express";
import * as fs from 'fs';
import OpenAI from 'openai';

// --- Global Configuration ---
const dallEModel = process.env.AI_IMAGE_MODEL || 'dall-e-3'; // Définit le modèle DALL-E à utiliser, par défaut 'dall-e-3' si la variable d'environnement AI_IMAGE_MODEL n'est pas définie.
const downloadFolder = process.env.DOWNLOAD_FOLDER || 'default'; // Définit le nom du dossier de téléchargement, par défaut 'default' si la variable d'environnement DOWNLOAD_FOLDER n'est pas définie.
const initialImagePrompt = 'Generate a highly detailed and artistic illustration in a heroic-fantasy style, suitable for a role-playing game character portrait. The style should resemble digital painting with soft gradients and a focus on character detail. Consider the overall mood and atmosphere. '; // Prompt initial de base pour la génération d'image.
const maxRetries = 3; // Nombre maximum de tentatives en cas d'échec de l'appel à l'API OpenAI.

// --- OpenAI Configuration ---
if (!process.env.API_KEY) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
    // Gestion de l'erreur si la clé API OpenAI n'est pas définie dans les variables d'environnement.
}
const openai = new OpenAI({ apiKey: process.env.API_KEY }); // Initialisation de l'objet OpenAI avec la clé API récupérée des variables d'environnement.

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== 'production'; // Fonction pour vérifier si l'application s'exécute dans un environnement local (la variable d'environnement NODE_ENV n'est pas 'production').

// Controller function to generate the illustration
export const generateImage = async (req: Request, res: Response): Promise<void> => {
    const pathSrc: string = `/app/downloads/${downloadFolder}/`; // Construit le chemin du dossier de téléchargement local.
    const imgName: string = `${downloadFolder}-openai-image_${Math.floor(Date.now() / 1000)}.png`; // Génère un nom de fichier unique pour l'image basée sur le timestamp.
    let retryCount = 0; // Initialise le compteur de tentatives.

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
            const prompt = req.body.prompt; // Récupère le prompt fourni dans le corps de la requête POST.
            console.log('generateImage :: prompt received:', prompt);

            if (!prompt) {
                console.error('No prompt provided.');
                res.status(400).json({ message: 'No prompt provided.' });
                return;
            }

            const fullPrompt = initialImagePrompt + prompt; // Combine le prompt initial avec le prompt spécifique de la requête.

            console.log('generateImage :: fullPrompt being sent:', fullPrompt); // Affiche le prompt complet qui sera envoyé à l'API OpenAI.
            console.log(`Calling OpenAI DALL-E API with model: ${dallEModel}`); // Indique que l'appel à l'API OpenAI est en cours.
            const response = await openai.images.generate({
                model: dallEModel,
                prompt: fullPrompt,
                n: 1, // number of illustration generated
                size: "1024x1024", // DALL-E 3 supports sizes: "1024x1024", "1792x1024", "1024x1792".
                quality: "standard", // "standard" is less expensive than "hd".
                style: "natural", // "vivid" or "natural". "natural" seems suitable for the prompt.
                response_format: 'b64_json', // Request Base64 JSON for image data.
            });
            console.log('OpenAI API response received.'); // Indique que la réponse de l'API OpenAI a été reçue.

            if (response.data && response.data.length > 0 && response.data[0].b64_json) {
                const imageBase64 = response.data[0].b64_json; // Extrait les données Base64 de l'image de la réponse.
                const buffer = Buffer.from(imageBase64, 'base64'); // Crée un buffer à partir des données Base64.

                if (isLocalEnvironment()) {
                    try {
                        fs.writeFileSync(pathSrc + imgName, buffer);
                        console.log(`> Local image saved to: ${pathSrc}${imgName}`);
                        console.log(`> Image data obtained (Base64), size: ${buffer.length} bytes.`);
                    } catch (writeError) {
                        console.error(`Error writing local image file ${pathSrc}${imgName}:`, writeError);
                    }
                } else {
                    console.log('> Running in non-local environment, skipping local image save.');
                }

                res.status(200).json({ image: imageBase64, message: "Image generation processed by DALL-E" }); // Envoie une réponse HTTP 200 avec les données de l'image.
                return;
            } else {
                console.error('Unexpected OpenAI API response structure or missing image data:', JSON.stringify(response));
                res.status(500).json({ message: "Image generation failed: unexpected response from DALL-E API." }); // Envoie une réponse HTTP 500 en cas de structure de réponse inattendue.
                return;
            }

        } catch (err: any) {
            console.error("Error during generateImage API call to DALL-E:", err); // Log de l'erreur globale lors de l'appel à l'API.

            if (err.response && err.response.data && err.response.data.error) {
                console.error("OpenAI Error Details:", err.response.data.error); // Log des détails de l'erreur renvoyée par l'API OpenAI.
                if (err.response.status === 500 || err.response.status === 503) {
                    console.log(`OpenAI API Server Error (${err.response.status}), retrying (${retryCount + 1}/${maxRetries})...`);
                    await new Promise(resolve => setTimeout(resolve, (retryCount + 1) * 1000)); // Attente avant de retenter l'appel en cas d'erreur serveur de l'API.
                    retryCount++;
                    continue;
                } else {
                    res.status(err.response.status || 500).json({
                        message: "Error during generating image with DALL-E",
                        error: err.response.data.error.message || "Unknown OpenAI API error"
                    }); // Envoie une réponse HTTP 500 avec les détails de l'erreur de l'API.
                    return;
                }
            } else {
                console.error("Non-OpenAI Error:", err);
                res.status(500).json({ message: "An unexpected error occurred", error: err.message }); // Envoie une réponse HTTP 500 pour les erreurs inattendues.
                return;
            }
        }
    }

    console.error(`Image generation failed after ${maxRetries} retries.`);
    res.status(500).json({ message: "Image generation service unavailable after multiple retries." }); // Envoie une réponse HTTP 500 si toutes les tentatives échouent.
};