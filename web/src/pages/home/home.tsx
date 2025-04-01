// home.tsx

import {useState} from 'react';
import {Button} from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from '@/components/ui/select';
import {Table, TableBody, TableHead, TableHeader, TableRow} from '@/components/ui/table';
import {Textarea} from '@/components/ui/textarea';
import {CharacterDetailsModel, CharacterFull} from '@/components/model/character.model';
import {CharacterRow} from '@/pages/home/characterRow';
import {getListCharactersFull} from '@/services/getListCharactersFull.service.ts';
import {useCreateCharacter} from "@/services/createCharacter.service.ts";
import {updateCharacter} from '@/services/updateCharacter.service.ts';


export type ModalTypes = 'read' | 'update' | 'delete' | null;

export function Home() {
  const { data: charactersData, refetch, isLoading: isListLoading } = getListCharactersFull();
  const updateCharacterService = updateCharacter();
  const { mutate, isLoading: isCreateLoading } = useCreateCharacter();
  const [modalType, setModalType] = useState<ModalTypes>(null);
  const [selectedCharacter, setSelectedCharacter] = useState<CharacterFull | null>(null);

  const [isOpen, setIsOpen] = useState(false);

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
            setModalType(null);
          },
        },
    );
  };

  const handleUpdateCharacter = async (updatedCharacterDetails: CharacterDetailsModel) => {
    try {
      if (!selectedCharacter) return;
      const fullModel: CharacterFull = {
        ...updatedCharacterDetails,
        details: updatedCharacterDetails,
        context: selectedCharacter.context,
        illustration: selectedCharacter.illustration,
      };
      await updateCharacterService.updateCharacter(fullModel);
      await refetch();
    } catch (error) {
      console.error('Erreur lors de la mise à jour du personnage :', error);
    }
  };

  // @ts-ignore
  // @ts-ignore
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
                    <TableHead className="table-head" />
                  </TableRow>
                </TableHeader>

                <TableBody>
                  {charactersData?.data?.map((character: CharacterFull) => (
                      <CharacterRow
                          key={character.details?.id}
                          character={character}
                          modalType={modalType}
                          setModalType={setModalType}
                          selectedCharacter={selectedCharacter}
                          setSelectedCharacter={setSelectedCharacter}
                          updateCharacter={handleUpdateCharacter}
                          refetch={refetch}
                      />
                  ))}
                </TableBody>
              </Table>
          )}
        </main>
      </div>
  );
}