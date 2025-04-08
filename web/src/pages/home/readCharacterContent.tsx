// readCharacterContent.tsx
import {Dispatch, SetStateAction, useState} from 'react';
import {CharacterFull} from '@/components/model/character-full.model.tsx';
import {ScrollArea} from '@/components/ui/scroll-area';
import {Table, TableBody, TableCell, TableRow} from '@/components/ui/table';
import {Dialog, DialogContent, DialogDescription, DialogTitle, DialogTrigger} from '@/components/ui/dialog';
import {ModalTypes} from "@/pages/home/home.tsx";
import {useTheme} from "@/components/theme-provider.tsx";
import {Tabs, TabsContent, TabsList, TabsTrigger} from '@radix-ui/react-tabs';
import {Eye} from 'lucide-react';
import {Button} from '@/components/ui/button';


interface ReadCharacterContentProps {
    character: CharacterFull;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: Dispatch<SetStateAction<CharacterFull | null>>;
    modalType: ModalTypes;
    setModalType: Dispatch<SetStateAction<ModalTypes>>;
    handleReadCharacter: (character: CharacterFull) => void;
}

const renderJsonData = (jsonData: any, level = 0): JSX.Element[] => {
    if (!jsonData || typeof jsonData !== 'object') return [];

    let keyIndex = 0; // Initialize a unique key index

    return Object.entries(jsonData).flatMap(([key, value]) => {
        const bullet = "➠".repeat(level);

        if (Array.isArray(value)) {
            return [
                <TableRow key={`${key}-category-${level}-${keyIndex++}`}>
                    <TableCell className={`purples category-${level + 1}`}>{bullet} {key.charAt(0).toUpperCase() + key.slice(1)}</TableCell>
                    <TableCell className="flex stretch"></TableCell>
                </TableRow>,
                ...value.flatMap((item, index) => {
                    if (typeof item === 'object' && item !== null) {
                        return Object.entries(item).map(([itemKey, itemValue]) => (
                            <TableRow key={`${key}-${index}-${itemKey}-${level}-${keyIndex++}`}>
                                <TableCell className="mint">{bullet} {itemKey}</TableCell>
                                <TableCell className="flex stretch">{String(itemValue)}</TableCell>
                            </TableRow>
                        ));
                    } else {
                        return (
                            <TableRow key={`${key}-${index}-${level}-${keyIndex++}`}>
                                <TableCell className="mint">{bullet} {index + 1}</TableCell>
                                <TableCell className="flex stretch">{String(item)}</TableCell>
                            </TableRow>
                        );
                    }
                }),
            ];
        } else if (typeof value === 'object' && value !== null) {
            return [
                <TableRow key={`${key}-category-${level}-${keyIndex++}`}>
                    <TableCell className={`purples category-${level + 1}`}>{bullet} {key.charAt(0).toUpperCase() + key.slice(1)}</TableCell>
                    <TableCell className="flex stretch"></TableCell>
                </TableRow>,
                ...renderJsonData(value, level + 1),
            ];
        } else {
            return (
                <TableRow key={`${key}-${level}-${keyIndex++}`}>
                    <TableCell className="mint">{bullet} {key}</TableCell>
                    <TableCell className="flex stretch">{String(value)}</TableCell>
                </TableRow>
            );
        }
    });
};

