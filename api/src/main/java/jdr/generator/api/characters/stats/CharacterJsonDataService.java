package jdr.generator.api.characters.stats;


import java.util.Optional;

public interface CharacterJsonDataService {

    CharacterJsonDataEntity save(CharacterJsonDataEntity characterJsonDataEntity);

    CharacterJsonDataEntity findById(long id);

    Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long id);
}