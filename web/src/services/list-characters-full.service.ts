import {useQuery} from 'react-query';
import axios, {AxiosResponse} from 'axios';
import {CharacterFull} from '@/components/model/character.model';

interface CharactersResponse {
    data: CharacterFull[];
}

export const useListCharactersFull = () => {
    return useQuery<AxiosResponse<CharactersResponse>, Error>(
        'charactersFull',
        async () => {
            return await axios.get('/characters/full');
        }
    );
};