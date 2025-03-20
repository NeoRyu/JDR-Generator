import axios from 'axios'
import { useMutation } from 'react-query'

export const useCreateCharacter = () => {
  return useMutation(
    'characters',
    async (payload: {
      gameSystem: string
      race: string
      class: string
      description?: string
    }) => {
      const { data } = await axios.post(
        'http://localhost:3333/characters',
        payload,
      )

      return data
    },
  )
}
