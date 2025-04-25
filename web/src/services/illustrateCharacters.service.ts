// illustrateCharacter.service.tsx
import {useMutation} from 'react-query';
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080'; // Fallback

// TODO : Permettre aux utilisateurs de générer une nouvelle illustration si l'actuelle ne leur convient pas
export const useIllustrateCharacter = () => {
    return useMutation('illustrate', async (imagePrompt: string) => {
        return await axios.post(`${API_BASE_URL}/characters/illustrate`, imagePrompt, {
            headers: {
                'Content-Type': 'application/json',
                // Les headers CORS sont gérés par le serveur
                // 'Access-Control-Allow-Origin': 'http://localhost:8080',
                // 'Access-Control-Allow-Credentials': 'true',
            },
        });
    });
};