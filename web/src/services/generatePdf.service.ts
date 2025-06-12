// src/services/generatePdf.service.ts
import { useMutation, UseMutationResult } from "react-query";
import axios, { AxiosResponse } from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080"; // Fallback .env.local

/**
 * Hook pour générer le PDF d'un personnage.
 * Retourne un ArrayBuffer qui est le contenu binaire du PDF.
 */
export const useGeneratePdf = (): UseMutationResult<
    AxiosResponse<ArrayBuffer>,
    unknown,
    number
> => {
    return useMutation(
        "generatePdf",
        async (characterId: number) => {
            return await axios.get(
                `${API_BASE_URL}/characters/pdf/generate/${characterId}`,
                {
                    responseType: 'arraybuffer',
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/pdf",
                    },
                },
            );
        },
    );
};
