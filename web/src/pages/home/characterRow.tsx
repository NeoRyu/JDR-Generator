// CharacterRow.tsx

import {TableCell, TableRow} from '@/components/ui/table';
import dayjs from 'dayjs';
import {CharacterDetailsModel, CharacterFull} from '@/components/model/character.model';
import {ReadCharacterContent} from "@/pages/home/readCharacterContent.tsx";
import {UpdateCharacterDialog} from "@/pages/home/updateCharacterContent.tsx";
import {DeleteCharacterContent} from "@/pages/home/deleteCharacterContent.tsx";
import {ModalTypes} from "@/pages/home/home.tsx";
import {Dispatch, SetStateAction} from "react";

interface CharacterRowProps {
    character: CharacterFull;
    modalType: ModalTypes;
    setModalType: Dispatch<SetStateAction<ModalTypes>>;
    selectedCharacter: CharacterFull | null;
    setSelectedCharacter: Dispatch<SetStateAction<CharacterFull | null>>;
    updateCharacter: (updatedCharacter: CharacterDetailsModel) => Promise<void>; // Modified type here
    refetch: () => void;
}

export function CharacterRow({
                                 character,
                                 modalType,
                                 setModalType,
                                 selectedCharacter,
                                 setSelectedCharacter,
                                 updateCharacter,
                                 refetch,
                             }: CharacterRowProps) {

    const handleReadCharacter = (character: CharacterFull) => {
        setSelectedCharacter(character);
        setModalType('read');
    };

    const handleUpdateCharacter = async (updatedCharacter: CharacterDetailsModel) => { // Modified type here
        try {
            await updateCharacter(updatedCharacter);
            await refetch();
            setModalType(null);
            setSelectedCharacter(null);
        } catch (error) {
            console.error('Erreur lors de la sauvegarde du personnage :', error);
        }
    };

    return (
        <TableRow key={character.details.id}>
            <TableCell>{dayjs(character.details.createdAt).format('DD/MM/YYYY')}</TableCell>
            <TableCell>{character.context?.promptGender == 'Male' ? '♂' : (character.context?.promptGender == 'Female' ? '♀' : '⚥')}</TableCell>
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
                        modalType={modalType === 'read' ? 'read' : null}
                        setModalType={setModalType}
                        character={character}
                        selectedCharacter={selectedCharacter}
                        setSelectedCharacter={setSelectedCharacter}
                        handleReadCharacter={handleReadCharacter}
                    />
                    <UpdateCharacterDialog
                        modalType={modalType === 'update' ? 'update' : null}
                        setModalType={setModalType}
                        character={character}
                        selectedCharacter={selectedCharacter}
                        setSelectedCharacter={setSelectedCharacter}
                        updateCharacter={handleUpdateCharacter}
                    />
                    <DeleteCharacterContent
                        key={character.details.id}
                        modalType={modalType === 'delete' ? 'delete' : null}
                        setModalType={setModalType}
                        characterId={character.details.id}
                        refetch={refetch}
                    />
                </div>
            </TableCell>
        </TableRow>
    );
}