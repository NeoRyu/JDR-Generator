import {CharacterContext} from "@/components/model/character-context.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";
import {CharacterIllustration} from "@/components/model/character-illustration.model.tsx";
import {CharacterJsonData} from "@/components/model/character-json.model.tsx";

export interface CharacterFull extends CharacterDetailsModel {
  context: CharacterContext;
  details: CharacterDetailsModel;
  jsonData: CharacterJsonData;
  illustration: CharacterIllustration;
}
