import {Request, Response} from "express";
import dotenv from "dotenv";
import fs from "fs";
import OpenAI from 'openai';

dotenv.config();

// --- Global Configuration ---
const apiKey = process.env.API_KEY;
const openAITextModel = process.env.AI_TEXT_MODEL || 'gpt-3.5-turbo';
const maxOutputTokens: number = +(process.env.MAX_TOKENS || 2048);
const temperature: number = +(process.env.TEMPERATURE || 0.7);
const topP: number = +(process.env.TOP_P || 0.8);
const downloadFolder = process.env.DOWNLOAD_FOLDER || 'default';

// --- OpenAI Configuration ---
if (!apiKey) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
}
const openai = new OpenAI({ apiKey });

// System prompt for OpenAI
const systemPrompt = `You are an expert in RPGs, with extensive knowledge of various gaming systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be informed about the game system, the character's race, class, and perhaps some description to inspire you. Your response must not rely on previous exchanges, must be in JSON format only, with the following data filled in, and the texts need to be in French (fr-FR), except for the 'image' which should be in English and the 'name' which should be appropriate for the game universe provided in the context:
        example answer:
        '''
        {
            'name': 'Eldric Ironhand',
            'age': 28,
            'birthPlace': 'Citadelle d\\'Ironcrest',
            'residenceLocation': 'Une maison modeste dans la Citadelle d\\'Ironcrest.',
            'reasonForResidence': 'Proximité de la famille et accès à la forge familiale.',
            'climate': 'Tempéré, avec des hivers rigoureux.',
            'commonProblems': 'Défis dans la forge, rivalités locales.',
            'dailyRoutine': 'S\\'entraîner, forger, passer du temps avec ses amis et sa famille.',
            'parentsAlive': true,
            'detailsAboutParents': 'Le père est un maître forgeron ; la mère est une artisane qualifiée.',
            'feelingsAboutParents': 'Respecte profondément ses parents, en particulier son père, un forgeron renommé.',
            'siblings': 'A une soeur cadette qui est actuellement apprentie alchimiste.',
            'childhoodStory': 'J\\'ai grandi dans une famille de forgerons, fasciné par les armes et les armures.',
            'youthFriends': 'Plusieurs amis d'enfance, principalement d\\'autres apprentis d\\'artisans locaux.',
            'pet': 'Un chien de chasse robuste nommé Brawn.',
            'maritalStatus': 'Célibataire',
            'typeOfLover': 'Quelqu\\'un de fort, indépendant et passionné.',
            'conjugalHistory': 'Quelques romances passagères, rien d'important'.',
            'children': 0,
            'education': 'Éducation de base, mais formation approfondie au combat et à la forge.',
            'profession': 'Guerrier et forgeron',
            'reasonForProfession': 'Passion pour les armes et désir d\\'honorer la tradition familiale.',
            'workPreferences': 'Travaux impliquant des défis physiques et d'endurance.',
            'changeInSelf': 'J\\'espère acquerir une compétence me permettant d'exceller dans mon domaine.',
            'changeInWorld': 'Je souhaite être reconnu pour mes talents et poursuivre mes rêves.',
            'goal': 'Devenir un héros légendaire ou forger une arme unique.',
            'reasonForGoal': 'Désir être reconnu pour ma valeur et mes capacités.',
            'biggestObstacle': 'Difficile d'être reconnu dans un monde déjà rempli de héros et d\\'aventuriers.',
            'overcomingObstacle': 'Dévouement implacable à l\\'entraînement et aux missions.',
            'planIfSuccessful': 'Fondez ma propre école de guerriers ou de forgerons.',
            'planIfFailed': 'Retournez à la citadelle et continuez d'étudier en tant que forgeron.',
            'selfDescription': 'Un guerrier dévoué avec un cœur d\\'artisan.',
            'distinctiveTrait': 'Un accident pendant son apprentissage lui a fait perdre un doigt.',
            'physicalDescription': 'Grand, musclé, cheveux bruns courts, yeux verts, avec des cicatrices de bataille.',
            'clothingPreferences': 'S'habille de manière pratique, préférant les vêtements permettant de bouger facilement.',
            'fears': 'Être oublié ou considéré comme médiocre.',
            'favoriteFood': 'Sa nourriture préféré est le ragoût de bœuf en morceaux.',
            'leisureActivities': 'Forger, chasser, et boire quelques pintes de bières.',
            'hobbies': 'S'entrâiner à la forge, entraînement au combat',
            'idealCompany': 'Son groupe doit contenir dans l'idéal des compagnons fidèles et stimulants.',
            'attitudeTowardsGroups': 'Préfère l\\'action individuelle, mais respecte la force de chacun des compagnons du groupe.',
            'attitudeTowardsWorld': 'Sa vision du monde est optimiste et ouvert d'esprit.',
            'attitudeTowardsPeople': 'Amical mais prudent envers les inconnus.',
            'image': "A tall and muscular man with short brown hair and green eyes, equipped with light armor and a blacksmith's hammer."
        }
        '''
`;
const formatUserPrompt = (data: any): string => {
    const context = `
    game system: ${data.promptSystem}
    race: ${data.promptRace}
    gender: ${data.promptGender}
    class: ${data.promptClass}
    description: ${data.promptDescription}
    `;
    return systemPrompt + ` Please use the context provided below to generate the character: Context: '''` + context + `'''`;
}

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== 'production';

