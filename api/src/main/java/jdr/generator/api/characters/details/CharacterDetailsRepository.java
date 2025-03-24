package jdr.generator.api.characters.details;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterDetailsRepository extends JpaRepository<CharacterDetailsEntity, Long> {

}
