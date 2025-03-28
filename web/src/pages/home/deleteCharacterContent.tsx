import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog';
import {Button} from '@/components/ui/button';
import {Trash2} from 'lucide-react';
import {useDeleteCharacter} from '@/services/delete-character.service';
import React from 'react';
import {ModalTypes} from "@/pages/home/home.tsx";


interface DeleteCharacterContentProps {
    characterToDeleteId: number | null;
    modalType: ModalTypes;
    setModalType: React.Dispatch<React.SetStateAction<ModalTypes>>;
    setCharacterToDeleteId: React.Dispatch<React.SetStateAction<number | null>>;
    deleteCharacter: (id: number) => void;
    refetch: () => void;
}

export function DeleteCharacterContent({
   characterToDeleteId,
   modalType,
   setModalType,
   setCharacterToDeleteId,
   deleteCharacter,
   refetch,
}: DeleteCharacterContentProps) {
    console.log('> characterToDeleteId:', characterToDeleteId);
    const { mutate: deleteMutation, isLoading: isDeleteLoading } = useDeleteCharacter();

    const handleDelete = () => {
        console.log('handleDelete called');
        if (characterToDeleteId) {
            console.log('characterToDeleteId:', characterToDeleteId);
            deleteMutation(characterToDeleteId, {
                onSuccess: () => {
                    console.log('deleteMutation onSuccess');
                    deleteCharacter(characterToDeleteId);
                    setModalType(null);
                    setCharacterToDeleteId(null);
                    refetch();
                },
                onError: (error) => {
                    console.error('deleteMutation onError:', error);
                },
            });
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
                    variant="outline">
                    <Trash2 />
                </Button>
            </DialogTrigger>

            <DialogContent className="sm:max-w-[425px]">
                <DialogTitle>Êtes-vous sûr de vouloir supprimer ce personnage ?</DialogTitle>
                <DialogDescription>Cette action est irréversible.</DialogDescription>
                <DialogFooter>
                    <Button type="button" variant="secondary" onClick={() => setModalType(null)}>
                        Annuler
                    </Button>
                    <Button type="button"
                            variant="destructive"
                            onClick={handleDelete}
                            disabled={isDeleteLoading}
                    >
                        {isDeleteLoading ? 'Suppression...' : 'Supprimer'}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}