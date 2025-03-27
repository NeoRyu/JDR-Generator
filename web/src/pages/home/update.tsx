import {Character, CharacterFull} from "@/components/model/character.model.tsx";
import React, {Dispatch, SetStateAction, useRef} from "react";
import {useTheme} from "@/components/theme-provider.tsx";
import {Dialog, DialogContent, DialogDescription, DialogTitle, DialogTrigger} from "@radix-ui/react-dialog";
import {Button} from "@/components/ui/button.tsx";
import {Pen} from "lucide-react";
import {CharacterForm} from "@/components/form/character-form.tsx";


interface EditDialogProps {
    character: CharacterFull;
    modalType: 'details' | 'edit' | 'delete' | null;
    setModalType: Dispatch<SetStateAction<'details' | 'edit' | 'delete' | null>>;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: (character: CharacterFull | null) => void;
    updateCharacter: (character: Character) => Promise<void>;
}

export const EditDialog: React.FC<EditDialogProps> = ({character, modalType, selectedCharacter, setModalType, setSelectedCharacter, updateCharacter }) => {
    const { theme } = useTheme();
    const characterFormRef = useRef<CharacterFormRef>(null);

    return (
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
                }} variant="outline" className="button" type="button">
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
                        onSubmit={updateCharacter}
                        renderSaveButton={() => null} // Ne pas rendre le bouton ici, il est déjà rendu dans l'en-tête
                    />
                )}
            </DialogContent>

        </Dialog>
    );
};