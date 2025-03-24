package jdr.generator.api.characters.context;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="character_context")
public class CharacterContextEntity {

    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="prompt_system", nullable=false)
    private String promptSystem;

    @Column(name="prompt_race")
    private String promptRace;

    @Column(name="prompt_gender")
    private String promptGender;

    @Column(name="prompt_class")
    private String promptClass;

    @Column(name="prompt_description")
    private String promptDescription;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // public void setContextId(Integer contextId) {}
}
