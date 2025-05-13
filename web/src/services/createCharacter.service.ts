// createCharacter.service.tsx
import axios from "axios";
import {useMutation} from "react-query";

function cleanData(chaine: string | undefined): string | undefined {
  if (!chaine) return chaine;
  return chaine;
}

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080"; // Fallback pour le dev local

export const useCreateCharacter = () => {
  return useMutation(
    "characters",
    async (payloadData: {
      promptSystem: string;
      promptRace: string;
      promptGender: string;
      promptClass?: string;
      promptDescription?: string;
    }) => {
      const cleanedData = {
        ...payloadData,
        promptSystem: cleanData(payloadData.promptSystem),
        promptRace: cleanData(payloadData.promptRace),
        promptGender: cleanData(payloadData.promptGender),
        promptClass: cleanData(payloadData.promptClass),
        promptDescription: cleanData(payloadData.promptDescription),
      };
      return await axios.post(
        `${API_BASE_URL}/characters/generate`,
        cleanedData,
        {
          headers: {
            "Content-Type": "application/json",
            // Les headers CORS sont gérés par le serveur
            // 'Access-Control-Allow-Origin': 'http://localhost:8080',
            // 'Access-Control-Allow-Credentials': 'true',
          },
        },
      );
    },
  );
};
