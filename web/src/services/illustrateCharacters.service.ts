// illustrateCharacter.service.tsx
import {useMutation, UseMutationResult} from "react-query";
import axios, {AxiosResponse} from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080"; // Fallback

interface RegenerateIllustrationPayload {
    id: number;
    drawStyle: string;
}

// Permettre aux utilisateurs de générer une nouvelle illustration si l'actuelle ne leur convient pas
// Hook pour la régénération d'illustration (PUT)
export const useRegenerateIllustration = (): UseMutationResult<AxiosResponse<ArrayBuffer>, unknown, RegenerateIllustrationPayload, unknown> => {
    return useMutation(
        "regenerateIllustration",
        async (payload: RegenerateIllustrationPayload) => {
            return await axios.put(
                `${API_BASE_URL}/characters/illustrate`,
                payload,
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

