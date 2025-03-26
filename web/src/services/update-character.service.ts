import axios from 'axios';
import {Character} from '@/components/model/character.model';


export const useUpdateCharacter = () => {
    const updateCharacter = async (character: Character) => {
        try {
            const response = await axios.put(`/characters/details/${character.id}`, character);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour du personnage : ', error);
            throw error; // Propagation de l'erreur pour une gestion ultérieure
        }
    };
    return { updateCharacter };
};