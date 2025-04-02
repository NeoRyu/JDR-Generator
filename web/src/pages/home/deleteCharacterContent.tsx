import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog';
import {Button} from '@/components/ui/button';
import {Loader2, Trash2} from 'lucide-react';
import {useDeleteCharacter} from '@/services/deleteCharacter.service.ts';
import React from 'react';
import {ModalTypes} from '@/pages/home/home.tsx';
import {CharacterFull} from "@/components/model/character.model.tsx";

export interface DeleteCharacterContentProps {
    character: CharacterFull | null;
    modalType: ModalTypes;
    setModalType: React.Dispatch<React.SetStateAction<ModalTypes>>;
    refetch: () => void;
}

export function DeleteCharacterContent({
                                           character,
                                           modalType,
                                           setModalType,
                                           refetch,
                                       }: DeleteCharacterContentProps) {
    const { mutate: deleteMutation, isLoading: isDeleteLoading } = useDeleteCharacter();

    const handleDelete = () => {
        if (character && character.details?.id) {
            deleteMutation(character.details.id, {
                onSuccess: () => {
                    console.log('deleteMutation onSuccess');
                    setModalType(null);
                    refetch();
                },
                onError: (error) => {
                    console.error('deleteMutation onError:', error);
                },
            });
        } else {
            console.error('character or character.id is undefined');
        }
    };

    return (
        <Dialog
            open={modalType === 'delete'}
            onOpenChange={(open) => {
                if (!open) {
                    setModalType(null);
                }
            }}
        >
            <DialogTrigger asChild>
                <Button
                    className="button-red"
                    type="button"
                    variant="outline"
                    onClick={() => setModalType('delete')}
                    disabled={isDeleteLoading} // Désactiver le bouton pendant le chargement
                >
                    {isDeleteLoading ? <Loader2 className="animate-spin" /> : <Trash2 />}
                </Button>
            </DialogTrigger>

            <DialogContent className="sm:max-w-[475px]">
                <DialogTitle>
                    {character?.details?.name && character?.context?.promptSystem
                        ? `Êtes-vous sûr de vouloir supprimer le personnage "${character.details.name}" (Univers: ${character.context.promptSystem}) ?`
                        : 'Êtes-vous sûr de vouloir supprimer ce personnage ?'}
                </DialogTitle>
                <DialogDescription>Cette action est irréversible.</DialogDescription>
                <DialogFooter>
                    <Button type="button" variant="secondary" onClick={() => setModalType(null)}>
                        Annuler
                    </Button>
                    <Button
                        type="button"
                        variant="destructive"
                        onClick={handleDelete}
                        disabled={isDeleteLoading} // Désactiver le bouton pendant le chargement
                    >
                        {isDeleteLoading ? <Loader2 className="animate-spin" /> : 'Supprimer'}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}