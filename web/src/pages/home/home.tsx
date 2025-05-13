// home.tsx
import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {Table, TableBody, TableHead, TableHeader, TableRow,} from "@/components/ui/table";
import {Textarea} from "@/components/ui/textarea";
import {CharacterFull} from "@/components/model/character-full.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";
import {CharacterRow} from "@/pages/home/characterRow";
import {getListCharactersFull} from "@/services/getListCharactersFull.service.ts";
import {useCreateCharacter} from "@/services/createCharacter.service.ts";
import {updateCharacter} from "@/services/updateCharacter.service.ts";
import {gameUniverses} from "@/pages/home/listes/gameUniverses.tsx";
import {characterRaces} from "@/pages/home/listes/characterRaces.tsx";
import {characterGenders} from "@/pages/home/listes/characterGenders.tsx";
import {characterClasses} from "@/pages/home/listes/characterClasses.tsx";
import CustomSelect from "@/components/ui/customSelect.tsx";

export type ModalTypes = "read" | "update" | "delete" | null;

export function Home() {
  const {
    data: charactersData,
    refetch,
    isLoading: isListLoading,
  } = getListCharactersFull();
  const [localCharactersData, setLocalCharactersData] = useState<
    CharacterFull[] | undefined
  >(undefined);
  useEffect(() => {
    if (charactersData && Array.isArray(charactersData.data)) {
      setLocalCharactersData(charactersData.data);
    }
  }, [charactersData, localCharactersData]);

  const updateCharacterService = updateCharacter();
  const { mutate, isLoading: isCreateLoading } = useCreateCharacter();
  const [deletingCharacters, setDeletingCharacters] = useState<number[]>([]);
  const [selectedCharacter, setSelectedCharacter] =
    useState<CharacterFull | null>(null);
  const [modalType, setModalType] = useState<ModalTypes>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [promptSystem, setPromptSystem] = useState("");
  const [promptRace, setPromptRace] = useState("");
  const [promptGender, setPromptGender] = useState("");
  const [promptClass, setPromptClass] = useState("");
  const [promptDescription, setPromptDescription] = useState("");

  const handleGenerate = () => {
    if (!promptSystem || !promptGender) {
      return alert("Remplissez les champs");
    }
    mutate(
      {
        promptSystem,
        promptClass,
        promptGender,
        promptRace,
        promptDescription,
      },
      {
        onSuccess: () => {
          refetch();
          setPromptSystem("");
          setPromptRace("");
          setPromptGender("");
          setPromptClass("");
          setPromptDescription("");
          setIsOpen(false);
          setModalType(null);
          setSelectedCharacter(null);
        },
      },
    );
  };

  const handleUpdateCharacter = async (
    updatedCharacterDetails: CharacterDetailsModel,
  ) => {
    try {
      if (!selectedCharacter) return;
      const fullModel: CharacterFull = {
        ...updatedCharacterDetails,
        details: updatedCharacterDetails,
        context: selectedCharacter.context,
        illustration: selectedCharacter.illustration,
        jsonData: selectedCharacter.jsonData,
      };
      await updateCharacterService.updateCharacter(fullModel);
      await refetch();
      setModalType(null);
      setSelectedCharacter(null);
    } catch (error) {
      console.error("Erreur lors de la mise à jour du personnage :", error);
    }
  };

  const handleCharacterDeleted = (characterId: number) => {
    setDeletingCharacters([...deletingCharacters, characterId]);
    if (localCharactersData) {
      const updatedCharacters = localCharactersData.filter(
        (char: CharacterFull) => char.details.id !== characterId,
      );
      setLocalCharactersData([...updatedCharacters]);
      refetch();
    }
  };

  return (
    <div className="h-screen flex flex-col px-4">
      {/* HEADER : JDR.Generator */}
      <header className="h-16 flex items-center sliced-wrapper">
        <div className="sliced-top text-muted-foreground">JDR.Generator</div>
        <div className="sliced-bottom text-muted-foreground" aria-hidden="true">
          JDR.Generator
        </div>
      </header>

      <main className="flex flex-col">
        {/* CREATION DE NOUVEAUX PERSONNAGES */}
        <div className="flex justify-end">
          <Dialog open={isOpen} onOpenChange={setIsOpen}>
            {/* BOUTON DE CREATION DE PERSONNAGE */}
            <DialogTrigger asChild>
              <Button className="button-aura" onClick={() => setIsOpen(true)}>
                Nouveau personnage
              </Button>
            </DialogTrigger>
            {/* FENÊTRE DE SAISIE DU CONTEXTE POUR CREATION */}
            <DialogContent>
              <DialogTitle>Création d'un nouveau personnage</DialogTitle>
              <DialogDescription>
                Remplissez les champs pour générer un nouveau personnage.
              </DialogDescription>
              {/* UNIVERS DE JEU */}
              <CustomSelect
                options={gameUniverses}
                value={promptSystem}
                onChange={setPromptSystem}
                placeholder="Sélectionnez l'univers du jeu ou saisissez une valeur"
              />
              {/* ESPECE DU PERSONNAGE */}
              <CustomSelect
                options={characterRaces}
                value={promptRace}
                onChange={setPromptRace}
                placeholder="Sélectionnez la race du personnage ou saisissez une valeur"
              />
              {/* GENRE DU PERSONNAGE */}
              <CustomSelect
                options={characterGenders}
                value={promptGender}
                onChange={setPromptGender}
                placeholder="Sélectionnez le sexe du personnage ou saisissez une valeur"
              />
              {/* ARCHETYPE DU PERSONNAGE */}
              <CustomSelect
                options={characterClasses}
                value={promptClass}
                onChange={setPromptClass}
                placeholder="Sélectionnez la classe du personnage ou saisissez une valeur"
              />
              {/* DESCRIPTION EXTENSIBLE POUR PERMETTRE UN AFFINAGE LORS DE LA GENERATION */}
              <Textarea
                placeholder="Description"
                value={promptDescription}
                onChange={(event) => setPromptDescription(event.target.value)}
              />
              {/* BOUTON GENERER */}
              <DialogFooter>
                <Button
                  type="button"
                  disabled={isCreateLoading}
                  onClick={handleGenerate}
                >
                  {isCreateLoading ? "Génération..." : "Générer !"}
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>

        {isListLoading ? (
          <div className="loading-spiraleclispe-container">
            <div className="loading-spiraleclispe">
              <span>{/* ANIMATION LOADING */}</span>
            </div>
          </div>
        ) : (
          <Table>
            {/* LISTE HEADER DES PERSONNAGES DEJA CREES */}
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
              {/* LISTE APERCUS DES PERSONNAGES DEJA CREES + BOUTONS */}
              {localCharactersData?.map((character: CharacterFull) => (
                <CharacterRow
                  key={character.details?.id}
                  character={character}
                  modalType={modalType}
                  setModalType={setModalType}
                  selectedCharacter={selectedCharacter}
                  setSelectedCharacter={setSelectedCharacter}
                  updateCharacter={handleUpdateCharacter}
                  refetch={refetch}
                  onCharacterDeleted={handleCharacterDeleted}
                  deletingCharacters={deletingCharacters}
                  setDeletingCharacters={setDeletingCharacters}
                />
              ))}
            </TableBody>
          </Table>
        )}
      </main>
    </div>
  );
}
