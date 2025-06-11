package jdr.generator.api.characters.illustration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegenerateIllustrationRequestDto {
    private Long id;
    private String drawStyle;
}