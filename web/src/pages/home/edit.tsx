import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import {CharacterFull} from '@/components/model/character.model';
import dayjs from 'dayjs';
import {Button} from "@/components/ui/button.tsx";
import {Dispatch, SetStateAction} from 'react';


interface DetailsDialogProps {
    character: CharacterFull | null;
    modalType: 'details' | 'edit' | 'delete' | null;
    setModalType: Dispatch<SetStateAction<'details' | 'edit' | 'delete' | null>>;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: (character: CharacterFull | null) => void;
}

export function DetailsDialog({ character, modalType, setModalType, setSelectedCharacter }: DetailsDialogProps) {
    if (!character || modalType !== 'details') {
        return null;
    }

    const handleClose = () => {
        setModalType(null);
        setSelectedCharacter(null);
    };

    return (
        <Dialog open={modalType === 'details'} onOpenChange={(open) => open ? setModalType('details') : handleClose()}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Détails du personnage</DialogTitle>
                    <DialogDescription>
                        Informations complètes sur le personnage.
                    </DialogDescription>
                </DialogHeader>

                <div className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Date de création
                            </label>
                        </div>
                        <div className="col-span-3">
                            {dayjs(character.details.createdAt).format('DD/MM/YYYY')}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Nom
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.name}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Espèce
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.context?.promptRace}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Âge
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.age}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Archétype
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.context?.promptClass}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Profession
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.profession}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Objectif
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.goal}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Lieu de résidence
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.residenceLocation}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Univers du jeu
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.context?.promptSystem}
                        </div>
                    </div>

                    <div className="grid grid-cols-4 items-start gap-4">
                        <div className="flex flex-col space-y-1">
                            <label className="text-right font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                Description
                            </label>
                        </div>
                        <div className="col-span-3">
                            {character.details?.selfDescription}
                        </div>
                    </div>
                </div>

                <DialogClose className="absolute right-4 top-4">
                    <Button variant="ghost" className="h-8 w-8 p-0">
                        <span className="sr-only">Fermer</span>
                    </Button>
                </DialogClose>
            </DialogContent>
        </Dialog>
    );
}