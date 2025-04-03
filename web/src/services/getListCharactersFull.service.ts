// getListCharactersFull.service.ts
import {useQuery} from 'react-query';
import axios, {AxiosResponse} from 'axios';
import {CharacterFull} from '@/components/model/character-full.model.tsx';


export interface CharactersResponse {
    data: CharacterFull[];
}

export const getListCharactersFull = () => {
    return useQuery<AxiosResponse<CharactersResponse>, Error>(
        'charactersFull',
        async () => {
            return await axios.get<CharactersResponse>('/characters/full'); // Correction ici
        }
    );
};