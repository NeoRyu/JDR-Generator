package jdr.generator.api.characters.details;

import jdr.generator.api.characters.CharacterFullModel;

import java.util.List;


public interface CharacterDetailsService {


    CharacterDetailsEntity save(CharacterDetailsEntity character);

    CharacterDetailsEntity findById(Long id);

    CharacterDetailsEntity updateCharacterDetails(Long id, CharacterFullModel updatedCharacter);

    void deleteCharacter(Long id);

    List<CharacterDetailsModel> getAllCharacters();

    List<CharacterFullModel> getAllCharactersFull();

}
