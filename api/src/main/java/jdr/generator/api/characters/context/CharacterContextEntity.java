package jdr.generator.api.characters.context;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/** Represents the context information for a character. */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "character_context")
public class CharacterContextEntity {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "prompt_system", nullable = false)
  private String promptSystem;

  @Column(name = "prompt_race")
  private String promptRace;

  @Column(name = "prompt_gender")
  private String promptGender;

  @Column(name = "prompt_class")
  private String promptClass;

  @Column(name = "prompt_description")
  private String promptDescription;

  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  // public void setContextId(Integer contextId) {}
}