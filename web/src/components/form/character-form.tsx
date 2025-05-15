// character-form.tsx
import React, {forwardRef, useImperativeHandle, useState} from "react";
import {Textarea} from "@/components/ui/textarea";
import {Input} from "@/components/ui/input";
import {CharacterFull} from "@/components/model/character-full.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";

import dayjs from "dayjs";

// TODO : Etendre la fonctionnalité pour permettre d'éditer aussi les context (pour la vue details et globale),
//  illustration (dont la description et generation)
//  et stats (plus complexe ici car va nécessiter de parcourir un json aux key inconnues et parfois identiques pour éditer, supprimer ou ajouter...)
interface CharacterFormProps {
  initialValues: CharacterFull;
  onSubmit: (updatedCharacter: CharacterDetailsModel) => Promise<void>;
  renderSaveButton?: (
    handleSubmit: (e: React.FormEvent) => void,
  ) => React.ReactNode;
}

export interface CharacterFormRef {
  handleSubmit: (e: React.FormEvent) => void;
}

export const CharacterForm = forwardRef<CharacterFormRef, CharacterFormProps>(
  ({ initialValues, onSubmit, renderSaveButton }, ref) => {
    const [character, setCharacter] = useState<CharacterFull>(initialValues);

    const handleChange = (
      e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
      const { name, value, type } = e.target;
      if (type === "checkbox") {
        const target = e.target as HTMLInputElement;
        setCharacter({
          ...character,
          details: { ...character.details, [name]: target.checked },
        });
      } else if (type === "number") {
        setCharacter({
          ...character,
          details: { ...character.details, [name]: Number(value) },
        });
      } else {
        setCharacter({
          ...character,
          details: { ...character.details, [name]: value },
        });
      }
    };

    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      await onSubmit(character.details);
    };

    useImperativeHandle(ref, () => ({
      handleSubmit,
    }));

    return (
      <div>
        {renderSaveButton && renderSaveButton(handleSubmit)}
        <div
          style={{
            maxHeight: "80vh",
            overflowY: "auto",
            width: "99%",
            paddingRight: "1rem",
          }}
        >
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="name" className="mb-1">
                  Nom
                </label>
                <Input
                  type="text"
                  name="name"
                  id="name"
                  placeholder="Nom"
                  value={character.details?.name ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="age" className="mb-1">
                  Âge
                </label>
                <Input
                  type="number"
                  name="age"
                  id="age"
                  placeholder="Âge"
                  value={character.details?.age ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col">
                <div className="flex flex-col items-center">
                  <label htmlFor="parentsAlive" className="mb-5">
                    Parents en vie ?
                  </label>
                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      name="parentsAlive"
                      id="parentsAlive"
                      checked={
                        typeof character.details?.parentsAlive === "string"
                          ? character.details?.parentsAlive === "true"
                          : character.details?.parentsAlive || false
                      }
                      onChange={handleChange}
                      style={{ transform: "scale(2)" }}
                    />
                    <span className="ml-2">
                      {typeof character.details?.parentsAlive === "string"
                        ? character.details?.parentsAlive === "true"
                          ? "Oui"
                          : "Non"
                        : character.details?.parentsAlive
                          ? "Oui"
                          : "Non"}
                    </span>
                  </div>
                </div>
              </div>
              <div className="flex flex-col flex-1">
                <div className="flex flex-col">
                  <label htmlFor="detailsAboutParents" className="mb-1">
                    Détails sur les parents
                  </label>
                  <Textarea
                    name="detailsAboutParents"
                    id="detailsAboutParents"
                    placeholder="Détails sur les parents"
                    value={character.details?.detailsAboutParents ?? ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="flex flex-col">
                  <label htmlFor="feelingsAboutParents" className="mb-1">
                    Sentiments par rapport aux parents
                  </label>
                  <Input
                    type="text"
                    name="feelingsAboutParents"
                    id="feelingsAboutParents"
                    placeholder="Sentiments par rapport aux parents"
                    value={character.details?.feelingsAboutParents ?? ""}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="siblings" className="mb-1">
                  Frères ou soeurs ?
                </label>
                <Input
                  type="text"
                  name="siblings"
                  id="siblings"
                  placeholder="Fraternité ?"
                  value={character.details?.siblings ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="education" className="mb-1">
                  Education
                </label>
                <Input
                  type="text"
                  name="education"
                  id="education"
                  placeholder="Education"
                  value={character.details?.education ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="childhoodStory" className="mb-1">
                  Enfance du personnage
                </label>
                <Textarea
                  name="childhoodStory"
                  id="childhoodStory"
                  placeholder="Enfance du personnage"
                  value={character.details?.childhoodStory ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="youthFriends" className="mb-1">
                  Amis d'enfance
                </label>
                <Textarea
                  name="youthFriends"
                  id="youthFriends"
                  placeholder="Amis d'enfance"
                  value={character.details?.youthFriends ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="birthPlace" className="mb-1">
                  Lieu de naissance
                </label>
                <Input
                  type="text"
                  name="birthPlace"
                  id="birthPlace"
                  placeholder="Lieu de naissance"
                  value={character.details?.birthPlace ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="climate" className="mb-1">
                  Climat
                </label>
                <Input
                  type="text"
                  name="climate"
                  id="climate"
                  placeholder="Climat"
                  value={character.details?.climate ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="residenceLocation" className="mb-1">
                  Lieu de résidence
                </label>
                <Input
                  type="text"
                  name="residenceLocation"
                  id="residenceLocation"
                  placeholder="Lieu de résidence"
                  value={character.details?.residenceLocation ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="reasonForResidence" className="mb-1">
                  Raison de la résidence
                </label>
                <Input
                  type="text"
                  name="reasonForResidence"
                  id="reasonForResidence"
                  placeholder="Raison de la résidence"
                  value={character.details?.reasonForResidence ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="maritalStatus" className="mb-1">
                  État matrimonial
                </label>
                <Input
                  type="text"
                  name="maritalStatus"
                  id="maritalStatus"
                  placeholder="État matrimonial"
                  value={character.details?.maritalStatus ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="children" className="mb-1">
                  Nombre d'enfants
                </label>
                <Input
                  type="number"
                  name="children"
                  id="children"
                  placeholder="A des enfants ?"
                  value={character.details?.children ?? 0}
                  onChange={handleChange}
                />
              </div>
            </div>
            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="typeOfLover" className="mb-1">
                  Type de partenaire en amour
                </label>
                <Textarea
                  name="typeOfLover"
                  id="typeOfLover"
                  placeholder="Type de partenaire en amour"
                  value={character.details?.typeOfLover ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="conjugalHistory" className="mb-1">
                  Histoire conjugale
                </label>
                <Textarea
                  name="conjugalHistory"
                  id="conjugalHistory"
                  placeholder="Histoire conjugale"
                  value={character.details?.conjugalHistory ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex flex-col flex-1">
              <label htmlFor="pet" className="mb-1">
                Animal de compagnie
              </label>
              <Input
                type="text"
                name="pet"
                id="pet"
                placeholder="Animal de compagnie"
                value={character.details?.pet ?? ""}
                onChange={handleChange}
              />
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="profession" className="mb-1">
                  Profession
                </label>
                <Input
                  type="text"
                  name="profession"
                  id="profession"
                  placeholder="Profession"
                  value={character.details?.profession ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="reasonForProfession" className="mb-1">
                  Raison de la profession
                </label>
                <Input
                  type="text"
                  name="reasonForProfession"
                  id="reasonForProfession"
                  placeholder="Raison de la profession"
                  value={character.details?.reasonForProfession ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="workPreferences" className="mb-1">
                  Préférences de travail
                </label>
                <Textarea
                  name="workPreferences"
                  id="workPreferences"
                  placeholder="Préférences de travail"
                  value={character.details?.workPreferences ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="commonProblems" className="mb-1">
                  Problèmes communs
                </label>
                <Textarea
                  name="commonProblems"
                  id="commonProblems"
                  placeholder="Problèmes communs"
                  value={character.details?.commonProblems ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="dailyRoutine" className="mb-1">
                  Routine journalière
                </label>
                <Textarea
                  name="dailyRoutine"
                  id="dailyRoutine"
                  placeholder="Routine journalière"
                  value={character.details?.dailyRoutine ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="changeInWorld" className="mb-1">
                  Changement souhaité dans le monde
                </label>
                <Textarea
                  name="changeInWorld"
                  id="changeInWorld"
                  placeholder="Changement souhaité dans le monde"
                  value={character.details?.changeInWorld ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="changeInSelf" className="mb-1">
                  Souhait personnel
                </label>
                <Textarea
                  name="changeInSelf"
                  id="changeInSelf"
                  placeholder="Souhait personnel"
                  value={character.details?.changeInSelf ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="goal" className="mb-1">
                  Objectif de vie
                </label>
                <Textarea
                  name="goal"
                  id="goal"
                  placeholder="Objectif de vie"
                  value={character.details?.goal ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="reasonForGoal" className="mb-1">
                  Raison de l'objectif
                </label>
                <Textarea
                  name="reasonForGoal"
                  id="reasonForGoal"
                  placeholder="Raison de l'objectif"
                  value={character.details?.reasonForGoal ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="biggestObstacle" className="mb-1">
                  Obstacle majeur
                </label>
                <Textarea
                  name="biggestObstacle"
                  id="biggestObstacle"
                  placeholder="Obstacle majeur"
                  value={character.details?.biggestObstacle ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="overcomingObstacle" className="mb-1">
                  Surmonter les obstacles
                </label>
                <Textarea
                  name="overcomingObstacle"
                  id="overcomingObstacle"
                  placeholder="Surmonter les obstacles"
                  value={character.details?.overcomingObstacle ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="planIfSuccessful" className="mb-1">
                  Plan en cas de succès
                </label>
                <Textarea
                  name="planIfSuccessful"
                  id="planIfSuccessful"
                  placeholder="Plan en cas de succès"
                  value={character.details?.planIfSuccessful ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="planIfFailed" className="mb-1">
                  Plan en cas d'échec
                </label>
                <Textarea
                  name="planIfFailed"
                  id="planIfFailed"
                  placeholder="Plan en cas d'échec"
                  value={character.details?.planIfFailed ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="selfDescription" className="mb-1">
                  Description de la personne
                </label>
                <Textarea
                  name="selfDescription"
                  id="selfDescription"
                  placeholder="Description de la personne"
                  value={character.details?.selfDescription ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="physicalDescription" className="mb-1">
                  Description Physique
                </label>
                <Textarea
                  name="physicalDescription"
                  id="physicalDescription"
                  placeholder="Description Physique"
                  value={character.details?.physicalDescription ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="distinctiveTrait" className="mb-1">
                  Signe distinctif
                </label>
                <Input
                  type="text"
                  name="distinctiveTrait"
                  id="distinctiveTrait"
                  placeholder="Signe distinctif"
                  value={character.details?.distinctiveTrait ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="favoriteFood" className="mb-1">
                  Nourriture favorite
                </label>
                <Input
                  type="text"
                  name="favoriteFood"
                  id="favoriteFood"
                  placeholder="Nourriture favorite"
                  value={character.details?.favoriteFood ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="fears" className="mb-1">
                  Phobies
                </label>
                <Textarea
                  name="fears"
                  id="fears"
                  placeholder="Phobies"
                  value={character.details?.fears ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="clothingPreferences" className="mb-1">
                  Préférence vestimentaire
                </label>
                <Textarea
                  name="clothingPreferences"
                  id="clothingPreferences"
                  placeholder="Préférence vestimentaire"
                  value={character.details?.clothingPreferences ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="hobbies" className="mb-1">
                  Loisirs
                </label>
                <Textarea
                  name="hobbies"
                  id="hobbies"
                  placeholder="Loisirs"
                  value={character.details?.hobbies ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="leisureActivities" className="mb-1">
                  Activités de loisir
                </label>
                <Textarea
                  name="leisureActivities"
                  id="leisureActivities"
                  placeholder="Activités de loisir"
                  value={character.details?.leisureActivities ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="idealCompany" className="mb-1">
                  Compagnons idéaux
                </label>
                <Textarea
                  name="idealCompany"
                  id="idealCompany"
                  placeholder="Compagnons idéaux"
                  value={character.details?.idealCompany ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="attitudeTowardsGroups" className="mb-1">
                  Attitude envers le groupe
                </label>
                <Textarea
                  name="attitudeTowardsGroups"
                  id="attitudeTowardsGroups"
                  placeholder="Attitude envers le groupe"
                  value={character.details?.attitudeTowardsGroups ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex gap-4">
              <div className="flex flex-col flex-1">
                <label htmlFor="attitudeTowardsWorld" className="mb-1">
                  Vision du monde
                </label>
                <Textarea
                  name="attitudeTowardsWorld"
                  id="attitudeTowardsWorld"
                  placeholder="Vision du monde"
                  value={character.details?.attitudeTowardsWorld ?? ""}
                  onChange={handleChange}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="attitudeTowardsPeople" className="mb-1">
                  Attitude envers les autres
                </label>
                <Textarea
                  name="attitudeTowardsPeople"
                  id="attitudeTowardsPeople"
                  placeholder="Attitude envers les autres"
                  value={character.details?.attitudeTowardsPeople ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="flex flex-col flex-1">
              <label htmlFor="image" className="mb-1">
                Description de l'illustration
              </label>
              <Input
                type="text"
                name="image"
                id="image"
                placeholder="Image URL"
                value={character.details?.image ?? ""}
                onChange={handleChange}
              />
            </div>

            <div className="flex gap-4 disabled">
              <div className="flex flex-col flex-1">
                <label htmlFor="createdAt" className="mb-1">
                  Date de création
                </label>
                <Input
                  type="text"
                  name="createdAt"
                  id="createdAt"
                  placeholder="Date de création"
                  value={
                    dayjs(character.details.createdAt).format("DD/MM/YYYY") ||
                    ""
                  }
                  onChange={handleChange}
                  readOnly={true}
                />
              </div>
              <div className="flex flex-col flex-1">
                <label htmlFor="updatedAt" className="mb-1">
                  Date de mise à jour (si enregistrement)
                </label>
                <Input
                  type="text"
                  name="updatedAt"
                  id="updatedAt"
                  placeholder="Date de mise à jour"
                  value={dayjs(new Date()).format("DD/MM/YYYY")}
                  onChange={handleChange}
                  readOnly={true}
                />
              </div>
            </div>
          </form>
        </div>
      </div>
    );
  },
);

CharacterForm.displayName = "CharacterForm";
