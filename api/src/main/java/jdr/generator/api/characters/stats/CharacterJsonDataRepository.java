package jdr.generator.api.characters.stats;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for accessing and managing
 * {@link CharacterJsonDataEntity} objects. */
@Repository
public interface CharacterJsonDataRepository extends JpaRepository<CharacterJsonDataEntity, Long> {

  /**
   * Finds character JSON data by its associated character details ID.
   *
   * @param characterDetailsId The ID of the CharacterDetailsEntity.
   * @return An Optional containing the found CharacterJsonDataEntity, or an empty Optional if not
   *     found.
   */
  Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long characterDetailsId);
}
