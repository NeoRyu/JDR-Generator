import {Request, Response} from "express";
import {
    ChatSession,
    EnhancedGenerateContentResponse,
    GenerateContentResult,
    GenerativeModel,
    GoogleGenerativeAI
} from '@google/generative-ai';
import dotenv from "dotenv";
import fs from "fs";

dotenv.config();

// Configuration requise
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(process.env.API_KEY);
const configMaxOutputTokens: number = +process.env.MAX_TOKENS;
const model: GenerativeModel = genAI.getGenerativeModel({
    model: process.env.AI_TEXT_MODEL,
    generationConfig: {
        maxOutputTokens: configMaxOutputTokens,
        temperature: 0.7,
        topP: 0.8,
        topK: 40,
    },
});

// Ce tableau permet de conserver l'historique des conversations
const conversationContext: any[] = [];

// Les prompts qui seront envoyés par la suite pour générer une réponse attendue
const basePrompt = `You are an expert in RPGs, with extensive knowledge of various gaming systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be informed about the game system, the character's race, class, and perhaps some description to inspire you. Your response needs to be only in JSON format with the following data filled in, and the texts need to be in French (fr-FR), except for the name:
        example answer:
        '''
        {
            'name': 'Eldric Ironhand',
            'âge': 28,
            'birthPlace': 'Citadelle d\\'Ironcrest',
            'childhoodStory': 'J\\'ai grandi dans une famille de forgerons, fasciné par les armes et les armures.',
            'feelingsAboutParents': 'Respecte profondément, en particulier son père, un forgeron renommé.',
            'parentsAlive': true,
            'detailsAboutParents': 'Le père est un maître forgeron ; la mère est une artisane qualifiée.',
            'siblings': 'Une sœur cadette, apprentie alchimiste.',
            'youthFriends': 'Divers, principalement d\\'autres apprentis d\\'artisans locaux.',
            'maritalStatus': 'Célibataire',
            'conjugalHistory': 'Quelques romances passagères, rien de grave.',
            'enfants': 'Aucun',
            'education': 'Éducation de base, mais formation approfondie au combat et à la forge.',
            'profession': 'Guerrier et forgeron',
            'reasonForProfession': 'Passion pour les armes et désir d\\'honorer la tradition familiale.',
            'physicalDescription': 'Grand, musclé, cheveux bruns courts, yeux verts, avec des cicatrices de bataille.',
            'distinctiveTrait': 'Extrêmement doué en forgeage et en combat.',
            'goal': 'Devenez un héros légendaire et forgez une arme unique.',
            'reasonForGoal': 'Désir de prouver votre valeur et vos capacités.',
            'planIfSuccessful': 'Fondez votre propre école de guerriers et de forgerons.',
            'planIfFailed': 'Retournez à la citadelle et continuez en tant que forgeron.',
            'biggestObstacle': 'Prouvez votre valeur dans un monde plein de héros et d\\'aventures.',
            'overcomingObstacle': 'Dévouement implacable à l\\'entraînement et aux missions.',
            'changeInWorld': 'Je veux inspirer les autres à poursuivre leurs rêves.',
            'changeInSelf': 'J\\'espère trouver votre véritable objectif et vos capacités.',
            'fears': 'Être oublié ou considéré comme médiocre.',
            'selfDescription': 'Un guerrier dévoué avec un cœur d\\'artisan.',
            'attitudeTowardsWorld': 'Optimiste et stimulant.',
            'attitudeTowardsPeople': 'Amical mais prudent.',
            'attitudeTowardsGroups': 'Préfère l\\'action individuelle, mais respecte la force des groupes.',
            'leisureActivities': 'Forge, chasse, jeux de stratégie.',
            'clothingPreferences': 'Habillez-vous de manière pratique, en préférant les vêtements qui permettent de bouger facilement.',
            'workPreferences': 'Travaux impliquant des défis physiques et stratégiques.',
            'favoriteFood': 'Ragoût de bœuf en morceaux.',
            'hobbies': 'Forge, entraînement au combat, échecs.',
            'pet': 'Un chien de chasse robuste nommé Brawn.',
            'idealCompany': 'Des compagnons fidèles et stimulants.',
            'typeOfLover': 'Quelqu\\'un de fort, indépendant et passionné.',
            'residenceLocation': 'Une maison modeste dans la Citadelle d\\'Ironcrest.',
            'climat': 'Tempéré, avec des hivers rigoureux.',
            'reasonForResidence': 'Proximité de la famille et accès à la forge familiale.',
            'commonProblems': 'Défis dans la forge, rivalités locales.',
            'dailyRoutine': 'S\\'entraîner, forger, passer du temps avec ses amis et sa famille.',
            'image': 'Un homme grand et musclé aux cheveux bruns et aux yeux verts, portant une armure pratique et tenant un marteau de forge.'
        }
        '''
`;

function contextPrompt(data: any): string {
    return basePrompt + `Please use the context provided below to generate the character:
    Context:
    '''
    game system: ${data.gameSystem}
    race: ${data.race}
    class: ${data.class}
    description: ${data.description}
    '''
`;
}

// Fonction contrôleur pour gérer les conversations
export const generateResponse = async (req: Request, res: Response) => {
    try {
        const { prompt } = req.body;
        const pathSrc: string = 'C:\\Users\\'+process.env.USER_WINDOW+'\\Downloads\\FantasyGenerator\\'
        const txtName: string = 'FantasyGenerator-gemini_'+Math.floor(Date.now()/1000)+'.txt';

        const result: GenerateContentResult = await model.generateContent( // prompt
            contextPrompt({
                gameSystem:'Call of Cthulhu',
                race:'Human',
                class:'Nécromant',
                description:'Il est en possession du Necronomicon.',
            })
        );
        const responseText: string = result.response.text();
        console.log(responseText);

        // Stocke la conversation
        conversationContext.push([prompt, responseText]);
        res.send({ response: responseText });

        fs.writeFileSync(pathSrc + txtName, responseText);

    } catch (err) {
        console.error(err);
        res.status(500).json({ message: "Internal server error" });
    }
};