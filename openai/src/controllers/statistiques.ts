import {Request, Response} from "express";
import dotenv from "dotenv";
import fs from "fs";
import OpenAI from 'openai'; // Importer la biblioth\u00E8que OpenAI

dotenv.config();

// --- Configuration OpenAI ---
// Assurez-vous que process.env.API_KEY contient votre cl\u00E9 API OpenAI
if (!process.env.API_KEY) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
    // G\u00E9rer cette erreur
}
const openai = new OpenAI({
    apiKey: process.env.API_KEY as string,
});

// Mod\u00E8le OpenAI \u00E0 utiliser pour la g\u00E9n\u00E9ration de texte (background et stats)
// 'gpt-4o' est le plus r\u00E9cent et performant, 'gpt-3.5-turbo' est plus rapide et moins cher.
const openAITextModel = process.env.AI_TEXT_MODEL || 'gpt-3.5-turbo'; // Utiliser la variable d'env ou une valeur par d\u00E9faut

// Configuration pour l'appel API (peut \u00EAtre ajust\u00E9e via variables d'env)
const configMaxOutputTokens: number = +(process.env.MAX_TOKENS || 2048); // Utiliser la variable d'env ou une valeur par d\u00E9faut
const temperature: number = +(process.env.TEMPERATURE || 0.7);
const topP: number = +(process.env.TOP_P || 0.8);

// Les prompts qui seront envoy\u00E9s \u00E0 l'API OpenAI
// Ce prompt est adapt\u00E9 du v\u00F4tre pour le format de conversation OpenAI (r\u00F4les system/user)
const systemPrompt = `You are an RPG expert with extensive knowledge of various game systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be provided with information on the game system, the character's race and class, and any other information needed to define them, as well as their stats, skills, disciplines, and equipment, in the target game world, to inspire you. If the character doesn't have a world, invent one; otherwise, don't invent it. Your response must not rely on previous exchanges, must be in JSON format only, with the following data filled in, and the texts need to be in French (fr-FR), except for the name:
{
"name": "string",
"age": "number",
"birthPlace": "string",
"residenceLocation": "string",
"reasonForResidence": "string",
"climate": "string",
"commonProblems": "string",
"dailyRoutine": "string",
"parentsAlive": "boolean",
"detailsAboutParents": "string",
"feelingsAboutParents": "string",
"siblings": "string",
"childhoodStory": "string",
"youthFriends": "string",
"pet": "string",
"maritalStatus": "string",
"typeOfLover": "string",
"conjugalHistory": "string",
"children": "number",
"education": "string",
"profession": "string",
"reasonForProfession": "string",
"workPreferences": "string",
"changeInSelf": "string",
"changeInWorld": "string",
"goal": "string",
"reasonForGoal": "string",
"biggestObstacle": "string",
"overcomingObstacle": "string",
"planIfSuccessful": "string",
"planIfFailed": "string",
"selfDescription": "string",
"distinctiveTrait": "string",
"physicalDescription": "string",
"clothingPreferences": "string",
"fears": "string",
"favoriteFood": "string",
"leisureActivities": "string",
"hobbies": "string",
"idealCompany": "string",
"attitudeTowardsGroups": "string",
"attitudeTowardsWorld": "string",
"attitudeTowardsPeople": "string",
"image": "string"
}`;

// Le prompt sp\u00E9cifique pour les statistiques
const statsUserPrompt = (characterData: any): string => {
    // Adapter ce prompt si n\u00E9cessaire en fonction des donn\u00E9es re\u00E7ues et de ce que vous voulez pour les stats
    // Bas\u00E9 sur votre code Gemini, 'req.body.data' contient les donn\u00E9es du personnage.
    const data = characterData.data; // Votre code Gemini passait un objet { data: ... }
    return `Based on the following character information, generate their statistics, skills, disciplines, and equipment in JSON format. If the game system (like Das Schwarze Auge) implies specific stat blocks, use those or invent a plausible structure if the system is not fully known. Character Data: '''` + JSON.stringify(data) + `'''`;
}


// Fonction pour v\u00E9rifier si l'on est en environnement local (pas en production Docker/Cloud)
// On peut d\u00E9cider d'\u00EAtre en "local" si la variable NODE_ENV n'est PAS 'production'
const isLocalEnvironment = (): boolean => {
    return process.env.NODE_ENV !== 'production';
};


