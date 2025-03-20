import axios from 'axios'
import { useQuery } from 'react-query'

export const useListCharacters = () => {
  return useQuery('characters', async () => {
    const { data } = await axios.get('http://localhost:3333/characters')

    return data
  })
}
