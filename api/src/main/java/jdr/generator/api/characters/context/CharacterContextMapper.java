package jdr.generator.api.characters.context;

import java.util.Date;

public class CharacterContextMapper {

    public static CharacterContextEntity convertModelToEntity(CharacterContextModel model) {
        CharacterContextEntity entity = new CharacterContextEntity();
        entity.setPromptSystem(model.promptSystem);
        entity.setPromptRace(model.promptRace);
        entity.setPromptGender(model.promptGender);
        entity.setPromptClass(model.promptClass);
        entity.setPromptDescription(model.promptDescription);
        entity.setCreatedAt(new Date());
        return entity;
    }
}