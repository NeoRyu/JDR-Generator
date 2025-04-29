import {Request, Response} from "express";
import * as fs from 'fs';
// Import OpenAI library
import OpenAI from 'openai';

// --- Global Configuration ---
const apiKey = process.env.API_KEY;
const dallEModel = process.env.AI_IMAGE_MODEL || 'dall-e-3';
const downloadFolder = process.env.DOWNLOAD_FOLDER || 'default';
const initialImagePrompt = 'Generate a highly detailed and artistic illustration in a heroic-fantasy style, suitable for a role-playing game character portrait. The style should resemble digital painting with soft gradients and a focus on character detail. Consider the overall mood and atmosphere. ';
const maxRetries = 3;

// --- OpenAI Configuration ---
if (!apiKey) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
    // Handle this error - the application might not start or API calls will fail
}
const openai = new OpenAI({ apiKey });

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== 'production';

// Controller function to generate the illustration
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

            const fullPrompt = "A fluffy white cat."; // initialImagePrompt + prompt;

            console.log('generateImage :: fullPrompt being sent:', fullPrompt);
            console.log(`Calling OpenAI DALL-E API with model: ${dallEModel}`);
            const response = await openai.images.generate({
                model: dallEModel,
                prompt: fullPrompt,
                n: 1,
                // DALL-E 3 supports sizes: "1024x1024", "1792x1024", "1024x1792".
                // To maintain a 721x1024 aspect ratio while reducing size,
                // we can choose the closest supported dimensions.
                // "1024x1024" is a supported and square option.
                // Alternatively, we could try "1792x1024" and see if it's acceptable.
                // Let's start with a slightly reduced but supported size.
                size: "1024x1024", // Default and supported size. Consider "512x512" for lower cost.
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