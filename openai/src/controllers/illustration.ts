import {Request, Response} from "express";
import * as fs from 'fs';
// Importation de la biblioth\u00E8que OpenAI
import OpenAI from 'openai';


// --- Configuration OpenAI ---
// Assurez-vous que process.env.API_KEY contient votre cl\u00E9 API OpenAI
if (!process.env.API_KEY) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
    // G\u00E9rer cette erreur - l'application peut ne pas d\u00E9marrer ou les appels API \u00E9choueront
}
const openai = new OpenAI({
    apiKey: process.env.API_KEY as string, // Utiliser une variable d'environnement pour la cl\u00E9
});

// Mod\u00E8le DALL-E \u00E0 utiliser. Les options courantes sont 'dall-e-2' ou 'dall-e-3'.
// 'dall-e-3' est g\u00E9n\u00E9ralement plus performant pour suivre les instructions.
// Vous pouvez le rendre configurable via une variable d'environnement si vous le souhaitez.
const dallEModel = process.env.AI_IMAGE_MODEL || 'dall-e-3'; // Utiliser une variable d'env ou une valeur par d\u00E9faut


// Les prompts qui seront envoy\u00E9s par la suite pour g\u00E9n\u00E9rer une image
// Adaptez ce prompt initial si n\u00E9cessaire pour guider le style de DALL-E,
// en tenant compte du style que vous recherchez (similaire \u00E0 Midjourney si possible).
const initialImagePrompt = 'Generate a highly detailed and artistic illustration in a heroic-fantasy style, suitable for a role-playing game character portrait. Consider the overall mood and atmosphere. Description: ';


// Fonction pour v\u00E9rifier si l'on est en environnement local (pas en production Docker/Cloud)
// On consid\u00E8re "local" si NODE_ENV n'est PAS 'production'.
const isLocalEnvironment = (): boolean => {
    return process.env.NODE_ENV !== 'production';
};


