package jdr.generator.api.characters.context;

import lombok.*;

import java.util.Date;

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
