import {Request, Response} from "express";
import dotenv from "dotenv";
import fs from "fs";
import OpenAI from 'openai'; // Importer la biblioth\u00E8que OpenAI

dotenv.config();

// --- Configuration OpenAI ---
// Assurez-vous que process.env.API_KEY contient votre cl\u00E9 API OpenAI
if (!process.env.API_KEY) {
    console.error("API_KEY environment variable is not set. OpenAI API calls will fail.");
    // G\u00E9rer cette erreur - l'application peut ne pas d\u00E9marrer ou les appels API \u00E9choueront
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
const topK: number = +(process.env.TOP_K || 40); // OpenAI utilise top_p, pas top_k de la m\u00EAme mani\u00E8re, mais on peut garder la structure de config si utile


// Les prompts qui seront envoy\u00E9s \u00E0 l'API OpenAI
// OpenAI utilise un format de conversation avec des r\u00F4les (system, user, assistant)
const systemPrompt = `You are an expert in RPGs, with extensive knowledge of various gaming systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. Your response must not rely on previous exchanges, must be in JSON format only, with the following data filled in, and the texts need to be in French (fr-FR), except for the name:
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

// Fonction pour formatter le prompt bas\u00E9 sur le contexte re\u00E7u
const formatUserPrompt = (data: any): string => {
    const context = `
    game system: ${data.promptSystem}
    race: ${data.promptRace}
    gender: ${data.promptGender}
    class: ${data.promptClass}
    description: ${data.promptDescription}
    `;
    return `Please use the context provided below to generate the character details in JSON format as requested in the system prompt. Context: '''` + context + `'''`;
}

// Fonction pour v\u00E9rifier si l'on est en environnement local (pas en production Docker/Cloud)
// On peut d\u00E9cider d'\u00EAtre en "local" si la variable NODE_ENV n'est PAS 'production'
const isLocalEnvironment = (): boolean => {
    return process.env.NODE_ENV !== 'production';
};


// Fonction contr\u00F4leur pour g\u00E9n\u00E9rer le background (texte JSON)
export const generateResponse = async (req: Request, res: Response) => {
    // Chemain de sauvegarde des fichiers, adapt\u00E9 pour un environnement Linux dans le conteneur
    // Assurez-vous que process.env.DOWNLOAD_FOLDER est d\u00E9fini et que le r\u00E9pertoire est mont\u00E9 en volume si vous voulez les r\u00E9cup\u00E9rer sur l'h\u00F4te.
    const pathSrc: string = `/app/downloads/${process.env.DOWNLOAD_FOLDER || 'default'}/`;
    const txtName: string = `${process.env.DOWNLOAD_FOLDER || 'default'}-openai-background_${Math.floor(Date.now()/1000)}.txt`;

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


    try {
        const { prompt } = req.body; // Le prompt principal (pas utilis\u00E9 directement dans le prompt structur\u00E9, mais pr\u00E9sent dans le corps de la requ\u00EAte Gemini)
        const contextData = req.body; // Les donn\u00E9es de contexte (system, race, gender, class, description)


        console.log('OpenAI Background :: Contexte re\u00E7u :', contextData);

        // --- Appel \u00E0 l'API OpenAI (Chat Completions) ---
        console.log(`Calling OpenAI API with model: ${openAITextModel}`);
        const completion = await openai.chat.completions.create({
            model: openAITextModel,
            messages: [
                { role: "system", content: systemPrompt }, // Le r\u00F4le system d\u00E9finit le comportement et le format de sortie
                { role: "user", content: formatUserPrompt(contextData) } // Le prompt utilisateur avec le contexte
            ],
            response_format: { type: "json_object" }, // Demander la r\u00E9ponse au format JSON
            temperature: temperature,
            max_tokens: configMaxOutputTokens,
            // top_p: topP // top_p est support\u00E9
            // top_k n'est g\u00E9n\u00E9ralement pas utilis\u00E9 de la m\u00EAme mani\u00E8re par OpenAI Chat, on peut l'ignorer
        });
        console.log('OpenAI API response received.');


        // --- Traitement de la r\u00E9ponse OpenAI ---
        let responseText: string | null = null;
        if (completion.choices && completion.choices.length > 0 && completion.choices[0].message && completion.choices[0].message.content) {
            responseText = completion.choices[0].message.content;
            console.log('Response text content received.');
            // Normalement, si response_format: { type: "json_object" } est support\u00E9 et respect\u00E9,
            // responseText devrait \u00EAtre une cha\u00EEne JSON valide.
        } else {
            console.error("OpenAI API response structure unexpected or missing content:", JSON.stringify(completion));
            res.status(500).json({ message: "Error during generating background: unexpected OpenAI response." });
            return;
        }


        // La r\u00E9ponse est cens\u00E9e \u00EAtre un objet JSON valide gr\u00E2ce \u00E0 response_format: { type: "json_object" }
        // Cependant, on peut inclure une v\u00E9rification et un parsing pour s'assurer et uniformiser la sortie.
        try {
            const parsedJson = JSON.parse(responseText);
            // Assurez-vous que le JSON pars\u00E9 correspond \u00E0 la structure attendue si n\u00E9cessaire
            const finalResponseText = JSON.stringify(parsedJson); // Renvoyer une cha\u00EEne JSON propre

            // Sauvegarde du fichier texte conditionnelle \u00E0 l'environnement local
            if (isLocalEnvironment()) {
                try {
                    fs.writeFileSync(pathSrc + txtName, finalResponseText);
                    console.log(`> Background text saved locally to: ${pathSrc}${txtName}`);
                } catch (writeError) {
                    console.error(`Error writing local background text file ${pathSrc}${txtName}:`, writeError);
                    // L'erreur n'est pas bloquante si la sauvegarde locale est optionnelle
                }
            }


            res.status(200).send({ response: finalResponseText }); // Renvoyer le JSON \u00E0 l'API Java

        } catch (jsonError) {
            console.error("Error parsing JSON response from OpenAI:", jsonError);
            console.error("Invalid JSON text received:", responseText);
            res.status(500).json({ message: "Error parsing generated JSON background.", error: jsonError.message });
        }


    } catch (err: any) { // Caster l'erreur en 'any' pour acc\u00E9der aux d\u00E9tails de l'erreur OpenAI
        console.error("Error during OpenAI Background API call:", err);
        // G\u00E9rer sp\u00E9cifiquement les erreurs de l'API OpenAI
        if (err.response && err.response.data && err.response.data.error) {
            console.error("OpenAI Error Details:", err.response.data.error);
            res.status(err.response.status || 500).json({
                message: "Error during generating background with OpenAI",
                error: err.response.data.error.message || "Unknown OpenAI API error"
            });
        } else {
            // G\u00E9rer les erreurs non sp\u00E9cifiques \u00E0 l'API OpenAI
            console.error("Non-OpenAI Error:", err);
            res.status(500).json({ message: "An unexpected error occurred", error: err.message });
        }
    }
};

// Exporter la fonction pour qu'elle puisse \u00EAtre utilis\u00E9e dans app.ts
// Note : L'historique de conversation (`conversationContext`) n'est pas g\u00E9r\u00E9 ici car OpenAI API est stateless par d\u00E9faut.
// La gestion de l'historique pour une conversation suivie devrait \u00EAtre g\u00E9r\u00E9e dans le code appelant (votre API Java)
// en envoyant les messages pr\u00E9c\u00E9dents dans le tableau `messages` de l'appel `chat.completions.create`.
// Pour cette impl\u00E9mentation simple qui g\u00E9n\u00E8re un background complet \u00E0 chaque appel, l'historique n'est pas n\u00E9cessaire.