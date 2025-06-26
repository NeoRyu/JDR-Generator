package jdr.generator.api.characters.illustration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "character_details_id", unique = true, nullable = false)
  private CharacterDetailsEntity characterDetails;

  @Column(name = "image_label", columnDefinition = "TEXT")
  private String imageLabel;

  @Lob
  @Column(name = "image_blob")
  private byte[] imageBlob;
}
