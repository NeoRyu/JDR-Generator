import axios from 'axios'
import {useQuery} from 'react-query'


export const useListCharacters = () => {
  return useQuery('characters', async () => {
    const { data } = await axios.get('http://localhost:8080/characters',
        {
          headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': 'http://localhost:8080',
            'Access-Control-Allow-Credentials': 'true',
          },
        }
    );
    // TODO : add this get endpoint to java middleware
    console.log('axios.post :: generate', data);
    return data
  })
}
