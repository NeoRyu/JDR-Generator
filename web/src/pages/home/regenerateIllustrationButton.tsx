// src/pages/home/RegenerateIllustrationButton.tsx
import React, { useState, Dispatch, SetStateAction } from "react";
import { Button } from "@/components/ui/button";
import {Loader2, Users} from "lucide-react";
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

    const regenerateIllustrationMutation = useRegenerateIllustration();

    const handleConfirmRegeneration = async () => {
        if (!character?.details?.id) {
            console.error("Character ID is missing for regeneration.");
            return;
        }

        setShowConfirmationModal(false); // Ferme la modale de confirmation
        setIsRegenerating(true); // Active l'état de chargement

        try {
            const response = await regenerateIllustrationMutation.mutateAsync(
                character.details.id
            );
            const newImageBlob = await arrayBufferToBase64(response.data);

            // Met à jour l'image du personnage dans la liste locale (géré par characterRow)
            // On ne modifie pas directement l'état de 'characterRow' ici,
            // on laisse le 'refetch' global ou la mise à jour de 'selectedCharacter' propager le changement.

            // Si le personnage de cette ligne est également celui actuellement ouvert dans la modale de lecture,
            // mettez à jour l'image dans la modale pour une consistance visuelle immédiate.
            if (selectedCharacter && selectedCharacter.details?.id === character.details?.id) {
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

    return (
        <>
            <Button
                onClick={() => setShowConfirmationModal(true)} // Ouvre la modale de confirmation
                disabled={isRegenerating} // Désactive le bouton pendant la régénération
                type="button"
                variant="outline"
                className="button-red"
                style={{ color: "#e36804!important"}}
            >
                {isRegenerating ? <Loader2 className="animate-spin" /> : <Users />}
            </Button>

            <Dialog open={showConfirmationModal} onOpenChange={setShowConfirmationModal}>
                <DialogContent>
                    <DialogTitle>ACTION : Régénération du portrait de {character.details?.name}</DialogTitle>
                    <DialogDescription>
                        Voulez-vous vraiment générer une nouvelle illustration de portrait
                        pour le personnage {character.details?.name} ? Cette action est irréversible...

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