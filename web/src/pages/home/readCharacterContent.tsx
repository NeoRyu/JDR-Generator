import React from 'react';
import {CharacterFull} from '@/components/model/character.model';
import {ScrollArea} from '@/components/ui/scroll-area';
import {Table, TableBody, TableCell, TableRow} from '@/components/ui/table';
import {Dialog, DialogContent, DialogDescription, DialogTitle, DialogTrigger} from '@/components/ui/dialog';
import {Button} from '@/components/ui/button';
import {Eye} from 'lucide-react';
import {ModalTypes} from "@/pages/home/home.tsx";


interface ReadCharacterContentProps {
    character: CharacterFull;
    modalType: ModalTypes;
    setModalType: React.Dispatch<React.SetStateAction<ModalTypes>>;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: React.Dispatch<React.SetStateAction<CharacterFull | null>>;
}

export function ReadCharacterContent({ character, modalType, setModalType, selectedCharacter, setSelectedCharacter }: ReadCharacterContentProps) {
    if (!character) {
        return <div>Personnage non trouvé.</div>;
    }

    return (
        <Dialog
            open={
                modalType === 'read' &&
                selectedCharacter?.details.id === character.details.id
            }
            onOpenChange={(open) => {
                if (!open) {
                    setModalType(null);
                    setSelectedCharacter(null);
                }
            }}
        >
            <DialogTrigger asChild>
                <Button onClick={() => {
                    setModalType('read');
                    setSelectedCharacter(character);
                }} className="button" type="button" variant="outline" >
                    <Eye />
                </Button>
            </DialogTrigger>

            <DialogContent className={`max-w-[45vw] w-full`}>
                <div className="flex flex-col items-start">
                    <div className="flex items-start">
                        <img
                            className="rounded shadow w-56 h-56 object-contain"
                            src={`data:image/png;base64,${character.illustration?.imageBlob}`}
                            alt={character.details?.image}
                        />
                        <div className="flex-1">
                            <DialogTitle className="character-name">{character.details?.name}</DialogTitle>
                            <DialogDescription className="character-context">
                                <div className="flex character-context">
                                    <div className="w-55 mint">
                                        <p>Archétype &nbsp;</p>
                                        <p>Espèce &nbsp;</p>
                                        <p>Sexe &nbsp;</p>
                                    </div>
                                    <div className="purples">
                                        <p>{character.context?.promptClass}</p>
                                        <p>{character.context?.promptRace}</p>
                                        <p>{character.context?.promptGender}</p>
                                    </div>
                                </div>
                                <div className="flex w-55 character-description">
                                    <p><span className="">Description : </span>
                                        <span className="grays">{character.context?.promptDescription}</span>
                                    </p>
                                </div>
                                <div className="flex w-55 character-description">
                                    <p><span className="">Apparence : </span>
                                        <span className="grays">{character.details?.image}</span>
                                    </p>
                                </div>
                            </DialogDescription>
                        </div>
                    </div>

                    <ScrollArea className="h-64 mt-6 max-w-[45vw] w-full">
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell className="mint">Nom</TableCell>
                                    <TableCell className="flex stretch">{character.details?.name}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Age</TableCell>
                                    <TableCell className="flex stretch">{character.details?.age}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Lieu de naissance</TableCell>
                                    <TableCell className="flex stretch">{character.details?.birthPlace}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Lieu de résidence</TableCell>
                                    <TableCell className="flex stretch">{character.details?.residenceLocation}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Raison de la résidence</TableCell>
                                    <TableCell className="flex stretch">{character.details?.reasonForResidence}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Climat</TableCell>
                                    <TableCell className="flex stretch">{character.details?.climate}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Problèmes communs</TableCell>
                                    <TableCell className="flex stretch">{character.details?.commonProblems}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Routine journalière</TableCell>
                                    <TableCell className="flex stretch">{character.details?.dailyRoutine}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Parents en vie ?</TableCell>
                                    <TableCell className="flex stretch">{character.details?.parentsAlive ? 'Oui' : 'Non'}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Détails sur les parents</TableCell>
                                    <TableCell className="flex stretch">{character.details?.detailsAboutParents}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Sentiments par rapport aux parents</TableCell>
                                    <TableCell className="flex stretch">{character.details?.feelingsAboutParents}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Fraternité ?</TableCell>
                                    <TableCell className="flex stretch">{character.details?.siblings}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Enfance du personnage</TableCell>
                                    <TableCell className="flex stretch">{character.details?.childhoodStory}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Amis d'enfance</TableCell>
                                    <TableCell className="flex stretch">{character.details?.youthFriends}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Animal de compagnie</TableCell>
                                    <TableCell className="flex stretch">{character.details?.pet}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">État matrimonial</TableCell>
                                    <TableCell className="flex stretch">{character.details?.maritalStatus}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Type de conjoint</TableCell>
                                    <TableCell className="flex stretch">{character.details?.typeOfLover}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Histoire conjugale</TableCell>
                                    <TableCell className="flex stretch">{character.details?.conjugalHistory}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Nombre d'enfants</TableCell>
                                    <TableCell className="flex stretch">{character.details?.children}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Education</TableCell>
                                    <TableCell className="flex stretch">{character.details?.education}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Profession</TableCell>
                                    <TableCell>{character.details?.profession}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Raison de la profession</TableCell>
                                    <TableCell className="flex stretch">{character.details?.reasonForProfession}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Préférences de travail</TableCell>
                                    <TableCell className="flex stretch">{character.details?.workPreferences}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Changement souhaité dans le monde</TableCell>
                                    <TableCell className="flex stretch">{character.details?.changeInWorld}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Souhait personnel</TableCell>
                                    <TableCell className="flex stretch">{character.details?.changeInSelf}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Objectif de vie</TableCell>
                                    <TableCell className="flex stretch">{character.details?.goal}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Raison de l'objectif</TableCell>
                                    <TableCell className="flex stretch">{character.details?.reasonForGoal}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Obstacle majeur</TableCell>
                                    <TableCell className="flex stretch">{character.details?.biggestObstacle}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Surmonter les obstacles</TableCell>
                                    <TableCell className="flex stretch">{character.details?.overcomingObstacle}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Plan en cas de succès</TableCell>
                                    <TableCell className="flex stretch">{character.details?.planIfSuccessful}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Plan en cas d'échec</TableCell>
                                    <TableCell className="flex stretch">{character.details?.planIfFailed}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Description de la personne</TableCell>
                                    <TableCell className="flex stretch">{character.details?.selfDescription}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Signe distinctif</TableCell>
                                    <TableCell className="flex stretch">{character.details?.distinctiveTrait}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Description Physique</TableCell>
                                    <TableCell className="flex stretch">{character.details?.physicalDescription}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Préférence vestimentaire</TableCell>
                                    <TableCell className="flex stretch">{character.details?.clothingPreferences}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Phobies</TableCell>
                                    <TableCell className="flex stretch">{character.details?.fears}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Nourriture favorite</TableCell>
                                    <TableCell className="flex stretch">{character.details?.favoriteFood}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Loisirs</TableCell>
                                    <TableCell className="flex stretch">{character.details?.hobbies}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Activités de loisir</TableCell>
                                    <TableCell className="flex stretch">{character.details?.leisureActivities}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Compagnons idéaux</TableCell>
                                    <TableCell className="flex stretch">{character.details?.idealCompany}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Attitude envers le groupe</TableCell>
                                    <TableCell className="flex stretch">{character.details?.attitudeTowardsGroups}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Vision du monde</TableCell>
                                    <TableCell className="flex stretch">{character.details?.attitudeTowardsWorld}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="mint">Attitude envers les autres</TableCell>
                                    <TableCell className="flex stretch">{character.details?.attitudeTowardsPeople}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </ScrollArea>
                </div>
            </DialogContent>
        </Dialog>
    );
}