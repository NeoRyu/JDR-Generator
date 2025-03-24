package jdr.generator.api.characters.context;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterContextRepository extends JpaRepository<CharacterContextEntity, Long> {

}
