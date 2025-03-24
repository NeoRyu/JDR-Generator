import axios from 'axios'
import {useMutation} from 'react-query'


export const useCreateCharacter = () => {
    return useMutation(
        'characters',
        async (payload: {
            promptSystem: string
            promptRace: string
            promptClass?: string
            promptDescription?: string
        }) => {
            const { data } = await axios.post(
                'http://localhost:8080/characters/generate',
                payload,
                {
                    headers: {
                        "Content-Type": "application/json",
                        'Access-Control-Allow-Origin': 'http://localhost:8080',
                        'Access-Control-Allow-Credentials': 'true',
                    },
                }
            );
            // TODO : format json response
            console.log('axios.post :: generate', data);
            return data
        },
    )
}
