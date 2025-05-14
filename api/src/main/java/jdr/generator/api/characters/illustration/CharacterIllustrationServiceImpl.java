package jdr.generator.api.characters.illustration;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the
 * {@link CharacterIllustrationService} interface. */
@Service
@RequiredArgsConstructor
public class CharacterIllustrationServiceImpl implements CharacterIllustrationService {

  private static final Logger LOGGER = LogManager.getLogger();

  private final CharacterIllustrationRepository characterIllustrationRepository;

  /**
   * Saves a new character illustration.
   *
   * @param characterIllustrationEntity The CharacterIllustrationEntity to save.
   * @return The saved CharacterIllustrationEntity.
   */
  @Override
  @Transactional
  public CharacterIllustrationEntity save(CharacterIllustrationEntity characterIllustrationEntity) {
    LOGGER.info(
            "Saving illustration with prompt : {}", characterIllustrationEntity.getImageLabel());
    try {
      return characterIllustrationRepository.save(characterIllustrationEntity);
    } catch (Exception e) {
      LOGGER.error("Error saving illustration: {}", characterIllustrationEntity, e);
      throw e;
    }
  }

  /**
   * Finds a character illustration by its ID.
   *
   * @param id The ID of the character illustration to find.
   * @return The found CharacterIllustrationEntity.
   * @throws RuntimeException if the illustration is not found.
   */
  @Override
  public CharacterIllustrationEntity findById(long id) {
    LOGGER.info("Illustration findById: {}", id);
    return characterIllustrationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Illustration not found with id: " + id));
  }
}