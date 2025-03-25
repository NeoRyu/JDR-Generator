import axios from 'axios'
import {useQuery} from 'react-query'


export const useListCharacters = () => {
  return useQuery('characters',
      async () => {
          return await axios.get('/characters', {});
      });
}
