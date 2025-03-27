import React, {Dispatch, SetStateAction, useCallback} from 'react';
import {Button} from '@/components/ui/button';
import {Dialog, DialogContent, DialogDescription, DialogFooter, DialogTitle,} from '@/components/ui/dialog';
import {QueryObserverResult, UseMutateFunction} from "react-query";
import {AxiosResponse} from "axios";


interface DeleteDialogProps {
    characterToDeleteId: number | null;
    modalType: 'details' | 'edit' | 'delete' | null;
    setModalType: Dispatch<SetStateAction<'details' | 'edit' | 'delete' | null>>;
    setCharacterToDeleteId: (id: number | null) => void;
    deleteCharacter: UseMutateFunction<void, Error, number, unknown>; // Correct type
    refetch: () => Promise<QueryObserverResult<AxiosResponse<any, any>, Error>>; // Correct type
}

export const DeleteDialog: React.FC<DeleteDialogProps> = ({ characterToDeleteId, setModalType, setCharacterToDeleteId, deleteCharacter, refetch, modalType }) => {
    const confirmDeleteCharacter = useCallback(async () => {
        if (characterToDeleteId !== null) {
            try {
                await deleteCharacter(characterToDeleteId);
                await refetch();
            } catch (error) {
                console.error('Erreur lors de la suppression du personnage:', error);
                // Gérez l'erreur ici, par exemple, affichez un message à l'utilisateur
            }
        }
        setModalType(null);
        setCharacterToDeleteId(null);
    }, [characterToDeleteId, deleteCharacter, refetch, setCharacterToDeleteId, setModalType]);

    return (
        <Dialog open={modalType === 'delete'} onOpenChange={(open) => {
            if (!open) {
                setModalType(null);
                setCharacterToDeleteId(null);
            }
        }}>
            <DialogContent>
                <DialogTitle>Supprimer le personnage</DialogTitle>
                <DialogDescription>
                    Êtes-vous sûr de vouloir supprimer ce personnage ? Cette action est irréversible.
                </DialogDescription>
                <DialogFooter>
                    <Button variant="outline" onClick={() => {
                        setModalType(null);
                        setCharacterToDeleteId(null);
                    }}>
                        Annuler
                    </Button>
                    <Button
                        variant="destructive"
                        onClick={confirmDeleteCharacter}
                        disabled={false} // Vous pouvez désactiver le bouton si nécessaire
                    >
                        Supprimer
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};
