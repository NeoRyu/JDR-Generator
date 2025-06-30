// getListCharactersFull.service.ts
import {useQuery} from "react-query";
import axios, {AxiosResponse} from "axios";
import {CharacterFull} from "@/components/model/character-full.model.tsx";

export interface CharactersResponse {
  data: CharacterFull[];
}

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://34.155.16.213:80"; // Fallback local : "http://localhost:8080"

export const getListCharactersFull = () => {
  return useQuery<AxiosResponse<CharactersResponse>, Error>(
    "charactersFull",
    async () => {
      return await axios.get<CharactersResponse>(
        `${API_BASE_URL}/characters/full`,
      );
    },
  );
};