// Controller function to generate the background (JSON text)
export const generateResponse = async (req: Request, res: Response) => {
    const pathSrc: string = `/app/downloads/${downloadFolder}/`;
    const txtName: string = `${downloadFolder}-openai-background_${Math.floor(Date.now() / 1000)}.txt`;

    // Create local download directory if needed
    if (isLocalEnvironment() && !fs.existsSync(pathSrc)) {
        try {
            fs.mkdirSync(pathSrc, { recursive: true, mode: 0o755 });
            console.log(`Local download directory created: ${pathSrc}`);
        } catch (e) {
            console.error(`Error creating local download directory ${pathSrc}:`, e);
        }
    }

    try {
        const { prompt } = req.body; // Main prompt (not directly used in the structured prompt)
        const contextData = req.body; // Context data (system, race, gender, class, description)

        console.log('OpenAI Background :: Received Context:', contextData);

        // --- Call OpenAI API (Chat Completions) ---
        console.log(`Calling OpenAI API with model: ${openAITextModel}`);
        const completion = await openai.chat.completions.create({
            model: openAITextModel,
            messages: [
                { role: "system", content: systemPrompt },
                { role: "user", content: formatUserPrompt(contextData) }
            ],
            response_format: { type: "json_object" },
            temperature: temperature,
            max_tokens: maxOutputTokens,
            top_p: topP
        });
        console.log('OpenAI API response received.');

        let responseText: string | null = null;
        if (completion.choices && completion.choices.length > 0 && completion.choices[0].message && completion.choices[0].message.content) {
            responseText = completion.choices[0].message.content;
            console.log('Response text content received.');
        } else {
            console.error("OpenAI API response structure unexpected or missing content:", JSON.stringify(completion));
            res.status(500).json({ message: "Error during generating background: unexpected OpenAI response." });
            return;
        }

        try {
            const parsedJson = JSON.parse(responseText);
            const finalResponseText = JSON.stringify(parsedJson);

            if (isLocalEnvironment()) {
                try {
                    fs.writeFileSync(pathSrc + txtName, finalResponseText);
                    console.log(`> Background text saved locally to: ${pathSrc}${txtName}`);
                } catch (writeError) {
                    console.error(`Error writing local background text file ${pathSrc}${txtName}:`, writeError);
                }
            }

            res.status(200).send({ response: finalResponseText });

        } catch (jsonError) {
            console.error("Error parsing JSON response from OpenAI:", jsonError);
            console.error("Invalid JSON text received:", responseText);
            res.status(500).json({ message: "Error parsing generated JSON background.", error: jsonError.message });
        }

    } catch (err: any) {
        console.error("Error during OpenAI Background API call:", err);
        if (err.response && err.response.data && err.response.data.error) {
            console.error("OpenAI Error Details:", err.response.data.error);
            res.status(err.response.status || 500).json({
                message: "Error during generating background with OpenAI",
                error: err.response.data.error.message || "Unknown OpenAI API error"
            });
        } else {
            console.error("Non-OpenAI Error:", err);
            res.status(500).json({ message: "An unexpected error occurred", error: err.message });
        }
    }
};