// createCharacter.service.tsx
import axios from 'axios'
import {useMutation} from 'react-query'


function cleanData(chaine: string | undefined): string | undefined {
    if (!chaine) return chaine;
    return chaine;
}

export const useCreateCharacter = () => {
    return useMutation('characters',
        async (payloadData: {
            promptSystem: string
            promptRace: string
            promptGender: string
            promptClass?: string
            promptDescription?: string
        }) => {
            const cleanedData = {
                ...payloadData,
                promptSystem: cleanData(payloadData.promptSystem),
                promptRace: cleanData(payloadData.promptRace),
                promptGender: cleanData(payloadData.promptGender),
                promptClass: cleanData(payloadData.promptClass),
                promptDescription: cleanData(payloadData.promptDescription),
            };
            return await axios.post('/characters/generate', cleanedData, {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': 'http://localhost:8080',
                    'Access-Control-Allow-Credentials': 'true',
                },
            });
        });
}