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

  private CharacterDetailsModel details;
  private CharacterContextModel context;
  private CharacterIllustrationModel illustration;
  private CharacterJsonDataModel jsonData;
}
