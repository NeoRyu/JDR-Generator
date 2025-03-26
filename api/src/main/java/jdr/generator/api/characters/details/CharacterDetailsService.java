package jdr.generator.api.characters.details;

import java.util.List;


public interface CharacterDetailsService {

    List<CharacterDetailsModel> getAllCharacters();

    CharacterDetailsEntity save(CharacterDetailsEntity character);

    CharacterDetailsEntity findById(Long id);

    CharacterDetailsEntity updateCharacterDetails(Long id, CharacterDetailsModel updatedCharacter);
}
