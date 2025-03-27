package jdr.generator.api.characters;

import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CharacterFullModel {

    private CharacterDetailsModel details;
    private CharacterContextModel context;
    private CharacterIllustrationModel illustration;

    // Constructeurs, getters et setters
    public CharacterFullModel() {}

    public CharacterFullModel(CharacterDetailsModel details, CharacterContextModel context, CharacterIllustrationModel illustration) {
        this.details = details;
        this.context = context;
        this.illustration = illustration;
    }

}