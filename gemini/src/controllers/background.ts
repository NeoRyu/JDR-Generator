import {Request, Response} from "express";
import dotenv from "dotenv";
import fs from "fs";
import {GenerateContentResult, GenerativeModel, GoogleGenerativeAI,} from "@google/generative-ai";

dotenv.config();

/**
 * @fileoverview Contrôleur pour la génération de backgrounds de personnages de JDR via l'API Gemini.
 * @description Ce fichier gère la construction de prompts détaillés, l'interaction avec le modèle
 * de texte Gemini, le traitement de la réponse JSON, et la sauvegarde des résultats.
 */

// --- Configuration requise du modèle Gemini ---

/**
 * Instance du client GoogleGenerativeAI.
 * Initialisée avec la clé API depuis les variables d'environnement.
 * @type {GoogleGenerativeAI}
 */
const genAI: GoogleGenerativeAI = new GoogleGenerativeAI(''+process.env.API_KEY);

/**
 * Nombre maximum de tokens pour la réponse générée.
 * Récupéré depuis la variable d'environnement `MAX_TOKENS`, ou 2048 par défaut.
 * @type {number}
 */
const configMaxOutputTokens: number = +(process.env.MAX_TOKENS ?? '2048');

/**
 * Instance du modèle génératif Gemini pour le texte.
 * Configuré avec le modèle de texte spécifié dans les variables d'environnement
 * et les paramètres de génération (maxOutputTokens, temperature, topP, topK).
 * Inclut également des `safetySettings` pour ajuster les seuils de blocage de contenu.
 * @type {GenerativeModel}
 */
const model: GenerativeModel = genAI.getGenerativeModel({
  model: ''+process.env.AI_TEXT_MODEL,
  generationConfig: {
    maxOutputTokens: configMaxOutputTokens,
    temperature: 0.7,
    topP: 0.8,
    topK: 40,
  },
  /*
  // Ajout de safetySettings pour potentiellement réduire les blocages sur des contenus de JDR
  // Ajustez ces seuils avec prudence. BLOCK_ONLY_HIGH est moins restrictif que BLOCK_MEDIUM_AND_ABOVE.
  safetySettings: [
    {
      category: HarmCategory.HARM_CATEGORY_HARASSMENT,
      threshold: HarmBlockThreshold.BLOCK_ONLY_HIGH,
    },
    {
      category: HarmCategory.HARM_CATEGORY_HATE_SPEECH,
      threshold: HarmBlockThreshold.BLOCK_ONLY_HIGH,
    },
    {
      category: HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT,
      threshold: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE, // Peut être plus strict ici
    },
    {
      category: HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT,
      threshold: HarmBlockThreshold.BLOCK_ONLY_HIGH,
    },
   */
});

/**
 * Tableau pour conserver l'historique des conversations (prompt et réponse).
 * Note: Actuellement, cet historique n'est pas utilisé pour influencer les générations futures
 * de manière contextuelle dans les appels suivants à l'API dans ce contrôleur.
 * @type {Array<[string | undefined, string]>}
 */
const conversationContext: any[] = [];


// --- Prompts pour la génération de background ---

/**
 * Prompt de base fournissant le contexte initial et les instructions de formatage à l'IA.
 * Décrit le rôle de l'IA en tant qu'expert en JDR et spécifie le format JSON attendu.
 * @type {string}
 */
const basePrompt = `You are an expert in RPGs, with extensive knowledge of various gaming systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be informed about the game system, the character's race, class, and perhaps some description to inspire you. Your response must not rely on previous exchanges, must be in JSON format only, with the following data filled in, and the texts need to be in French (fr-FR), except for the 'image' which should be in English and the 'name' which should be appropriate for the game universe provided in the context:
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

/**
 * Interface définissant la structure des données de contexte pour la génération du prompt.
 */
function contextPrompt(data: {
  promptSystem: any;
  promptRace: any;
  promptGender: any;
  promptClass: any;
  promptDescription: any;
}): string {
  /**
   * Construit le prompt complet à envoyer à l'API Gemini en combinant le `basePrompt`
   * avec les informations contextuelles spécifiques au personnage.
   *
   * @param {CharacterContextData} data - Les données contextuelles du personnage.
   * @returns {string} Le prompt complet formaté.
   */
  const context = `
    game system: ${data.promptSystem}
    race: ${data.promptRace}
    gender: ${data.promptGender}
    class: ${data.promptClass}
    description: ${data.promptDescription}
    `;
  return (
    basePrompt +
    `Please use the context provided below to generate the character: Context: '''` +
    context +
    `'''`
  );
}


// --- Fonction Contrôleur ---

/**
 * Génère un background de personnage détaillé en utilisant l'API Gemini.
 * La fonction attend des informations contextuelles sur le personnage (système de jeu, race, classe, etc.)
 * dans le corps de la requête. La réponse de l'IA est attendue en format JSON,
 * qui est ensuite validé, nettoyé, et renvoyé au client.
 * Le résultat est également sauvegardé localement en mode développement.
 *
 * @async
 * @param {Request} req - L'objet requête Express. Le corps de la requête (`req.body`)
 *                        doit contenir les propriétés de {@link CharacterContextData}
 *                        et optionnellement `prompt` (pour `conversationContext`).
 * @param {Response} res - L'objet réponse Express.
 * @returns {Promise<void>} Une promesse qui se résout lorsque la réponse a été envoyée.
 *                          En cas de succès, envoie un JSON avec la propriété `response` contenant le background généré.
 *                          En cas d'erreur (validation, API, analyse JSON), envoie un statut d'erreur approprié.
 */
export const generateResponse = async (req: Request, res: Response) => {
  const pathSrc: string =
    "C:\\Users\\" +
    process.env.USER_WINDOW +
    "\\Downloads\\" +
    process.env.DOWNLOAD_FOLDER +
    "\\";
  const txtName: string =
    process.env.DOWNLOAD_FOLDER +
    "-gemini_" +
    Math.floor(Date.now() / 1000) +
    ".txt";
  try {
    const { prompt } = req.body;
    const contextPrompts = {
      promptSystem: req.body.promptSystem,
      promptRace: req.body.promptRace,
      promptGender: req.body.promptGender,
      promptClass: req.body.promptClass,
      promptDescription: req.body.promptDescription,
    };
    console.log("Contexte ::", contextPrompts);

    const result: GenerateContentResult = await model.generateContent(
      contextPrompt(contextPrompts),
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
