package jdr.generator.api.characters.details;

public interface CharacterDetailsService {

    CharacterDetailsEntity save(CharacterDetailsEntity character);

    CharacterDetailsEntity findById(long id);

}
