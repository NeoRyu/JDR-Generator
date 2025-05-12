package jdr.generator.api.characters.stats;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterJsonDataServiceImpl implements CharacterJsonDataService {

  private static final Logger LOGGER = LogManager.getLogger();

  private final CharacterJsonDataRepository characterJsonDataRepository;

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

  @Override
  public CharacterJsonDataEntity findById(long id) {
    LOGGER.info("Character JSON data findById: {}", id);
    return characterJsonDataRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Character JSON data not found with id: " + id));
  }

  @Override
  public Optional<CharacterJsonDataEntity> findByCharacterDetailsId(Long characterDetailsId) {
    LOGGER.info("Character JSON data findByCharacterDetailsId: {}", characterDetailsId);
    return characterJsonDataRepository.findByCharacterDetailsId(characterDetailsId);
  }
}
