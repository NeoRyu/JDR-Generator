import {Request, Response} from "express";
import dotenv from "dotenv";
import fs from "fs";
import OpenAI from "openai";

dotenv.config();

// --- Global Configuration ---
const apiKey = process.env.API_KEY;
const openAITextModel = process.env.AI_TEXT_MODEL || "gpt-3.5-turbo";
const configMaxOutputTokens: number = +(process.env.MAX_TOKENS || 2048);
const temperature: number = +(process.env.TEMPERATURE || 0.7);
const topP: number = +(process.env.TOP_P || 0.8);
const downloadFolder = process.env.DOWNLOAD_FOLDER || "default";

// --- OpenAI Configuration ---
if (!apiKey) {
  console.error(
    "API_KEY environment variable is not set. OpenAI API calls will fail.",
  );
}
const openai = new OpenAI({ apiKey });

// System prompt for OpenAI
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

// Specific prompt for statistics
const statsUserPrompt = (characterData: any): string => {
  const data = characterData.data;
  return (
    `Based on the following character information, generate their statistics, skills, disciplines, and equipment in JSON format. If the game system (like Das Schwarze Auge) implies specific stat blocks, use those or invent a plausible structure if the system is not fully known. Character Data: '''` +
    JSON.stringify(data) +
    `'''`
  );
};

// Function to check if we are in a local environment
const isLocalEnvironment = (): boolean => process.env.NODE_ENV !== "production";

// Controller function to generate the statistics (JSON text)
export const generateStats = async (req: Request, res: Response) => {
  const pathSrc: string = `/app/downloads/${downloadFolder}/`;
  const txtName: string = `${downloadFolder}-openai-stats_${Math.floor(Date.now() / 1000)}.txt`;

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
    const characterData = req.body;
    console.log("OpenAI Stats :: Received Data for Stats:", characterData);

    if (!characterData || !characterData.data) {
      console.error("Invalid or missing character data for stats generation.");
      res.status(400).json({ message: "Invalid or missing character data." });
      return;
    }

    // --- Call OpenAI API (Chat Completions) ---
    console.log(`Calling OpenAI API with model: ${openAITextModel} for stats`);
    const completion = await openai.chat.completions.create({
      model: openAITextModel,
      messages: [
        { role: "system", content: systemPrompt },
        { role: "user", content: statsUserPrompt(characterData) },
      ],
      response_format: { type: "json_object" },
      temperature: temperature,
      max_tokens: configMaxOutputTokens,
    });
    console.log("OpenAI API response received.");

    let responseText: string | null = null;
    if (
      completion.choices &&
      completion.choices.length > 0 &&
      completion.choices[0].message &&
      completion.choices[0].message.content
    ) {
      responseText = completion.choices[0].message.content;
      console.log("Response text content received.");
    } else {
      console.error(
        "OpenAI API response structure unexpected or missing content:",
        JSON.stringify(completion),
      );
      res
        .status(500)
        .json({
          message: "Error during generating stats: unexpected OpenAI response.",
        });
      return;
    }

    try {
      const parsedJson = JSON.parse(responseText);
      const finalResponseText = JSON.stringify(parsedJson);

      if (isLocalEnvironment()) {
        try {
          fs.writeFileSync(pathSrc + txtName, finalResponseText);
          console.log(`> Stats text saved locally to: ${pathSrc}${txtName}`);
        } catch (writeError) {
          console.error(
            `Error writing local stats text file ${pathSrc}${txtName}:`,
            writeError,
          );
        }
      }

      res.status(200).send({ response: finalResponseText });
    } catch (jsonError: any) {
      console.error(
        "Error parsing JSON response from OpenAI Stats:",
        jsonError,
      );
      console.error("Invalid JSON text received:", responseText);
      res
        .status(500)
        .json({
          message: "Error parsing generated JSON stats.",
          error: jsonError.message,
          rawResponse: responseText,
        });
    }
  } catch (err: any) {
    console.error("Error during OpenAI Stats API call:", err);
    if (err.response && err.response.data && err.response.data.error) {
      console.error("OpenAI Error Details:", err.response.data.error);
      res.status(err.response.status || 500).json({
        message: "Error during generating stats with OpenAI",
        error: err.response.data.error.message || "Unknown OpenAI API error",
      });
    } else {
      console.error("Non-OpenAI Error:", err);
      res
        .status(500)
        .json({ message: "An unexpected error occurred", error: err.message });
    }
  }
};
