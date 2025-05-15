// updateCharacterContent.tsx
import React, {useRef, useState} from "react";
import {Button} from "@/components/ui/button";
import {Dialog, DialogContent, DialogDescription, DialogTitle, DialogTrigger,} from "@/components/ui/dialog";
import {Loader2, Pen} from "lucide-react";
import {CharacterFull} from "@/components/model/character-full.model.tsx";
import {CharacterDetailsModel} from "@/components/model/character-details.model.tsx";
import {CharacterForm, CharacterFormRef,} from "@/components/form/character-form";
import {useTheme} from "@/components/theme-provider";
import {ModalTypes} from "@/pages/home/home.tsx";

interface UpdateCharacterDialogProps {
  character: CharacterFull;
  modalType: ModalTypes;
  selectedCharacter: CharacterFull | null;
  setModalType: React.Dispatch<React.SetStateAction<ModalTypes>>;
  setSelectedCharacter: (character: CharacterFull | null) => void;
  updateCharacter: (character: CharacterDetailsModel) => Promise<void>;
}

export const UpdateCharacterDialog: React.FC<UpdateCharacterDialogProps> = ({
  character,
  modalType,
  selectedCharacter,
  setModalType,
  setSelectedCharacter,
  updateCharacter,
}) => {
  const { theme } = useTheme();
  const characterFormRef = useRef<CharacterFormRef>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleFormSubmit = async (e: React.FormEvent) => {
    setIsLoading(true);
    if (characterFormRef.current) {
      await characterFormRef.current.handleSubmit(e);
    }
    setIsLoading(false);
  };

  return (
    <Dialog
      open={
        modalType === "update" &&
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
            setModalType("update");
            setSelectedCharacter(character);
          }}
          className="button"
          type="button"
          variant="outline"
        >
          <Pen />
        </Button>
      </DialogTrigger>

      <DialogContent
        className={`max-w-[90vw] w-full ${theme === "light" ? "dark" : "dark"}`}
      >
        <div className="flex justify-between items-start">
          <div
            style={{
              maxHeight: "80vh",
              overflowY: "auto",
              width: "80%",
              paddingRight: "1rem",
            }}
          >
            <DialogTitle>EDITION DU PERSONNAGE</DialogTitle>
            <DialogDescription>
              [ID_CONTEXT={selectedCharacter?.context?.id ?? "??"}] [ID_DETAILS=
              {selectedCharacter?.details?.id ?? "??"}] [ID_ILLUSTRATION=
              {selectedCharacter?.illustration?.id ?? "??"}]
            </DialogDescription>
          </div>
          <div className="flex justify-end">
            {selectedCharacter && (
              <div style={{ textAlign: "right", paddingRight: "1rem" }}>
                {isLoading ? (
                  <Button disabled className="w-full">
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Sauvegarde en cours...
                  </Button>
                ) : (
                  <Button
                    type="button"
                    onClick={handleFormSubmit}
                    className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded shadow-md transition duration-300 ease-in-out transform hover:scale-95"
                  >
                    Sauvegarder le personnage
                  </Button>
                )}
              </div>
            )}
          </div>
        </div>
        {selectedCharacter && (
          <CharacterForm
            ref={characterFormRef}
            initialValues={selectedCharacter}
            onSubmit={updateCharacter}
          />
        )}
      </DialogContent>
    </Dialog>
  );
};
