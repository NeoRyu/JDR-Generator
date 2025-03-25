package jdr.generator.api.characters.illustration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterIllustrationRepository extends JpaRepository<CharacterIllustrationEntity, Long> {
}