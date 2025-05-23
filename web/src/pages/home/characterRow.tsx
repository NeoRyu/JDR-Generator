// characterRow.tsx
import {TableCell, TableRow} from "@/components/ui/table";
import dayjs from "dayjs";
import {CharacterFull} from "@/components/model/character-full.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";
import {ReadCharacterContent} from "@/pages/home/readCharacterContent.tsx";
import {UpdateCharacterDialog} from "@/pages/home/updateCharacterContent.tsx";
import {DeleteCharacterContent} from "@/pages/home/deleteCharacterContent.tsx";
import {ModalTypes} from "@/pages/home/home.tsx";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {RegenerateIllustrationButton} from "@/pages/home/regenerateIllustrationButton.tsx";

interface CharacterRowProps {
  character: CharacterFull;
  modalType: ModalTypes;
  setModalType: Dispatch<SetStateAction<ModalTypes>>;
  selectedCharacter: CharacterFull | null;
  setSelectedCharacter: Dispatch<SetStateAction<CharacterFull | null>>;
  updateCharacter: (updatedCharacter: CharacterDetailsModel) => Promise<void>;
  refetch: () => void;
  onCharacterDeleted: (characterId: number) => void;
  deletingCharacters: number[];
  setDeletingCharacters: Dispatch<SetStateAction<number[]>>;
}

export function CharacterRow({
  character,
  modalType,
  setModalType,
  selectedCharacter,
  setSelectedCharacter,
  updateCharacter,
  refetch,
  onCharacterDeleted,
  deletingCharacters,
  setDeletingCharacters,
}: CharacterRowProps) {

  const handleReadCharacter = (character: CharacterFull) => {
    setSelectedCharacter(character);
    setModalType("read");
  };

  const [localImageBlob, setLocalImageBlob] = useState(character.illustration?.imageBlob || null);
  useEffect(() => {
    setLocalImageBlob(character.illustration?.imageBlob || null);
  }, [character.illustration?.imageBlob]);

  const handleUpdateCharacter = async (
    updatedCharacter: CharacterDetailsModel,
  ) => {
    try {
      await updateCharacter(updatedCharacter);
      void refetch();
      setModalType(null);
      setSelectedCharacter(null);
    } catch (error) {
      console.error("Erreur lors de la sauvegarde du personnage :", error);
    }
  };

  const handleUpdateIllustration = () => {
    void refetch();
  }

  const isDeleting = deletingCharacters.includes(character.details.id);

  return (
    <TableRow
      key={character.details.id}
      style={{
        backgroundColor: isDeleting ? "rgba(255, 0, 0, 0.1)" : "transparent",
      }}
    >
      <TableCell>
        {dayjs(character.details.createdAt).format("DD/MM/YYYY")}
      </TableCell>
      <TableCell>
        {localImageBlob ? (
            <img
                className="w-16 h-16 object-cover rounded shadow"
                src={`data:image/png;base64,${localImageBlob}`}
                alt={character.details?.image || "Illustration"}
            />
        ) : (
            <div className="w-16 h-16 bg-gray-200 flex items-center justify-center rounded">
              <span className="text-xs text-gray-500">Pas d'image</span>
            </div>
        )}
      </TableCell>
      <TableCell>
        <div className="text-2xl flex items-center justify-center">
          {character.context?.promptGender == "Male"
            ? "♂"
            : character.context?.promptGender == "Female"
              ? "♀"
              : "⚥"}
        </div>
      </TableCell>
      <TableCell>{character.details.name}</TableCell>
      <TableCell>{character.details.age}</TableCell>
      <TableCell>{character.context?.promptRace}</TableCell>
      <TableCell>{character.details.profession}</TableCell>
      <TableCell>{character.context?.promptClass}</TableCell>
      <TableCell>{character.details.goal}</TableCell>
      <TableCell>{character.context?.promptSystem}</TableCell>

      <TableCell>
        <div className="flex gap-2">
          <ReadCharacterContent
            modalType={modalType === "read" ? "read" : null}
            setModalType={setModalType}
            character={character}
            selectedCharacter={selectedCharacter}
            setSelectedCharacter={setSelectedCharacter}
            handleReadCharacter={handleReadCharacter}
            handleUpdateIllustration={handleUpdateIllustration}
          />
          <UpdateCharacterDialog
            modalType={modalType === "update" ? "update" : null}
            setModalType={setModalType}
            character={character}
            selectedCharacter={selectedCharacter}
            setSelectedCharacter={setSelectedCharacter}
            updateCharacter={handleUpdateCharacter}
          />
          <RegenerateIllustrationButton
              character={character}
              refetch={refetch}
              selectedCharacter={selectedCharacter}
              setSelectedCharacter={setSelectedCharacter}
          />
          <DeleteCharacterContent
            modalType={modalType === "delete" ? "delete" : null}
            setModalType={setModalType}
            character={character}
            selectedCharacter={selectedCharacter}
            setSelectedCharacter={setSelectedCharacter}
            refetch={refetch}
            onCharacterDeleted={onCharacterDeleted}
            deletingCharacters={deletingCharacters}
            setDeletingCharacters={setDeletingCharacters}
          />
        </div>
      </TableCell>
    </TableRow>
  );
}
