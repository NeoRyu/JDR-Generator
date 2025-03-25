package jdr.generator.api.characters.context;

import java.util.List;

public interface CharacterContextService {

    CharacterContextEntity save(CharacterContextEntity context);

    CharacterContextEntity findById(long id);

    List<CharacterContextModel> getAllContexts();

    CharacterContextModel createCharacterContextModel(DefaultContextJson data);
}
