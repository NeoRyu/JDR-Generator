package jdr.generator.api.characters.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CharacterJsonDataRepository extends JpaRepository<CharacterJsonDataEntity, Long> {

    Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long characterDetailsId);

}