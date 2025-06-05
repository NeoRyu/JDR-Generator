// deleteCharacterContent.tsx
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import {Loader2, Trash2} from "lucide-react";
import {useDeleteCharacter} from "@/services/deleteCharacter.service.ts";
import React, {Dispatch, SetStateAction} from "react";
import {ModalTypes} from "@/pages/home/home.tsx";
import {CharacterFull} from "@/components/model/character-full.model.tsx";

export interface DeleteCharacterContentProps {
  character: CharacterFull;
  selectedCharacter: CharacterFull | null;
  setSelectedCharacter: Dispatch<SetStateAction<CharacterFull | null>>;
  modalType: ModalTypes;
  setModalType: React.Dispatch<React.SetStateAction<ModalTypes>>;
  refetch: () => void;
  onCharacterDeleted: (characterId: number) => void;
  deletingCharacters: number[];
  setDeletingCharacters: React.Dispatch<React.SetStateAction<number[]>>;
}

export function DeleteCharacterContent({
  character,
  modalType,
  setModalType,
  selectedCharacter,
  setSelectedCharacter,
  refetch,
  onCharacterDeleted,
  deletingCharacters,
  setDeletingCharacters,
}: DeleteCharacterContentProps) {
  const { mutate: deleteMutation, isLoading: isDeleteLoading } =
    useDeleteCharacter();

  const handleDelete = () => {
    if (character && character.details?.id) {
      deleteMutation(character.details.id, {
        onSuccess: () => {
          console.log("deleteMutation onSuccess");
          setModalType(null);
          setSelectedCharacter(null);
          refetch();
          onCharacterDeleted(character.details.id);
          setDeletingCharacters(
            deletingCharacters.filter((id) => id !== character.details.id),
          );
        },
        onError: (error) => {
          console.error("deleteMutation onError:", error);
        },
      });
    } else {
      console.error("character or character.id is undefined");
    }
  };

  return (
    <Dialog
      open={
        modalType === "delete" &&
        selectedCharacter?.details.id === character.details.id
      }
      onOpenChange={(open) => {
        if (!open) {
          setModalType(null);
          setSelectedCharacter(null);
        } else {
          setSelectedCharacter(character);
        }
      }}
    >
      <DialogTrigger asChild>
        <Button
          className="button-red"
          type="button"
          variant="outline"
          onClick={() => setModalType("delete")}
          title="Supprimer définitivement ce personnage"
          disabled={isDeleteLoading} // Désactiver le bouton pendant le chargement
        >
          {isDeleteLoading ? <Loader2 className="animate-spin" /> : <Trash2 />}
        </Button>
      </DialogTrigger>

      <DialogContent className="sm:max-w-[475px]">
        <DialogTitle>
          {character?.details?.name && character?.context?.promptSystem
            ? `Êtes-vous sûr de vouloir supprimer le personnage "${character.details.name}" (Univers: ${character.context.promptSystem}) ?`
            : "Êtes-vous sûr de vouloir supprimer ce personnage ?"}
        </DialogTitle>
        <DialogDescription>Cette action est irréversible.</DialogDescription>
        <DialogFooter>
          <Button
            type="button"
            variant="secondary"
            onClick={() => setModalType(null)}
          >
            Annuler
          </Button>
          <Button
            type="button"
            variant="destructive"
            onClick={handleDelete}
            disabled={isDeleteLoading} // Désactiver le bouton pendant le chargement
          >
            {isDeleteLoading ? (
              <Loader2 className="animate-spin" />
            ) : (
              "Supprimer"
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
