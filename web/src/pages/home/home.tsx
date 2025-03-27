import {Button} from '@/components/ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import {ScrollArea} from '@/components/ui/scroll-area';
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from '@/components/ui/select';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow,} from '@/components/ui/table';
import {Textarea} from '@/components/ui/textarea';
import dayjs from 'dayjs';
import {Eye, Pen, X} from 'lucide-react';
import {useRef, useState} from 'react';
import {Character, CharacterFull} from '@/components/model/character.model';
import {CharacterForm} from '@/components/form/character-form';
import {useTheme} from '@/components/theme-provider';
import {useCreateCharacter} from '@/services/create-character.service';
import {useUpdateCharacter} from '@/services/update-character.service';
// @ts-ignore
import {useListCharactersFull} from "@/services/list-characters-full.service.ts";


export function Home() {
  const { data, refetch, isLoading: isListLoading } = useListCharactersFull();
  const { mutate, isLoading: isCreateLoading } = useCreateCharacter();
  const { updateCharacter } = useUpdateCharacter();

  const [isOpen, setIsOpen] = useState(false);
  const [selectedCharacter, setSelectedCharacter] = useState<CharacterFull | null>(null);
  const [modalType, setModalType] = useState<'details' | 'edit' | null>(null);

  const [promptSystem, setPromptSystem] = useState('');
  const [promptRace, setPromptRace] = useState('');
  const [promptGender, setPromptGender] = useState('');
  const [promptClass, setPromptClass] = useState('');
  const [promptDescription, setPromptDescription] = useState('');

  const handleGenerate = () => {
    if (!promptSystem || !promptGender) {
      return alert('Remplissez les champs');
    }

    mutate(
        { promptSystem, promptClass, promptGender, promptRace, promptDescription },
        {
          onSuccess: () => {
            refetch();

            setPromptSystem('');
            setPromptRace('');
            setPromptGender('');
            setPromptClass('');
            setPromptDescription('');

            setIsOpen(false);
          },
        },
    );
  };

  const handleUpdateCharacter = async (updatedCharacter: Character) => {
    try {
      await updateCharacter(updatedCharacter);
      await refetch();
      setModalType(null);
      setSelectedCharacter(null);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde du personnage :', error);
    }
  };

  const { theme } = useTheme();

  const characterFormRef = useRef<CharacterFormRef>(null);

  return (
      <div className="h-screen flex flex-col px-4">
        <header className="h-16 flex items-center sliced-wrapper">
          <div className="sliced-top text-muted-foreground">JDR.Generator</div>
          <div className="sliced-bottom text-muted-foreground" aria-hidden="true">JDR.Generator</div>
        </header>

        <main className="flex flex-col">
          <div className="flex justify-end">
            <Dialog open={isOpen} onOpenChange={setIsOpen}>
              <DialogTrigger asChild>
                <Button className="button-aura" onClick={() => setIsOpen(true)}>Nouveau personnage</Button>
              </DialogTrigger>

              <DialogContent>
                <DialogClose asChild>
                  <Button variant="ghost" className="absolute right-4 top-4">
                    <X className="h-4 w-4" />
                    <span className="sr-only">Fermer</span>
                  </Button>
                </DialogClose>
                <DialogTitle>Création d'un nouveau personnage</DialogTitle>
                <DialogDescription>
                  Remplissez les champs pour générer un nouveau personnage.
                </DialogDescription>

                <Select value={promptSystem} onValueChange={setPromptSystem}>
                  <SelectTrigger>
                    <SelectValue placeholder="Sélectionnez l'univers du jeu" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="undefined">Univers générique</SelectItem>
                    <SelectItem value="Aria">Aria</SelectItem>
                    <SelectItem value="Ars Magica">Ars Magica</SelectItem>
                    <SelectItem value="Brigandyne">Brigandyne</SelectItem>
                    <SelectItem value="Chill">Chill</SelectItem>
                    <SelectItem value="COPS">COPS</SelectItem>
                    <SelectItem value="Cyberpunk RED">Cyberpunk RED</SelectItem>
                    <SelectItem value="Call of Cthulhu">L'Appel de Cthulhu</SelectItem>
                    <SelectItem value="Das Schwarze Auge">L'Œil noir</SelectItem>
                    <SelectItem value="Dune">Dune</SelectItem>
                    <SelectItem value="Dungeons & Dragons">Donjons & Dragons</SelectItem>
                    <SelectItem value="Legends of the five Rings">La Légende des Cinq Anneaux</SelectItem>
                    <SelectItem value="Lone Wolf">Loup Solitaire</SelectItem>
                    <SelectItem value="Maléfices">Maléfices</SelectItem>
                    <SelectItem value="Midnight">Midnight</SelectItem>
                    <SelectItem value="Nightprowler">Nightprowler</SelectItem>
                    <SelectItem value="Pathfinder">Pathfinder</SelectItem>
                    <SelectItem value="Rêve de Dragon">Rêve de Dragon</SelectItem>
                    <SelectItem value="Shadowrun">Shadowrun</SelectItem>
                    <SelectItem value="Shaan">Shaan</SelectItem>
                    <SelectItem value="Star Wars">Star Wars</SelectItem>
                    <SelectItem value="Vermine">Vermine</SelectItem>
                    <SelectItem value="Vampire The Masquerade">Vampire : La Mascarade</SelectItem>
                    <SelectItem value="Warhammer Fantasy Roleplay">Warhammer Fantasy Roleplay</SelectItem>
                    <SelectItem value="Warhammer 40K">Warhammer 40K</SelectItem>
                    <SelectItem value="Würm">Würm</SelectItem>
                  </SelectContent>
                </Select>

                <Select value={promptRace} onValueChange={setPromptRace}>
                  <SelectTrigger>
                    <SelectValue placeholder="Sélectionnez la race du personnage" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Human">Humain</SelectItem>
                    <SelectItem value="Elf">Elfe</SelectItem>
                    <SelectItem value="Dwarf">Nain</SelectItem>
                    <SelectItem value="Halfling">Halfling</SelectItem>
                    <SelectItem value="Orc">Orc</SelectItem>
                    <SelectItem value="Gnome">Gnome</SelectItem>
                    <SelectItem value="Tiefling">Tiefling</SelectItem>
                    <SelectItem value="Dragonborn">Draconien</SelectItem>
                    <SelectItem value="Half-Elf">Demi-Elfe</SelectItem>
                    <SelectItem value="Half-Orc">Demi-Orc</SelectItem>
                    <SelectItem value="Tabaxi">Tabaxi</SelectItem>
                    <SelectItem value="Firbolg">Firbolg</SelectItem>
                    <SelectItem value="Aarakocra">Aarakocra</SelectItem>
                    <SelectItem value="Kenku">Kenku</SelectItem>
                    <SelectItem value="Goliath">Goliath</SelectItem>
                    <SelectItem value="Aasimar">Aasimar</SelectItem>
                  </SelectContent>
                </Select>

                <Select value={promptGender} onValueChange={setPromptGender}>
                  <SelectTrigger>
                    <SelectValue placeholder="Sélectionnez le sexe du personnage" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Male">Masculin</SelectItem>
                    <SelectItem value="Female">Féminin</SelectItem>
                    <SelectItem value="Non-binary">Autre</SelectItem>
                  </SelectContent>
                </Select>

                <Select value={promptClass} onValueChange={setPromptClass}>
                  <SelectTrigger>
                    <SelectValue placeholder="Sélectionnez la classe du personnage" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Warrior">Guerrier</SelectItem>
                    <SelectItem value="Wizard">Mage</SelectItem>
                    <SelectItem value="Rogue">Voleur</SelectItem>
                    <SelectItem value="Cleric">Clerc</SelectItem>
                    <SelectItem value="Paladin">Paladin</SelectItem>
                    <SelectItem value="Bard">Barde</SelectItem>
                    <SelectItem value="Druid">Druide</SelectItem>
                    <SelectItem value="Sorcerer">Sorcier</SelectItem>
                    <SelectItem value="Monk">Moine</SelectItem>
                    <SelectItem value="Ranger">Ranger</SelectItem>
                    <SelectItem value="Barbarian">Barbare</SelectItem>
                    <SelectItem value="Warlock">Magicien</SelectItem>
                    <SelectItem value="Fighter">Combattant</SelectItem>
                    <SelectItem value="Necromancer">Nécromant</SelectItem>
                    <SelectItem value="Alchemist">Alchimiste</SelectItem>
                    <SelectItem value="Summoner">Invocateur</SelectItem>
                  </SelectContent>
                </Select>

                <Textarea
                    placeholder="Description"
                    value={promptDescription}
                    onChange={(event) => setPromptDescription(event.target.value)}
                />

                <DialogFooter>
                  <Button
                      type="button"
                      disabled={isCreateLoading}
                      onClick={handleGenerate}
                  >
                    {isCreateLoading ? 'Génération...' : 'Générer !'}
                  </Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>

          {isListLoading ? (
              <div className="loading-spiraleclispe-container">
                <div className="loading-spiraleclispe">
                  <span>{/* <span><span><div className="loading-spiraleclispe-container"><span><span><span><span></span></span></span></span></div></span></span> */}</span>
                </div>
              </div>
          ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="table-head">Date de création</TableHead>
                    <TableHead className="table-head">Genre</TableHead>
                    <TableHead className="table-head">Nom du personnage</TableHead>
                    <TableHead className="table-head">Age</TableHead>
                    <TableHead className="table-head">Espèce</TableHead>
                    <TableHead className="table-head">Profession</TableHead>
                    <TableHead className="table-head">Archétype</TableHead>
                    <TableHead className="table-head">Objectifs</TableHead>
                    <TableHead className="table-head">Univers</TableHead>
                    <TableHead className="table-head"/>
                  </TableRow>
                </TableHeader>

                <TableBody>
                  {data && Array.isArray(data?.data) ? data.data.map((character: CharacterFull) => (
                      <TableRow key={character.details?.id}>
                        <TableCell>{dayjs(character.details.createdAt).format('DD/MM/YYYY')}</TableCell>
                        <TableCell>{character.context?.promptGender == 'Male' ? '♂' : (character.context?.promptGender == 'Female' ? '♀' : '⚥')}</TableCell>
                        <TableCell>{character.details?.name}</TableCell>
                        <TableCell>{character.details?.age}</TableCell>
                        <TableCell>{character.context?.promptRace}</TableCell>
                        <TableCell>{character.details?.profession}</TableCell>
                        <TableCell>{character.context?.promptClass}</TableCell>
                        <TableCell>{character.details?.goal}</TableCell>
                        <TableCell>{character.context?.promptSystem}</TableCell>
                        <TableCell>
                          <div className="flex gap-2">
                            <Dialog
                                open={
                                    modalType === 'details' &&
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
                                  setModalType('details');
                                  setSelectedCharacter(character);
                                }} className="button" type="button" variant="outline" >
                                  <Eye />
                                </Button>
                              </DialogTrigger>

                              <DialogContent className={`max-w-[45vw] w-full`}>
                                <div className="flex flex-col items-start"> {/* Disposition verticale */}
                                  <div className="flex items-start"> {/* Disposition horizontale pour le titre et l'image */}
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
                                          <TableCell className="mint">
                                            Nom
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.name}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Age
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.age}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Lieu de naissance
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.birthPlace}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Lieu de résidence
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.residenceLocation}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Raison de la résidence
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.reasonForResidence}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Climat
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.climate}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Problèmes communs
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.commonProblems}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Routine journalière
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.dailyRoutine}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Parents en vie ?
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.parentsAlive ? 'Oui' : 'Non'}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Détails sur les parents
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.detailsAboutParents}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Sentiments par rapport aux parents
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.feelingsAboutParents}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Fraternité ?
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.siblings}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Enfance du personnage
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.childhoodStory}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Amis d'enfance
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.youthFriends}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Animal de compagnie
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.pet}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            État matrimonial
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.maritalStatus}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Type de conjoint
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.typeOfLover}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Histoire conjugale
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.conjugalHistory}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Nombre d'enfants
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.children}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Education
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.education}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Profession
                                          </TableCell>
                                          <TableCell>
                                            {character.details?.profession}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Raison de la profession
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.reasonForProfession}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Préférences de travail
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.workPreferences}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Changement souhaité dans le monde
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.changeInWorld}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Souhait personnel
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.changeInSelf}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Objectif de vie
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.goal}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Raison de l'objectif
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.reasonForGoal}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Obstacle majeur
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.biggestObstacle}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Surmonter les obstacles
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.overcomingObstacle}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Plan en cas de succès
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.planIfSuccessful}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Plan en cas d'échec
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.planIfFailed}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Description de la personne
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.selfDescription}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Signe distinctif
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.distinctiveTrait}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Description Physique
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.physicalDescription}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Préférence vestimentaire
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.clothingPreferences}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Phobies
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.fears}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Nourriture favorite
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.favoriteFood}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Loisirs
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.hobbies}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Activités de loisir
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.leisureActivities}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Compagnons idéaux
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.idealCompany}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Attitude envers le groupe
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.attitudeTowardsGroups}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Vision du monde
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.attitudeTowardsWorld}
                                          </TableCell>
                                        </TableRow>

                                        <TableRow>
                                          <TableCell className="mint">
                                            Attitude envers les autres
                                          </TableCell>
                                          <TableCell className="flex stretch">
                                            {character.details?.attitudeTowardsPeople}
                                          </TableCell>
                                        </TableRow>

                                      </TableBody>
                                    </Table>
                                  </ScrollArea>
                                </div>

                              </DialogContent>
                            </Dialog>

                            <Dialog
                                open={modalType === 'edit' &&
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
                                  setModalType('edit');
                                  setSelectedCharacter(character);
                                }} className="button" type="button" variant="outline" >
                                  <Pen />
                                </Button>
                              </DialogTrigger>

                              <DialogContent className={`max-w-[90vw] w-full ${theme === 'dark' ? 'light' : 'dark'}`}>
                                <div className="flex justify-between items-start">
                                  <div style={{ maxHeight: '80vh', overflowY: 'auto', width: '80%', paddingRight: '1rem' }}>
                                    <DialogTitle>EDITION DU PERSONNAGE</DialogTitle>
                                    <DialogDescription>
                                      Modifiez les détails du personnage.
                                    </DialogDescription>
                                  </div>
                                  <div className="flex justify-end">
                                    {selectedCharacter && (
                                        <div style={{ textAlign: 'right', paddingRight: '1rem' }}>
                                          {/* Rendre le bouton directement ici */}
                                          <Button
                                              type="button"
                                              onClick={(e) => {
                                                if (selectedCharacter) {
                                                  characterFormRef.current?.handleSubmit(e);
                                                }
                                              }}
                                              className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded shadow-md transition duration-300 ease-in-out transform hover:scale-95"
                                          >
                                            Sauvegarder [ID={selectedCharacter?.id}]
                                          </Button>
                                        </div>
                                    )}
                                  </div>
                                </div>
                                {/* Rendre le formulaire en dehors de la colonne de droite */}
                                {selectedCharacter && (
                                    <CharacterForm
                                        ref={characterFormRef}
                                        initialValues={selectedCharacter?.details}
                                        onSubmit={handleUpdateCharacter}
                                        renderSaveButton={() => null} // Ne pas rendre le bouton ici, il est déjà rendu dans l'en-tête
                                    />
                                )}
                              </DialogContent>

                            </Dialog>
                          </div>
                        </TableCell>
                      </TableRow>
                  )) : null}
                </TableBody>

              </Table>
          )}
        </main>
      </div>
  );
}