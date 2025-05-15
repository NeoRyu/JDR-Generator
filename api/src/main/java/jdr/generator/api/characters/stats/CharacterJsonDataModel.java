package jdr.generator.api.characters.stats;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the data transfer object for character JSON data.
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterJsonDataModel {
  public Long id;
  public Long characterDetailsId;
  public String jsonData;
  public Date createdAt;
  public Date updatedAt;
}