export function ReadCharacterContent({ character, modalType, setModalType, selectedCharacter, setSelectedCharacter }: ReadCharacterContentProps) {
    const { theme } = useTheme();
    const [activeTab, setActiveTab] = useState("details");

    if (!character || !character.details) {
        return <div>Personnage non trouvé.</div>;
    }

    let parsedJson = null;
    try {
        parsedJson = character.jsonData && character.jsonData.jsonData && typeof character.jsonData.jsonData === 'string'
            ? JSON.parse(character.jsonData.jsonData)
            : null;
        if (parsedJson) console.log("Parsed JSON:", parsedJson);
    } catch (error) {
        console.error("Erreur lors de l'analyse du JSON :", error);
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
                <Button
                    onClick={() => {
                        setModalType('read');
                        setSelectedCharacter(character);
                    }}
                    className="button"
                    type="button"
                    variant="outline"
                >
                    <Eye />
                </Button>
            </DialogTrigger>
            <DialogContent className={`max-w-[90vw] w-full ${theme === 'light' ? 'dark' : 'dark'}`} style={{ height: '90vh', paddingRight: '1rem' }}>
                <div className="flex flex-col items-stretch">
                    <div className="flex items-start">
                        {character.illustration && character.illustration.imageBlob ? (
                            <img
                                className="rounded shadow w-64 h-64 object-contain"
                                src={`data:image/png;base64,${character.illustration.imageBlob}`}
                                alt={character.details?.image || "Illustration du personnage"}
                            />
                        ) : (
                            <div className="rounded shadow w-64 h-64 no-img">
                                <span className="text-gray-500">
                                    {character.details?.image ? `Illustration non disponible : ${character.details.image}` : "Aucune illustration disponible"}
                                </span>
                            </div>
                        )}
                        <div className="flex-1">
                            <DialogTitle className="character-name">
                                {character.context?.promptGender == 'Male' ? '♂' : (character.context?.promptGender == 'Female' ? '♀' : '⚥')}&nbsp;
                                {character.details?.name}
                            </DialogTitle>
                            <DialogDescription className="character-context">
                                <div className="flex character-context">
                                    <div className="w-55 mint">
                                        <p>Système de jeu &nbsp;</p>
                                        <p>Archétype &nbsp;</p>
                                        <p>Espèce &nbsp;</p>
                                    </div>
                                    <div className="purples">
                                        <p>{character.context?.promptSystem || "Non défini"}</p>
                                        <p>{character.context?.promptClass || "Non défini"}</p>
                                        <p>{character.context?.promptRace || "Non défini"}</p>
                                    </div>
                                </div>
                                <div className="flex w-55 character-description">
                                    <p><span className="">Description : </span>
                                        <span className="grays">{character.context?.promptDescription || "Non défini"}</span>
                                    </p>
                                </div>
                                <div className="flex w-55 character-description">
                                    <p><span className="">Apparence : </span>
                                        <span className="grays">{character.details?.image || "Non défini"}</span>
                                    </p>
                                </div>
                            </DialogDescription>
                        </div>
                    </div>

                    <Tabs value={activeTab} onValueChange={setActiveTab}>
                        <TabsList className="flex justify-evenly">
                            <TabsTrigger value="details" className={`tab-trigger-style ${activeTab === 'details' ? 'active' : 'mint'} `}>
                                <span className={activeTab === 'details' ? 'tab-trigger-text-active' : 'tab-trigger-text-inactive'}>
                                    Détails
                                </span>
                            </TabsTrigger>
                            <TabsTrigger value="stats" className={`tab-trigger-style ${activeTab === 'stats' ? 'active' : 'mint'}`}>
                                <span className={activeTab === 'stats' ? 'tab-trigger-text-active' : 'tab-trigger-text-inactive'}>
                                    Stats, Compétences & Équipements
                                </span>
                            </TabsTrigger>
                        </TabsList>
                        <TabsContent value="details" className="flex">
                            <ScrollArea className="mt-6 max-w-[90vw] w-full" style={{ height: '50vh', paddingRight: '1rem' }}>
                                <Table className="flex-1">
                                    <TableBody>
                                        {/*<TableRow>
                                            <TableCell className="mint">Nom</TableCell>
                                            <TableCell className="flex stretch">{character.details?.name}</TableCell>
                                        </TableRow>*/}
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
                        </TabsContent>
                        <TabsContent value="stats" className="flex">
                            <ScrollArea className="mt-6 max-w-[90vw] w-full" style={{ height: '50vh', paddingRight: '1rem' }}>
                                <Table className="flex-1" style={{ minWidth: '100%' }}>
                                    <TableBody>
                                        {parsedJson && renderJsonData(parsedJson)}
                                    </TableBody>
                                </Table>
                            </ScrollArea>
                        </TabsContent>
                    </Tabs>
                </div>
            </DialogContent>
        </Dialog>
    );
}