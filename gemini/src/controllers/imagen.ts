import {Request, Response} from "express";
import {GenerateContentResult, GenerativeModel, GoogleGenerativeAI, Part} from '@google/generative-ai';
import * as fs from 'fs';


// Configuration requise
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(process.env.API_KEY);
const model: GenerativeModel = genAI.getGenerativeModel({
    model: process.env.AI_IMAGE_MODEL,
    generationConfig: {
        // @ts-expect-error - Gemini API JS is missing this type
        responseModalities: ["Text", "Image"],
    },
});

// Les prompts qui seront envoyés par la suite pour générer une réponse attendue
const imagenPrompt = 'Using Imagen3, generate an illustration in a heroic-fantasy style, but not realistic, close to that of the French illustrator Grosnez (https://www.artstation.com/grosnez) based on this prompt: ';
const testGeminiImagePrompt = 'Un homme grand et musclé aux cheveux bruns et aux yeux verts, portant une armure pratique et tenant un marteau de forge.';

// Fonction contrôleur pour gérer les conversations
export const generateImage = async (req: Request, res: Response): Promise<void> => {
    const pathSrc: string = 'C:\\Users\\'+process.env.USER_WINDOW+'\\Downloads\\FantasyGenerator\\'
    const imgName: string = 'FantasyGenerator-imagen_'+Math.floor(Date.now()/1000)+'.png';
    try {
        console.log('generateImage :: prompt recu :', req.body.prompt); // Log du prompt reçu
        const prompt = req.body.prompt; // Assurez-vous que le prompt est extrait correctement
        if (!prompt) {
            console.error('No prompt provided.');
            res.status(400).json({ message: 'No prompt provided.' });
            return;
        }

        const result: GenerateContentResult = await model.generateContent({
            contents: [{ role: "user", parts: [{ text: prompt }] }],
        });
        console.log('Gemini API response:', result); // Log de la réponse de l'API Gemini

        if (result && result.response && result.response.candidates && result.response.candidates.length > 0) {
            result.response.candidates[0].content.parts.forEach((part: Part) => {
                if (part.text) {
                    console.log(part.text);
                } else if (part.inlineData) {
                    const imageData: string = part.inlineData.data;
                    const buffer: Buffer<ArrayBuffer> = Buffer.from(imageData, 'base64');
                    fs.writeFileSync(pathSrc + imgName, buffer);
                    console.log('> L\'image enregistrée ici :', pathSrc + imgName);
                } else {
                    console.log('Unexpected response part:', part);
                }
            });
        } else {
            if (result && result.response && result.response.promptFeedback && result.response.promptFeedback.blockReason) {
                console.error('Gemini API blocked the prompt:', result.response.promptFeedback.blockReason);
                res.status(400).json({ message: 'Prompt blocked by Gemini API', error: result.response.promptFeedback.blockReason });
            }
            console.log('No response candidates found.');
        }
        res.status(200).json({ message: "Image generation processed" });
    } catch (err) {
        console.error(err);
        res.status(500).json({message: "Error during generating image", error: err.message});
    }
};