// illustrateCharacter.service.tsx
import {useMutation, UseMutationResult} from "react-query";
import axios, {AxiosResponse} from "axios";

const API_BASE_URL = process.env.VITE_API_URL || "http://localhost:8080"; // Fallback

// Permettre aux utilisateurs de générer une nouvelle illustration si l'actuelle ne leur convient pas
// Hook pour la régénération d'illustration (PUT)
export const useRegenerateIllustration = (): UseMutationResult<AxiosResponse<ArrayBuffer>, unknown, number, unknown> => {
    return useMutation(
        "regenerateIllustration",
        async (characterId: number) => {
            return await axios.put(
                `${API_BASE_URL}/characters/illustrate/${characterId}`,
                null, // Le corps de la requête PUT est null car l'ID est dans le chemin
                {
                    responseType: 'arraybuffer',
                    headers: {
                        "Content-Type": "application/json",
                    },
                },
            );
        }
    );
};