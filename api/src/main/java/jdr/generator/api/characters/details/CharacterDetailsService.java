package jdr.generator.api.characters.details;

import jdr.generator.api.characters.CharacterFullModel;

import java.util.List;


public interface CharacterDetailsService {

    List<CharacterDetailsModel> getAllCharacters();

    List<CharacterFullModel> getAllCharactersFull();

    CharacterDetailsEntity save(CharacterDetailsEntity character);

    CharacterDetailsEntity findById(Long id);

    CharacterDetailsEntity updateCharacterDetails(Long id, CharacterDetailsModel updatedCharacter);
}
