package jdr.generator.api.characters;

import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.stats.CharacterJsonDataModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the complete model of a character, including its details, context, illustration, and
 * JSON data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterFullModel {

  private CharacterContextModel context;
  private CharacterDetailsModel details;
  private CharacterJsonDataModel jsonData;
  private CharacterIllustrationModel illustration;
}
