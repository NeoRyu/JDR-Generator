import {Request, Response} from "express";
import * as fs from "fs";

// --- Global Configuration ---
const maxRetries = 3;
const pollingMaxAttempts = 30; // Nombre de tentatives ajustable pour le polling du statut
const pollingDelayMs = 2000; // Délai entre les tentatives de polling (2 secondes)

// TODO : Mettre en place différents styles d'illustrations, modifier le contexte pour add cela.
// const chillIllustrationPrompt = "Generate a highly detailed and artistic illustration in a heroic-fantasy style, suitable for a role-playing game character portrait. The style should resemble digital painting with soft gradients and a focus on character detail. Consider the overall mood and atmosphere. ";
const photoRealisticPrompt = "Create a Photorealistic studio portrait of a mature-looking individual, taken with a professional camera, in a heroic fantasy setting. The lighting is dramatic and natural, emphasizing the character's features. The background is a smooth, dark gradient, carefully blurred to keep focus on the subject. The overall mood is serious and immersive, aiming for a high-resolution, unedited photograph. ";

// Interfaces pour les réponses de l'API Freepik
interface FreepikTaskCreationResponse {
  data?: { // <-- AJOUT DE CETTE LIGNE
    task_id?: string;
    status?: string;
    generated?: string[];
  };
  error?: {
    code: string;
    message: string;
  };
}

interface FreepikTaskStatusResponse {
  data?: {
    task_id?: string;
    status?: 'IN_PROGRESS' | 'COMPLETED' | 'FAILED' | string;
    generated?: string[];
    error?: {
      code: string;
      message: string;
    };
  };
  code?: string;
  message?: string;
}

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== "production";

