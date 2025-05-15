package jdr.generator.api.characters.stats;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the {@link CharacterJsonDataService} interface. */
@Service
@RequiredArgsConstructor
public class CharacterJsonDataServiceImpl implements CharacterJsonDataService {

  private static final Logger LOGGER = LogManager.getLogger();

  private final CharacterJsonDataRepository characterJsonDataRepository;

  /**
   * Saves a new character json data.
   *
   * @param characterJsonDataEntity The CharacterJsonDataEntity to save.
   * @return The saved CharacterJsonDataEntity
   */
  @Override
  @Transactional
  public CharacterJsonDataEntity save(CharacterJsonDataEntity characterJsonDataEntity) {
    LOGGER.info("Saving character JSON data: {}", characterJsonDataEntity);
    try {
      return characterJsonDataRepository.save(characterJsonDataEntity);
    } catch (Exception e) {
      LOGGER.error("Error saving character JSON data: {}", characterJsonDataEntity, e);
      throw e;
    }
  }

  /**
   * Finds a character json data by its ID.
   *
   * @param id The ID of the character JSON data to find.
   * @return The found CharacterJsonDataEntity.
   * @throws RuntimeException if the json data is not found.
   */
  @Override
  public CharacterJsonDataEntity findById(long id) {
    LOGGER.info("Character JSON data findById: {}", id);
    return characterJsonDataRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Character JSON data not found with id: " + id));
  }

  /**
   * Finds a character json data by its CharacterDetails ID.
   *
   * @param characterDetailsId The ID of the CharacterDetailsEntity.
   * @return The Optional CharacterJsonDataEntity
   */
  @Override
  public Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long characterDetailsId) {
    LOGGER.info("Character JSON data findByCharacterDetailsId: {}", characterDetailsId);
    return characterJsonDataRepository.findByCharacterDetailsId(characterDetailsId);
  }
}
