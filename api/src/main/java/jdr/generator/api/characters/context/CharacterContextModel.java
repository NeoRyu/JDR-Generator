package jdr.generator.api.characters.context;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class CharacterContextModel {

    public String promptSystem;
    public String promptRace;
    public String promptGender;
    public String promptClass;
    public String promptDescription;

    public Date createdAt;

}
