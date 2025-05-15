import {CharacterContext} from "@/components/model/character-context.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";
import {CharacterIllustration} from "@/components/model/character-illustration.model.tsx";
import {CharacterJsonData} from "@/components/model/character-stats.model.tsx";

export interface CharacterFull extends CharacterDetailsModel {
  details: CharacterDetailsModel;
  context: CharacterContext;
  illustration: CharacterIllustration;
  jsonData: CharacterJsonData;
}
