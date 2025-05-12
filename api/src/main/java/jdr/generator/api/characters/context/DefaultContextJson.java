package jdr.generator.api.characters.context;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** Represents the default JSON structure for character context information. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultContextJson {

  @Builder.Default
  @JsonProperty("promptSystem")
  String promptSystem = "Vampire The Masquerade";

  @Builder.Default
  @JsonProperty("promptRace")
  String promptRace = "Vampire";

  @Builder.Default
  @JsonProperty("promptGender")
  String promptGender = "Male";

  @Builder.Default
  @JsonProperty("promptClass")
  String promptClass = "Nosferatu";

  @Builder.Default
  @JsonProperty("promptDescription")
  String promptDescription = "";
}