// Fonction contr\u00F4leur pour g\u00E9n\u00E9rer la l'illustration
export const generateImage = async (req: Request, res: Response): Promise<void> => {
    // Chemin de sauvegarde des fichiers, adapt\u00E9 pour un environnement Linux dans le conteneur.
    // Assurez-vous que process.env.DOWNLOAD_FOLDER est d\u00E9fini.
    // Le r\u00E9pertoire devrait \u00EAtre mont\u00E9 en volume si vous voulez r\u00E9cup\u00E9rer les fichiers sur l'h\u00F4te.
    const pathSrc: string = `/app/downloads/${process.env.DOWNLOAD_FOLDER || 'default'}/`; // Chemin adapt\u00E9 pour le conteneur
    const imgName: string = `${process.env.DOWNLOAD_FOLDER || 'default'}-openai-image_${Math.floor(Date.now() / 1000)}.png`; // Nom de fichier
    const maxRetries = 3; // Nombre maximal de tentatives
    let retryCount = 0;

    // Cr\u00E9er le r\u00E9pertoire de t\u00E9l\u00E9chargement si n\u00E9cessaire ET si on est en local
    if (isLocalEnvironment() && !fs.existsSync(pathSrc)) {
        try {
            fs.mkdirSync(pathSrc, { recursive: true, mode: 0o755 });
            console.log(`Local download directory created: ${pathSrc}`);
        } catch (e) {
            console.error(`Error creating local download directory ${pathSrc}:`, e);
            // L'erreur n'est pas bloquante si la sauvegarde locale est optionnelle
        }
    }


    while (retryCount < maxRetries) {
        try {
            const prompt = req.body.prompt; // Le prompt texte re\u00E7u de l'API Java
            console.log('generateImage :: prompt re\u00E7u :', prompt);

            if (!prompt) {
                console.error('No prompt provided.');
                res.status(400).json({ message: 'No prompt provided.' });
                return;
            }

            // Combiner le prompt initial avec le prompt utilisateur
            const fullPrompt = initialImagePrompt + prompt;

            // --- Appel \u00E0 l'API DALL-E ---
            console.log(`Calling OpenAI DALL-E API with model: ${dallEModel}`);
            const response = await openai.images.generate({
                model: dallEModel, // Utilisez le mod\u00E8le DALL-E choisi
                prompt: fullPrompt, // Le prompt pour la g\u00E9n\u00E9ration
                n: 1, // Nombre d'images \u00E0 g\u00E9n\u00E9rer (ici, 1)
                size: "1024x1024", // Taille de l'image (adaptez si n\u00E9cessaire et support\u00E9e par le mod\u00E8le)
                response_format: 'b64_json', // Demander la r\u00E9ponse en Base64 JSON pour l'API Java
            });
            console.log('OpenAI API response received.');

            // --- Traitement de la r\u00E9ponse DALL-E ---
            // La r\u00E9ponse pour 'b64_json' est un objet avec une propri\u00E9t\u00E9 'data' qui est un tableau
            // Chaque \u00E9l\u00E9ment du tableau a une propri\u00E9t\u00E9 'b64_json' contenant la Base64 de l'image
            if (response.data && response.data.length > 0 && response.data[0].b64_json) {
                const imageBase64 = response.data[0].b64_json;
                const buffer = Buffer.from(imageBase64, 'base64');

                // Enregistrer l'image dans un fichier conditionnellement \u00E0 l'environnement local
                if (isLocalEnvironment()) {
                    try {
                        fs.writeFileSync(pathSrc + imgName, buffer);
                        console.log(`> Local image saved to: ${pathSrc}${imgName}`);
                        console.log(`> Image data obtained (Base64), size: ${buffer.length} bytes.`);
                    } catch (writeError) {
                        console.error(`Error writing local image file ${pathSrc}${imgName}:`, writeError);
                        // L'erreur n'est pas bloquante si la sauvegarde locale est optionnelle
                    }
                } else {
                    console.log('> Running in non-local environment, skipping local image save.');
                }


                // Renvoyer la Base64 de l'image \u00E0 l'API Java
                res.status(200).json({ image: imageBase64, message: "Image generation processed by DALL-E" });
                return; // Succ\u00E8s, on sort de la boucle de r\u00E9essai
            } else {
                // Si la r\u00E9ponse n'est pas au format attendu ou manque de donn\u00E9es
                console.error('Unexpected OpenAI API response structure or missing image data:', JSON.stringify(response));
                res.status(500).json({ message: "Image generation failed: unexpected response from DALL-E API." });
                return;
            }

        } catch (err: any) { // Caster l'erreur pour acc\u00E9der aux d\u00E9tails
            console.error("Error during generateImage API call to DALL-E:", err);

            // G\u00E9rer sp\u00E9cifiquement les erreurs de l'API OpenAI si n\u00E9cessaire
            if (err.response && err.response.data && err.response.data.error) {
                console.error("OpenAI Error Details:", err.response.data.error);
                // Pour un r\u00E9essai sur 500 ou 503 (erreurs serveurs OpenAI)
                if (err.response.status === 500 || err.response.status === 503) {
                    console.log(`OpenAI API Server Error (${err.response.status}), retrying (${retryCount + 1}/${maxRetries})...`);
                    await new Promise(resolve => setTimeout(resolve, (retryCount + 1) * 1000)); // D\u00E9lai exponentiel
                    retryCount++;
                    continue; // Continuer la boucle while pour r\u00E9essayer
                } else {
                    // Pour d'autres erreurs (ex: 400 Bad Request, 401 Unauthorized, 429 Rate Limit)
                    res.status(err.response.status || 500).json({ // Utiliser le statut de l'erreur OpenAI si disponible
                        message: "Error during generating image with DALL-E",
                        error: err.response.data.error.message || "Unknown OpenAI API error"
                    });
                    return;
                }

            } else {
                // G\u00E9rer les erreurs qui ne proviennent pas directement de l'API OpenAI (ex: erreur r\u00E9seau)
                console.error("Non-OpenAI Error:", err);
                res.status(500).json({ message: "An unexpected error occurred", error: err.message });
                return;
            }
        }
    }

    // Si on sort de la boucle sans succ\u00E8s apr\u00E8s les r\u00E9essais
    console.error(`Image generation failed after ${maxRetries} retries.`);
    res.status(500).json({ message: "Image generation service unavailable after multiple retries." });
};

// N'oubliez pas d'exporter generateImage si ce fichier est utilis\u00E9 comme module dans votre application NestJS.