// deleteCharacter.service.tsx
import axios from 'axios';
import {useMutation} from 'react-query';

interface DeleteCharacterResponse {
    // Définir les propriétés de la réponse si nécessaire
}

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'; // Fallback

const deleteCharacterRequest = async (id: number): Promise<DeleteCharacterResponse> => {
    try {
        const response = await axios.delete(`${API_BASE_URL}/characters/${id}`);
        if (response.status !== 204) {
            throw new Error(`Unexpected status code: ${response.status}`);
        }
        return response.data; // Retournez les données de la réponse si nécessaire
    } catch (error: any) {
        if (axios.isAxiosError(error)) {
            console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
            throw new Error(error.message);
        } else {
            console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
            throw new Error('Failed to delete character');
        }
    }
};

export const useDeleteCharacter = () => {
    return useMutation<DeleteCharacterResponse, Error, number>({
        mutationFn: deleteCharacterRequest,
        onSuccess: () => {
            console.log('Personnage supprimé avec succès !');
        },
        onError: (error: Error) => {
            console.error(`Erreur : ${error.message || 'Une erreur inconnue est survenue.'}`);
        },
    });
};