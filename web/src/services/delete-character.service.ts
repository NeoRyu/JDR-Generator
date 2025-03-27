import axios from 'axios'
import {useMutation} from 'react-query';


const deleteCharacterRequest = async (id: number): Promise<void> => {
    try{
        const response = await axios.delete(`/characters/${id}`);
        if (response.status !== 204) {
            throw new Error(`Unexpected status code: ${response.status}`);
        }
    } catch (error : any) {
        if (axios.isAxiosError(error)) {
            console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
            throw new Error(error.message);
        } else {
            console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
            throw new Error("Failed to delete character");
        }
    }
};

export const useDeleteCharacter = () => {
    return useMutation({
        mutationFn: deleteCharacterRequest,
        onSuccess: () => {
            console.log('Personnage supprimé avec succès !');
        },
        onError: (error: Error) => {
            console.error(`Erreur : ${error.message || 'Une erreur inconnue est survenue.'}`);
        },
    });
};
