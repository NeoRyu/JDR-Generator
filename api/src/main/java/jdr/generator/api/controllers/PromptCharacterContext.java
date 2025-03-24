package jdr.generator.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptCharacterContext {

    @Builder.Default
    @JsonProperty("promptSystem")
    String promptSystem = "undefined";

    @Builder.Default
    @JsonProperty("promptRace")
    String promptRace = "Human";

    @Builder.Default
    @JsonProperty("promptClass")
    String promptClass = "";

    @Builder.Default
    @JsonProperty("promptDescription")
    String promptDescription = "";

}
