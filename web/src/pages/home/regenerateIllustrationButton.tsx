// src/pages/home/RegenerateIllustrationButton.tsx
import React, { useState, Dispatch, SetStateAction } from "react";
import { Button } from "@/components/ui/button";
import { Loader2, Users } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
} from "@/components/ui/dialog";
import { useRegenerateIllustration } from "@/services/illustrateCharacters.service.ts";
import { arrayBufferToBase64 } from "@/lib/utils";
import { CharacterFull } from "@/components/model/character-full.model.tsx";
import CustomSelect from "@/components/ui/customSelect.tsx";
import { illustrationDrawStyles } from "@/pages/home/listes/illustrationDrawStyles.tsx";

interface RegenerateIllustrationButtonProps {
    character: CharacterFull;
    refetch: () => void;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: Dispatch<SetStateAction<CharacterFull | null>>;
}

export const RegenerateIllustrationButton: React.FC<
    RegenerateIllustrationButtonProps
> = ({ character, refetch, selectedCharacter, setSelectedCharacter }) => {
    const [isRegenerating, setIsRegenerating] = useState(false);
    const [showConfirmationModal, setShowConfirmationModal] = useState(false);
    const [selectedDrawStyle, setSelectedDrawStyle] = useState<string>("");

    const regenerateIllustrationMutation = useRegenerateIllustration();

    React.useEffect(() => {
        // Initialiser le style avec celui du personnage quand la modale s'ouvre ou le personnage change.
        if (showConfirmationModal && character?.context?.promptDrawStyle && selectedDrawStyle === "") {
            setSelectedDrawStyle(character.context.promptDrawStyle);
        } else if (!showConfirmationModal && character?.context?.promptDrawStyle) {
            // Si la modale est fermée, on peut vouloir réinitialiser si le personnage sélectionné change en arrière-plan
            setSelectedDrawStyle(character.context.promptDrawStyle);
        }
    }, [character, showConfirmationModal]);

    const handleConfirmRegeneration = async () => {
        if (!character?.details?.id) {
            console.error("Character ID is missing for regeneration.");
            return;
        }

        setShowConfirmationModal(false); // Ferme la modale de confirmation
        setIsRegenerating(true); // Active l'état de chargement

        try {
            // Déterminer le style final à envoyer à l'API
            const finalDrawStyle = selectedDrawStyle || character.context.promptDrawStyle;

            // Appel à mutateAsync avec l'objet payload
            const response = await regenerateIllustrationMutation.mutateAsync({
                id: character.details.id,
                drawStyle: finalDrawStyle,
            });

            // Mise à jour de l'illustration du personnage sélectionné
            if (response.data && selectedCharacter) {
                const newImageBlob = await arrayBufferToBase64(response.data);
                if (selectedCharacter.details?.id === character.details?.id) {
                    setSelectedCharacter(prev => {
                        if (!prev) return null;
                        return {
                            ...prev,
                            illustration: {
                                ...prev.illustration,
                                imageBlob: newImageBlob,
                            },
                        };
                    });
                }
            }

            void refetch(); // Déclenche un rechargement complet de la liste des personnages dans Home.tsx

            console.log(
                "Illustration régénérée avec succès pour le personnage:",
                character.details?.name
            );
        } catch (error) {
            console.error(
                "Erreur lors de la régénération de l'illustration :",
                error
            );
        } finally {
            setIsRegenerating(false); // Désactive l'état de chargement
        }
    };

    const toggleConfirmationModal = () => {
        setShowConfirmationModal(!showConfirmationModal);
        // Initialiser le style au moment de l'ouverture si la modale s'ouvre
        if (!showConfirmationModal && character?.context?.promptDrawStyle) {
            setSelectedDrawStyle(character.context.promptDrawStyle);
        }
    };

    return (
        <>
            <Button
                onClick={() => setShowConfirmationModal(true)} // Ouvre la modale de confirmation
                disabled={isRegenerating} // Désactive le bouton pendant la régénération
                type="button"
                variant="outline"
                className="button-red"
                style={{ color: "#e36804!important"}}
                title="Générer un nouveau portrait pour le personnage"
            >
                {isRegenerating ? <Loader2 className="animate-spin" /> : <Users />}
            </Button>

            <Dialog open={showConfirmationModal} onOpenChange={toggleConfirmationModal }>
                <DialogContent className="max-w-md md:max-lg">
                    <DialogTitle>ACTION : Régénération du portrait de {character.details?.name}</DialogTitle>
                    <DialogDescription>
                        Voulez-vous vraiment générer une nouvelle illustration de portrait
                        pour le personnage {character.details?.name} ? Cette action est irréversible...

                        <br/><br/>
                        <div className="flex flex-col gap-2">
                            <span>Style actuel :
                                {illustrationDrawStyles.find(s => s.value === character.context.promptDrawStyle)?.label || character.context.promptDrawStyle}
                            </span>
                            <CustomSelect
                                options={illustrationDrawStyles}
                                value={selectedDrawStyle}
                                onChange={(value: string) => setSelectedDrawStyle(value)}
                                placeholder="Sélectionnez un nouveau style"
                            />
                        </div>
                        <br/>
                        <img
                            className="object-cover rounded shadow cursor-pointer"
                            src={`data:image/png;base64,${character.illustration?.imageBlob}`}
                            alt={character.details?.image || "Illustration"}
                        />
                    </DialogDescription>
                    <DialogFooter>
                        <Button variant="outline" onClick={() => setShowConfirmationModal(false)}>
                            Annuler
                        </Button>
                        <Button
                            onClick={handleConfirmRegeneration}
                            disabled={regenerateIllustrationMutation.isLoading}
                        >
                            {regenerateIllustrationMutation.isLoading && (
                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                            )}
                            Confirmer
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    );
};