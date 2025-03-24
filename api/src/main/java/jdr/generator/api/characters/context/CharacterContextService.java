package jdr.generator.api.characters.context;

public interface CharacterContextService {

    CharacterContextEntity save(CharacterContextEntity context);

    CharacterContextEntity findById(long id);

}
