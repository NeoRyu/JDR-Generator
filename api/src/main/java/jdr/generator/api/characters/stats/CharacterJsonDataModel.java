package jdr.generator.api.characters.stats;

import lombok.*;

import java.util.Date;


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
