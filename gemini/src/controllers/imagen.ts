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
export const generateImage = async (req: Request, res: Response) => {
    try {
        const { prompt } = req.body;
        const pathSrc: string = 'C:\\Users\\'+process.env.USER_WINDOW+'\\Downloads\\FantasyGenerator\\'
        const imgName: string = 'FantasyGenerator-imagen_'+Math.floor(Date.now()/1000)+'.png';

        const result: GenerateContentResult = await model.generateContent({
            contents: [{ role: "user", parts: [{ text: prompt }] }],
        });

        result.response.candidates[0].content.parts.forEach((part: Part) => {
            if (part.text) {
                console.log(part.text);
            } else if (part.inlineData) {
                const imageData: string = part.inlineData.data;
                const buffer: Buffer<ArrayBuffer> = Buffer.from(imageData, 'base64');
                fs.writeFileSync(pathSrc + imgName, buffer);
                console.log('Image saved as ' + imgName);
            }
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: "Error during generating image" });
    }
};