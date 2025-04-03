// illustrateCharacter.service.tsx
import {useMutation} from 'react-query';
import axios from 'axios';


// TODO : Permettre aux utilisateurs de générer une nouvelle illustration si l'actuelle ne leur convient pas
export const useIllustrateCharacter = () => {
    return useMutation('illustrate', async (imagePrompt: string) => {
        return await axios.post('/characters/illustrate', imagePrompt, {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': 'http://localhost:8080',
                'Access-Control-Allow-Credentials': 'true',
            },
        });
    });
};