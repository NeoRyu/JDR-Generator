package jdr.generator.api.characters.illustration;

import jakarta.persistence.*;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents the illustration data associated with a character. */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "character_illustration")
public class CharacterIllustrationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_label", columnDefinition = "TEXT")
  private String imageLabel;

  @Lob
  @Column(name = "image_blob")
  private byte[] imageBlob;

  @ManyToOne
  @JoinColumn(name = "image_details", referencedColumnName = "id")
  private CharacterDetailsEntity imageDetails;
}