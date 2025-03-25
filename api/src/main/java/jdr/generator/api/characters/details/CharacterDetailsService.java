package jdr.generator.api.characters.details;

import java.util.List;


public interface CharacterDetailsService {

    List<CharacterDetailsModel> getAllCharacters();

    CharacterDetailsEntity save(CharacterDetailsEntity character);

}
