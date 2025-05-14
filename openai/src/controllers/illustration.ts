import {Request, Response} from "express";
import * as fs from "fs";

// --- Global Configuration ---
const dallEModel = process.env.AI_IMAGE_MODEL || "dall-e-3";
const downloadFolder = process.env.DOWNLOAD_FOLDER || "default";
const initialImagePrompt =
  "Generate a highly detailed and artistic illustration in a heroic-fantasy style, suitable for a role-playing game character portrait. The style should resemble digital painting with soft gradients and a focus on character detail. Consider the overall mood and atmosphere. ";
const maxRetries = 3;

// --- OpenAI Configuration ---
if (!process.env.API_KEY) {
  console.error(
    "API_KEY environment variable is not set. OpenAI API calls will fail.",
  );
}

// Définissez une interface pour la structure de la réponse de l'API OpenAI pour la génération d'images
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

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== "production";

// Controller function to generate the illustration
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
      const prompt = req.body.prompt;
      console.log("generateImage :: prompt received:", prompt);

      if (!prompt) {
        console.error("No prompt provided.");
        res.status(400).json({ message: "No prompt provided." });
        return;
      }

      const fullPrompt = initialImagePrompt + prompt;

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

/*
export const generateImage = async (req: Request, res: Response): Promise<void> => {
    const pathSrc: string = `/app/downloads/${downloadFolder}/`;
    const imgName: string = `${downloadFolder}-openai-image_${Math.floor(Date.now() / 1000)}.png`;
    let retryCount = 0;

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
            const prompt = req.body.prompt;
            console.log('generateImage :: prompt received:', prompt);

            if (!prompt) {
                console.error('No prompt provided.');
                res.status(400).json({ message: 'No prompt provided.' });
                return;
            }

            const fullPrompt = initialImagePrompt + prompt;

            console.log('generateImage :: fullPrompt being sent:', fullPrompt);
            console.log(`Calling OpenAI DALL-E API with model: ${dallEModel}`);
            const response = await openai.images.generate({
                model: dallEModel,
                prompt: fullPrompt,
                n: 1, // number of illustration generated
                size: "1024x1024", // DALL-E 3 supports sizes: "1024x1024", "1792x1024", "1024x1792".
                quality: "standard", // "standard" is less expensive than "hd".
                style: "natural", // "vivid" or "natural". "natural" seems suitable for the prompt.
                response_format: 'b64_json', // Request Base64 JSON for image data.
            });
            console.log('OpenAI API response received.');

            if (response.data && response.data.length > 0 && response.data[0].b64_json) {
                const imageBase64 = response.data[0].b64_json;
                const buffer = Buffer.from(imageBase64, 'base64');

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

                res.status(200).json({ image: imageBase64, message: "Image generation processed by DALL-E" });
                return;
            } else {
                console.error('Unexpected OpenAI API response structure or missing image data:', JSON.stringify(response));
                res.status(500).json({ message: "Image generation failed: unexpected response from DALL-E API." });
                return;
            }

        } catch (err: any) {
            console.error("Error during generateImage API call to DALL-E:", err);

            if (err.response && err.response.data && err.response.data.error) {
                console.error("OpenAI Error Details:", err.response.data.error);
                if (err.response.status === 500 || err.response.status === 503) {
                    console.log(`OpenAI API Server Error (${err.response.status}), retrying (${retryCount + 1}/${maxRetries})...`);
                    await new Promise(resolve => setTimeout(resolve, (retryCount + 1) * 1000));
                    retryCount++;
                    continue;
                } else {
                    res.status(err.response.status || 500).json({
                        message: "Error during generating image with DALL-E",
                        error: err.response.data.error.message || "Unknown OpenAI API error"
                    });
                    return;
                }
            } else {
                console.error("Non-OpenAI Error:", err);
                res.status(500).json({ message: "An unexpected error occurred", error: err.message });
                return;
            }
        }
    }

    console.error(`Image generation failed after ${maxRetries} retries.`);
    res.status(500).json({ message: "Image generation service unavailable after multiple retries." });
};
*/
