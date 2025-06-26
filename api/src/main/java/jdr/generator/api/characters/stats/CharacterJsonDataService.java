package jdr.generator.api.characters.stats;

import java.util.Optional;

/** Service interface for managing character JSON data operations. */
public interface CharacterJsonDataService {

  /**
   * Saves new character JSON data.
   *
   * @param entity The CharacterJsonDataEntity to save.
   * @return The saved CharacterJsonDataEntity.
   */
  CharacterJsonDataEntity save(CharacterJsonDataEntity entity);

  CharacterJsonDataEntity saveAndFlush(CharacterJsonDataEntity entity);

  /**
   * Finds character JSON data by its ID.
   *
   * @param id The ID of the character JSON data to find.
   * @return The found CharacterJsonDataEntity.
   */
  CharacterJsonDataEntity findById(long id);

  /**
   * Finds character JSON data by its associated character details ID.
   *
   * @param id The ID of the CharacterDetailsEntity.
   * @return An Optional containing the found CharacterJsonDataEntity, or an empty Optional if not
   *     found.
   */
  Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long id);
}
