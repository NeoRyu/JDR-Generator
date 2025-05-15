package jdr.generator.api.characters.context;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the data transfer object for character context information.
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterContextModel {
  public Long id;

  public String promptSystem;
  public String promptRace;
  public String promptGender;
  public String promptClass;
  public String promptDescription;

  public Date createdAt;
}
