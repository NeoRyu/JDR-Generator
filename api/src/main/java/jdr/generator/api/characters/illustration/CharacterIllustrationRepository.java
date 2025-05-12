package jdr.generator.api.characters.illustration;

import jdr.generator.api.characters.details.CharacterDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Repository interface for accessing and managing {@link CharacterIllustrationEntity} objects. */
@Repository
public interface CharacterIllustrationRepository
        extends JpaRepository<CharacterIllustrationEntity, Long> {
  /**
   * Finds a character illustration by its associated character details.
   *
   * @param imageDetails The CharacterDetailsEntity to search by.
   * @return An Optional containing the found CharacterIllustrationEntity, or an empty Optional if not
   * found.
   */
  Optional<CharacterIllustrationEntity> findByImageDetails(CharacterDetailsEntity imageDetails);
}