// Controller function to generate the illustration
export const generateImage = async (
  req: Request,
  res: Response,
): Promise<void> => {
  // LECTURE DES VARIABLES D'ENVIRONNEMENT ICI (Sinon initialisées trop tôt)
  const freepikModel = process.env.AI_IMAGE_MODEL || "flux-dev";
  const downloadFolder = process.env.DOWNLOAD_FOLDER || "default";
  const userWindow = process.env.USER_WINDOW || "default";

  const pathSrc: string = `C:\\Users\\${userWindow}\\Downloads\\${downloadFolder}\\`;
  const imgBaseName: string = `${downloadFolder}-freepik-image_${Math.floor(Date.now() / 1000)}.png`;
  let retryCount = 0;
  const freepikApiKey = "" + process.env.API_KEY;

  // Create local download directory if needed
  if (isLocalEnvironment() && !fs.existsSync(pathSrc)) {
    try {
      fs.mkdirSync(pathSrc, { recursive: true, mode: 0o755 });
      console.log(`Local download directory created: ${pathSrc}`);
    } catch (e) {
      console.error(`Error creating local download directory ${pathSrc}:`, e);
    }
  }

  let taskId: string | null = null;

  while (retryCount < maxRetries) {
    try {
      const prompt = req.body.prompt;
      console.log("generateImage :: prompt received:", prompt);

      if (!prompt) {
        console.error("No prompt provided.");
        res.status(400).json({ message: "No prompt provided." });
        return;
      }

      // const fullPrompt = chillIllustrationPrompt + prompt;
      const fullPrompt = photoRealisticPrompt + prompt;

      console.log("generateImage :: fullPrompt being sent:", fullPrompt);
      console.log(`Calling FREEPIK API with model: ${freepikModel}`);

      const headers = {
        "Content-Type": "application/json",
        "x-freepik-api-key": freepikApiKey,
      };

      const response = await fetch(
          "https://api.freepik.com/v1/ai/text-to-image/flux-dev",
          {
            method: "POST",
            headers: headers,
            body: JSON.stringify({
              prompt: fullPrompt,
              aspect_ratio: req.body.aspectRatio || 'square_1_1',
            }),
          },
      );
      const data: FreepikTaskCreationResponse = await response.json();

      // Vérifie si la réponse est OK OU si elle contient un task_id encapsulé
      if (response.ok && data?.data?.task_id) {
        taskId = data.data.task_id;
        console.log(`Freepik task created. Task ID: ${taskId}`);
        break;
      } else {
        // Si response ok est false, on affiche l'erreur, mais on vérifie si un task_id est tout de même disponible.
        if (data?.data?.task_id) {
          taskId = data.data.task_id;
          console.log(`Freepik task created, but response.ok was false. Task ID: ${taskId}`);
          // Ne pas break ici, car on veut voir la raison si le status n'est pas COMPLETED
          // On continue le polling dans ce cas, si le status n'est pas FAILED
        } else {
          console.error("Freepik API error during task creation. Full response data:", JSON.stringify(data, null, 2));
          // Logique existante de retry ou d'erreur
          if (response.status === 500 || response.status === 503) {
            console.log(
                `Freepik API Server Error (${response.status}), retrying (${retryCount + 1}/${maxRetries})...`,
            );
            await new Promise((resolve) =>
                setTimeout(resolve, (retryCount + 1) * 1000),
            );
            retryCount++;
            continue;
          } else {
            res.status(response.status || 500).json({
              message: "Image task creation failed",
              error: data.error ? data.error.message : "Unknown error",
            });
            return;
          }
        }
      }

    } catch (err: any) {
      console.error("Error during Freepik task creation API call:", err);
      // Retenter pour les erreurs réseau/inattendues
      if (retryCount < maxRetries - 1) {
        console.log(
            `Network/Unexpected Error, retrying (${retryCount + 1}/${maxRetries})...`,
        );
        await new Promise((resolve) =>
            setTimeout(resolve, (retryCount + 1) * 1000),
        ); // Délai croissant
        retryCount++;
        continue;
      } else {
        res
            .status(500)
            .json({ message: "An unexpected error occurred during task creation", error: err.message });
        return;
      }
    }

  }

  if (!taskId) {
    console.error(`Freepik task creation failed after ${maxRetries} retries.`);
    res.status(500).json({
      message: "Freepik image generation service unavailable (task creation failed).",
    });
    return;
  }

  // --- Étape 2 : Polling du statut et récupération de l'URL de l'image (requête GET) ---
  retryCount = 0; // Réinitialiser le compteur de tentatives pour le polling
  while (retryCount < pollingMaxAttempts) {
    try {
      console.log(`Polling Freepik task status for Task ID: ${taskId} (Attempt ${retryCount + 1}/${pollingMaxAttempts})`);
      const response = await fetch(
          `https://api.freepik.com/v1/ai/text-to-image/flux-dev/${taskId}`,
          {
            headers: {
              "x-freepik-api-key": freepikApiKey, // Utilisation de API_KEY
            },
          },
      );

      const data: FreepikTaskStatusResponse = await response.json();

      // Vérifie si la réponse contient bien l'objet 'data' imbriqué et ses propriétés
      if (response.ok && data?.data) {
        const status = data.data.status;
        console.log(`Task ${taskId} status: ${status}`);

        if (status === 'COMPLETED') {
          if (data.data.generated && data.data.generated.length > 0) {
            const imageUrl = data.data.generated[0];
            console.log(`Image generated successfully. URL: ${imageUrl}`);

            let imageBase64: string | null = null;

            // Télécharger l'image depuis l'URL et la convertir en Base64
            try {
              const imageResponse = await fetch(imageUrl);
              if (!imageResponse.ok) {
                throw new Error(`Failed to download image from ${imageUrl}: ${imageResponse.statusText}`);
              }
              const imageBuffer = await imageResponse.arrayBuffer();
              const buffer = Buffer.from(imageBuffer);
              imageBase64 = buffer.toString("base64");

              // Sauvegarder l'image localement si en environnement local
              if (isLocalEnvironment()) {
                const imgName = `${imgBaseName}.png`;
                fs.writeFileSync(pathSrc + imgName, buffer);
                console.log(`> Local image saved to: ${pathSrc}${imgName}`);
              } else {
                console.log("> Running in non-local environment, skipping local image save.");
              }

            } catch (downloadError) {
              console.error(`Error downloading or converting image from URL ${imageUrl}:`, downloadError);
              res.status(500).json({
                message: "Image generation failed: could not download or convert image.",
                error: (downloadError as Error).message,
              });
              return;
            }

            if (imageBase64) {
              res.status(200).json({
                image: imageBase64,
                message: "Image generation processed by Freepik",
              });
              return;
            } else {
              res.status(500).json({
                message: "Image generation failed: no image data after conversion.",
              });
              return;
            }
          } else {
            console.error(`Freepik API returned 'COMPLETED' status but no image URLs for task ${taskId}.`);
            res.status(500).json({
              message: "Image generation completed but no image URL found.",
            });
            return;
          }

        } else if (status === 'FAILED') {
          console.error(`Freepik task ${taskId} failed. Error: ${JSON.stringify(data.data.error)}`); // <-- MODIFIÉ ICI
          res.status(500).json({
            message: "Image generation failed.",
            error: data.data.error ? data.data.error.message : "Unknown error during generation.", // <-- MODIFIÉ ICI
          });
          return;

        } else { // IN_PROGRESS ou autre statut intermédiaire
          await new Promise((resolve) => setTimeout(resolve, pollingDelayMs));
          retryCount++;
        }

      } else {
        // Si response.ok est false ou data.data n'existe pas
        // Afficher la réponse complète pour diagnostiquer pourquoi data.data est manquant
        console.error("Freepik API error or unexpected response structure during task status check:", JSON.stringify(data, null, 2));
        await new Promise((resolve) => setTimeout(resolve, pollingDelayMs));
        retryCount++;
      }

    } catch (err: any) {
      console.error(`Error during Freepik task status check API call for task ${taskId}:`, err);
      await new Promise((resolve) => setTimeout(resolve, pollingDelayMs));
      retryCount++;
    }
  }

  console.error(`Image generation polling failed after ${pollingMaxAttempts} attempts for task ${taskId}.`);
  res.status(500).json({
    message: "Image generation service timed out or failed after multiple retries.",
  });

};
