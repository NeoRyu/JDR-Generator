package jdr.generator.api.characters.illustration;

import jdr.generator.api.characters.details.CharacterDetailsEntity;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterIllustrationModel {
    private Long id;

    private String imageLabel;
    private byte[] imageBlob;
    private CharacterDetailsEntity imageDetails;

}