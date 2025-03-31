import axios from 'axios';
import {CharacterFull} from '@/components/model/character.model';

export const updateCharacter = () => {
    const updateCharacter = async (characterFull: CharacterFull) => {
        try {
            characterFull.updatedAt = new Date().toISOString().slice(0, 16);
            const response = await axios.put(`/characters/details/${characterFull.details.id}`, characterFull);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour du personnage : ', error);
            throw error;
        }
    };
    return { updateCharacter };
};