// Fonction contr\u00F4leur pour g\u00E9n\u00E9rer les statistiques (texte JSON)
export const generateStats = async (req: Request, res: Response) => {
    // Chemain de sauvegarde des fichiers, adapt\u00E9 pour un environnement Linux dans le conteneur
    const pathSrc: string = `/app/downloads/${process.env.DOWNLOAD_FOLDER || 'default'}/`;
    const txtName: string = `${process.env.DOWNLOAD_FOLDER || 'default'}-openai-stats_${Math.floor(Date.now()/1000)}.txt`;


    // Cr\u00E9er le r\u00E9pertoire de t\u00E9l\u00Echargement si n\u00E9cessaire ET si on est en local
    if (isLocalEnvironment() && !fs.existsSync(pathSrc)) {
        try {
            fs.mkdirSync(pathSrc, { recursive: true, mode: 0o755 });
            console.log(`Local download directory created: ${pathSrc}`);
        } catch (e) {
            console.error(`Error creating local download directory ${pathSrc}:`, e);
            // L'erreur n'est pas bloquante si la sauvegarde locale est optionnelle
        }
    }


    try {
        const characterData = req.body; // Votre API Java envoie les donn\u00E9es du personnage dans req.body
        console.log('OpenAI Stats :: Donn\u00E9es re\u00E7ues pour stats :', characterData);

        if (!characterData || !characterData.data) { // V\u00E9rification bas\u00E9e sur la structure de votre appel
            console.error('Invalid or missing character data for stats generation.');
            res.status(400).json({ message: 'Invalid or missing character data.' });
            return;
        }

        // --- Appel \u00E0 l'API OpenAI (Chat Completions) ---
        console.log(`Calling OpenAI API with model: ${openAITextModel} for stats`);
        const completion = await openai.chat.completions.create({
            model: openAITextModel,
            messages: [
                { role: "system", content: systemPrompt }, // R\u00F4le system pour d\u00E9finir le comportement g\u00E9n\u00E9ral et le format JSON
                // Vous pourriez ajouter un message "assistant" ici si vous aviez un exemple de r\u00E9ponse souhait\u00E9e
                { role: "user", content: statsUserPrompt(characterData) } // Prompt utilisateur avec les donn\u00E9es du personnage
            ],
            response_format: { type: "json_object" }, // Demander explicitement la r\u00E9ponse au format JSON
            temperature: temperature, // Vous pouvez ajuster la temp\u00E9rature pour des r\u00E9ponses plus ou moins cr\u00E9atives
            max_tokens: configMaxOutputTokens, // Limiter la taille de la r\u00E9ponse
        });
        console.log('OpenAI API response received.');


        // --- Traitement de la r\u00E9ponse OpenAI ---
        let responseText: string | null = null;
        if (completion.choices && completion.choices.length > 0 && completion.choices[0].message && completion.choices[0].message.content) {
            responseText = completion.choices[0].message.content;
            console.log('Response text content received.');
            // responseText devrait \u00EAtre une cha\u00EEne JSON valide gr\u00E2ce \u00E0 response_format
        } else {
            console.error("OpenAI API response structure unexpected or missing content:", JSON.stringify(completion));
            res.status(500).json({ message: "Error during generating stats: unexpected OpenAI response." });
            return;
        }

        // V\u00E9rification et parsing du JSON
        try {
            const parsedJson = JSON.parse(responseText);
            // Optional: Ajouter des v\u00E9rifications sur la structure du JSON pars\u00E9 ici
            const finalResponseText = JSON.stringify(parsedJson); // Renvoyer une cha\u00EEne JSON propre

            // Sauvegarde du fichier texte conditionnelle \u00E0 l'environnement local
            if (isLocalEnvironment()) {
                try {
                    fs.writeFileSync(pathSrc + txtName, finalResponseText);
                    console.log(`> Stats text saved locally to: ${pathSrc}${txtName}`);
                } catch (writeError) {
                    console.error(`Error writing local stats text file ${pathSrc}${txtName}:`, writeError);
                    // L'erreur n'est pas bloquante si la sauvegarde locale est optionnelle
                }
            }

            res.status(200).send({ response: finalResponseText }); // Renvoyer le JSON \u00E0 l'API Java

        } catch (jsonError: any) {
            console.error("Error parsing JSON response from OpenAI Stats:", jsonError);
            console.error("Invalid JSON text received:", responseText);
            // Inclure la r\u00E9ponse brute dans l'erreur pour faciliter le d\u00E9bogage
            res.status(500).json({ message: "Error parsing generated JSON stats.", error: jsonError.message, rawResponse: responseText });
        }


    } catch (err: any) { // Caster l'erreur pour acc\u00E9der aux d\u00E9tails
        console.error("Error during OpenAI Stats API call:", err);
        // G\u00E9rer sp\u00E9cifiquement les erreurs de l'API OpenAI
        if (err.response && err.response.data && err.response.data.error) {
            console.error("OpenAI Error Details:", err.response.data.error);
            res.status(err.response.status || 500).json({
                message: "Error during generating stats with OpenAI",
                error: err.response.data.error.message || "Unknown OpenAI API error"
            });
        } else {
            console.error("Non-OpenAI Error:", err);
            res.status(500).json({ message: "An unexpected error occurred", error: err.message });
        }
    }
};

// Exporter la fonction pour qu'elle soit utilis\u00E9e dans app.ts
// Note : L'historique de conversation n'est pas g\u00E9r\u00E9 ici.