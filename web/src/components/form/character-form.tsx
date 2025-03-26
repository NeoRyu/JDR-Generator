import React, {forwardRef, useImperativeHandle, useState} from 'react';
import {Textarea} from '@/components/ui/textarea';
import {Input} from '@/components/ui/input';
import {Character} from '@/components/model/character.model';

interface CharacterFormProps {
    initialValues: Character;
    onSubmit: (updatedCharacter: Character) => Promise<void>;
    renderSaveButton?: (handleSubmit: (e: React.FormEvent) => void) => React.ReactNode;
}

interface CharacterFormRef {
    handleSubmit: (e: React.FormEvent) => void;
}

export const CharacterForm = forwardRef<CharacterFormRef, CharacterFormProps>(({ initialValues, onSubmit, renderSaveButton }, ref) => {
    const [character, setCharacter] = useState<Character>(initialValues);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setCharacter({ ...character, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await onSubmit(character);
    };

    useImperativeHandle(ref, () => ({
        handleSubmit,
    }));

    return (
        <div>
            {renderSaveButton && renderSaveButton(handleSubmit)}
            <div style={{ maxHeight: '80vh', overflowY: 'auto', width: '99%', paddingRight: '1rem' }}>
                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <div className="flex gap-4"> {/* Conteneur pour Nom et Âge */}
                        <div className="flex flex-col flex-1">
                            <label htmlFor="name" className="mb-1">Nom</label>
                            <Input
                                type="text"
                                name="name"
                                id="name"
                                placeholder="Nom"
                                value={character.name}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="flex flex-col flex-1">
                            <label htmlFor="age" className="mb-1">Âge</label>
                            <Input
                                type="number"
                                name="age"
                                id="age"
                                placeholder="Âge"
                                value={character.age}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="birthPlace" className="mb-1">Lieu de naissance</label>
                        <Input
                            type="text"
                            name="birthPlace"
                            id="birthPlace"
                            placeholder="Lieu de naissance"
                            value={character.birthPlace}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="residenceLocation" className="mb-1">Lieu de résidence</label>
                        <Input
                            type="text"
                            name="residenceLocation"
                            id="residenceLocation"
                            placeholder="Lieu de résidence"
                            value={character.residenceLocation}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="reasonForResidence" className="mb-1">Raison de la résidence</label>
                        <Textarea
                            name="reasonForResidence"
                            id="reasonForResidence"
                            placeholder="Raison de la résidence"
                            value={character.reasonForResidence}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="climate" className="mb-1">Climat</label>
                        <Input
                            type="text"
                            name="climate"
                            id="climate"
                            placeholder="Climat"
                            value={character.climate}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="commonProblems" className="mb-1">Problèmes communs</label>
                        <Textarea
                            name="commonProblems"
                            id="commonProblems"
                            placeholder="Problèmes communs"
                            value={character.commonProblems}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="dailyRoutine" className="mb-1">Routine journalière</label>
                        <Textarea
                            name="dailyRoutine"
                            id="dailyRoutine"
                            placeholder="Routine journalière"
                            value={character.dailyRoutine}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="parentsAlive" className="mb-1">Parents en vie ?</label>
                        <Input
                            type="text"
                            name="parentsAlive"
                            id="parentsAlive"
                            placeholder="Parents en vie ?"
                            value={character.parentsAlive}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="detailsAboutParents" className="mb-1">Détails sur les parents</label>
                        <Textarea
                            name="detailsAboutParents"
                            id="detailsAboutParents"
                            placeholder="Détails sur les parents"
                            value={character.detailsAboutParents}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="feelingsAboutParents" className="mb-1">Sentiments par rapport aux parents</label>
                        <Textarea
                            name="feelingsAboutParents"
                            id="feelingsAboutParents"
                            placeholder="Sentiments par rapport aux parents"
                            value={character.feelingsAboutParents}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="siblings" className="mb-1">Fraternité ?</label>
                        <Input
                            type="text"
                            name="siblings"
                            id="siblings"
                            placeholder="Fraternité ?"
                            value={character.siblings}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="childhoodStory" className="mb-1">Enfance du personnage</label>
                        <Textarea
                            name="childhoodStory"
                            id="childhoodStory"
                            placeholder="Enfance du personnage"
                            value={character.childhoodStory}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="youthFriends" className="mb-1">Amis d'enfance</label>
                        <Input
                            type="text"
                            name="youthFriends"
                            id="youthFriends"
                            placeholder="Amis d'enfance"
                            value={character.youthFriends}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="pet" className="mb-1">Animal de compagnie</label>
                        <Input
                            type="text"
                            name="pet"
                            id="pet"
                            placeholder="Animal de compagnie"
                            value={character.pet}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="maritalStatus" className="mb-1">État matrimonial</label>
                        <Input
                            type="text"
                            name="maritalStatus"
                            id="maritalStatus"
                            placeholder="État matrimonial"
                            value={character.maritalStatus}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="typeOfLover" className="mb-1">Type de partenaire en amour</label>
                        <Input
                            type="text"
                            name="typeOfLover"
                            id="typeOfLover"
                            placeholder="Type de partenaire en amour"
                            value={character.typeOfLover}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="conjugalHistory" className="mb-1">Histoire conjugale</label>
                        <Textarea
                            name="conjugalHistory"
                            id="conjugalHistory"
                            placeholder="Histoire conjugale"
                            value={character.conjugalHistory}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="children" className="mb-1">A des enfants ?</label>
                        <Input
                            type="number"
                            name="children"
                            id="children"
                            placeholder="A des enfants ?"
                            value={character.children}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="education" className="mb-1">Education</label>
                        <Input
                            type="text"
                            name="education"
                            id="education"
                            placeholder="Education"
                            value={character.education}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="profession" className="mb-1">Profession</label>
                        <Input
                            type="text"
                            name="profession"
                            id="profession"
                            placeholder="Profession"
                            value={character.profession}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="reasonForProfession" className="mb-1">Raison de la profession</label>
                        <Textarea
                            name="reasonForProfession"
                            id="reasonForProfession"
                            placeholder="Raison de la profession"
                            value={character.reasonForProfession}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="workPreferences" className="mb-1">Préférences de travail</label>
                        <Textarea
                            name="workPreferences"
                            id="workPreferences"
                            placeholder="Préférences de travail"
                            value={character.workPreferences}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="changeInWorld" className="mb-1">Changement souhaité dans le monde</label>
                        <Textarea
                            name="changeInWorld"
                            id="changeInWorld"
                            placeholder="Changement souhaité dans le monde"
                            value={character.changeInWorld}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="changeInSelf" className="mb-1">Souhait personnel</label>
                        <Textarea
                            name="changeInSelf"
                            id="changeInSelf"
                            placeholder="Souhait personnel"
                            value={character.changeInSelf}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="goal" className="mb-1">Objectif de vie</label>
                        <Textarea
                            name="goal"
                            id="goal"
                            placeholder="Objectif de vie"
                            value={character.goal}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="reasonForGoal" className="mb-1">Raison de l'objectif</label>
                        <Textarea
                            name="reasonForGoal"
                            id="reasonForGoal"
                            placeholder="Raison de l'objectif"
                            value={character.reasonForGoal}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="biggestObstacle" className="mb-1">Obstacle majeur</label>
                        <Textarea
                            name="biggestObstacle"
                            id="biggestObstacle"
                            placeholder="Obstacle majeur"
                            value={character.biggestObstacle}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="overcomingObstacle" className="mb-1">Surmonter les obstacles</label>
                        <Textarea
                            name="overcomingObstacle"
                            id="overcomingObstacle"
                            placeholder="Surmonter les obstacles"
                            value={character.overcomingObstacle}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="planIfSuccessful" className="mb-1">Plan en cas de succès</label>
                        <Textarea
                            name="planIfSuccessful"
                            id="planIfSuccessful"
                            placeholder="Plan en cas de succès"
                            value={character.planIfSuccessful}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="planIfFailed" className="mb-1">Plan en cas d'échec</label>
                        <Textarea
                            name="planIfFailed"
                            id="planIfFailed"
                            placeholder="Plan en cas d'échec"
                            value={character.planIfFailed}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="selfDescription" className="mb-1">Description de la personne</label>
                        <Textarea
                            name="selfDescription"
                            id="selfDescription"
                            placeholder="Description de la personne"
                            value={character.selfDescription}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="distinctiveTrait" className="mb-1">Signe distinctif</label>
                        <Input
                            type="text"
                            name="distinctiveTrait"
                            id="distinctiveTrait"
                            placeholder="Signe distinctif"
                            value={character.distinctiveTrait}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="physicalDescription" className="mb-1">Description Physique</label>
                        <Textarea
                            name="physicalDescription"
                            id="physicalDescription"
                            placeholder="Description Physique"
                            value={character.physicalDescription}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="clothingPreferences" className="mb-1">Préférence vestimentaire</label>
                        <Textarea
                            name="clothingPreferences"
                            id="clothingPreferences"
                            placeholder="Préférence vestimentaire"
                            value={character.clothingPreferences}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="fears" className="mb-1">Phobies</label>
                        <Textarea
                            name="fears"
                            id="fears"
                            placeholder="Phobies"
                            value={character.fears}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="favoriteFood" className="mb-1">Nourriture favorite</label>
                        <Input
                            type="text"
                            name="favoriteFood"
                            id="favoriteFood"
                            placeholder="Nourriture favorite"
                            value={character.favoriteFood}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="hobbies" className="mb-1">Loisirs</label>
                        <Textarea
                            name="hobbies"
                            id="hobbies"
                            placeholder="Loisirs"
                            value={character.hobbies}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="leisureActivities" className="mb-1">Activités de loisir</label>
                        <Textarea
                            name="leisureActivities"
                            id="leisureActivities"
                            placeholder="Activités de loisir"
                            value={character.leisureActivities}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="idealCompany" className="mb-1">Compagnons idéaux</label>
                        <Textarea
                            name="idealCompany"
                            id="idealCompany"
                            placeholder="Compagnons idéaux"
                            value={character.idealCompany}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="attitudeTowardsGroups" className="mb-1">Attitude envers le groupe</label>
                        <Textarea
                            name="attitudeTowardsGroups"
                            id="attitudeTowardsGroups"
                            placeholder="Attitude envers le groupe"
                            value={character.attitudeTowardsGroups}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="attitudeTowardsWorld" className="mb-1">Vision du monde</label>
                        <Textarea
                            name="attitudeTowardsWorld"
                            id="attitudeTowardsWorld"
                            placeholder="Vision du monde"
                            value={character.attitudeTowardsWorld}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="attitudeTowardsPeople" className="mb-1">Attitude envers les autres</label>
                        <Textarea
                            name="attitudeTowardsPeople"
                            id="attitudeTowardsPeople"
                            placeholder="Attitude envers les autres"
                            value={character.attitudeTowardsPeople}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="image" className="mb-1">Image URL</label>
                        <Input
                            type="text"
                            name="image"
                            id="image"
                            placeholder="Image URL"
                            value={character.image}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="contextId" className="mb-1">Contexte ID</label>
                        <Input
                            type="number"
                            name="contextId"
                            id="contextId"
                            placeholder="Contexte ID"
                            value={character.contextId || 0}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="createdAt" className="mb-1">Date de création</label>
                        <Input
                            type="text"
                            name="createdAt"
                            id="createdAt"
                            placeholder="Date de création"
                            value={character.createdAt || ''}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex flex-col">
                        <label htmlFor="updatedAt" className="mb-1">Date de mise à jour</label>
                        <Input
                            type="text"
                            name="updatedAt"
                            id="updatedAt"
                            placeholder="Date de mise à jour"
                            value={character.updatedAt || ''}
                            onChange={handleChange}
                        />
                    </div>
                </form>
            </div>
        </div>
    );
});
CharacterForm.displayName = 'CharacterForm';
