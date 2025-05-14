package jdr.generator.api.characters.details;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for accessing and managing
 * {@link CharacterDetailsEntity} objects. */
@Repository
public interface CharacterDetailsRepository extends JpaRepository<CharacterDetailsEntity, Long> {

}