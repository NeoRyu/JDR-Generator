import axios from 'axios'
import {useMutation} from 'react-query'

export const useCreateCharacter = () => {
    return useMutation('characters',
        async (payloadData: {
            promptSystem: string
            promptRace: string
            promptGender: string
            promptClass?: string
            promptDescription?: string
        }) => {
            return await axios.post('/characters/generate', payloadData, {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': 'http://localhost:8080',
                    'Access-Control-Allow-Credentials': 'true',
                },
            });
        });
}