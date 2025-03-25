import axios, {AxiosRequestConfig} from 'axios'
import {useQuery} from 'react-query'

const apiURL: string = 'http://localhost:8080/characters';
const axiosConfig: AxiosRequestConfig = {
};

export const useListCharacters = () => {
  return useQuery('characters',
      async () => {
          return await axios.get(apiURL, axiosConfig);
      });
}
