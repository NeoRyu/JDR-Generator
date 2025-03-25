import axios, {AxiosRequestConfig} from 'axios'
import {useMutation} from 'react-query'

const apiURL: string = 'http://localhost:8080/characters/generate';
const axiosConfig: AxiosRequestConfig = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': 'http://localhost:8080',
        'Access-Control-Allow-Credentials': 'true',
    },
};

export const useCreateCharacter = () => {
    return useMutation('characters',
        async (payloadData: {
            promptSystem: string
            promptRace: string
            promptGender: string
            promptClass?: string
            promptDescription?: string
        }) => {
            return await axios.post(apiURL, payloadData, axiosConfig);
        });
}
