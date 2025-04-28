// updateCharacter.service.tsx
import axios from 'axios';
import {CharacterFull} from '@/components/model/character-full.model.tsx';

function cleanData(value: any): any {
    if (typeof value === 'string') {
        return value;
    }
    if (typeof value === 'object' && value !== null) {
        if (Array.isArray(value)) {
            return value.map(item => cleanData(item));
        } else {
            const echappe = { ...value };
            for (const key in echappe) {
                echappe[key] = cleanData(echappe[key]);
            }
            return echappe;
        }
    }
    return value;
}

function validateData(characterFull: CharacterFull) {
    if (!characterFull || !characterFull.details || !characterFull.details.id) {
        throw new Error('ID de personnage invalide.');
    }
    if (!characterFull.details.name) {
        throw new Error('Le nom du personnage est requis.');
    }
}

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'; // Fallback

export const updateCharacter = () => {
    const updateCharacter = async (characterFull: CharacterFull) => {
        try {
            validateData(characterFull);
            const cleanedData = cleanData(characterFull);
            cleanedData.updatedAt = new Date().toISOString().slice(0, 16);
            const response = await axios.put(`${API_BASE_URL}/characters/details/${characterFull.details.id}`, cleanedData);
            return response.data;
        } catch (error: any) {
            if (error.response) {
                console.error('Erreur du serveur:', error.response.data);
                throw new Error(`Erreur du serveur: ${error.response.status}`);
            } else if (error.request) {
                console.error('Aucune réponse du serveur.');
                throw new Error('Aucune réponse du serveur.');
            } else {
                console.error('Erreur de requête:', error.message);
                throw new Error('Erreur de requête.');
            }
        }
    };
    return { updateCharacter };
};