package jdr.generator.api.characters.illustration;

import jdr.generator.api.characters.details.CharacterDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterIllustrationRepository extends JpaRepository<CharacterIllustrationEntity, Long> {
    Optional<CharacterIllustrationEntity> findByImageDetails(CharacterDetailsEntity imageDetails);
}