package jdr.generator.api.characters;

import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterFullModel {

    private CharacterDetailsModel details;
    private CharacterContextModel context;
    private CharacterIllustrationModel illustration;

}