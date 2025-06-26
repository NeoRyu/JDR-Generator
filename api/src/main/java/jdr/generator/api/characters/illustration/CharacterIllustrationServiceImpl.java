package jdr.generator.api.characters.illustration;

import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the {@link CharacterIllustrationService} interface. */
@Service
@RequiredArgsConstructor
public class CharacterIllustrationServiceImpl implements CharacterIllustrationService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterIllustrationRepository characterIllustrationRepository;
    private final CharacterDetailsService characterDetailsService;

    /**
     * Saves a new character illustration.
     *
     * @param entity The CharacterIllustrationEntity to save.
     * @return The saved CharacterIllustrationEntity.
     */
    @Override
    @Transactional
    public CharacterIllustrationEntity save(CharacterIllustrationEntity entity) {
        LOGGER.info("Saving illustration with prompt : {}", entity.getImageLabel());
        try {
            return characterIllustrationRepository.save(entity);
        } catch (Exception e) {
            LOGGER.error("Error saving illustration: {}", entity, e);
            throw e;
        }
    }

    @Transactional
    public CharacterIllustrationEntity saveAndFlush(CharacterIllustrationEntity entity) {
        LOGGER.info("Saving and flush context: {}", entity);
        try {
            return this.characterIllustrationRepository.saveAndFlush(entity);
        } catch (Exception e) {
            LOGGER.error("Error saving and flush context: {}", entity, e);
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

    /**
     * Finds a character illustration by its associated character details ID.
     *
     * @param characterDetailsId The ID of the CharacterDetailsEntity to search by.
     * @return The found CharacterIllustrationEntity.
     * @throws RuntimeException if the illustration is not found for the given characterDetailsId.
     */
    @Override
    public CharacterIllustrationEntity findByCharacterDetailsId(Long characterDetailsId) {
        LOGGER.info("findByCharacterDetailsId for character ID: {}", characterDetailsId);
        CharacterDetailsEntity characterDetails =
                characterDetailsService.findById(characterDetailsId);
        return characterIllustrationRepository
                .findByCharacterDetailsId(characterDetails.getId())
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Illustration not found for character details ID: "
                                                + characterDetailsId));
    }

    /**
     * Updates an existing character illustration with a new image blob and optionally a new label.
     *
     * @param characterDetailsId The ID of the character details associated with the illustration.
     * @param newImageBlob The new image data as a byte array.
     * @param newImageLabel An optional new label for the image. If null, the existing label is
     *     kept.
     * @return The updated CharacterIllustrationEntity.
     */
    @Override
    @Transactional
    public CharacterIllustrationEntity updateIllustration(
            Long characterDetailsId, byte[] newImageBlob, String newImageLabel) {
        LOGGER.info("Updating illustration for character ID: {}", characterDetailsId);
        CharacterIllustrationEntity illustrationToUpdate =
                findByCharacterDetailsId(characterDetailsId);
        illustrationToUpdate.setImageBlob(newImageBlob);
        if (newImageLabel != null && !newImageLabel.isEmpty()) {
            illustrationToUpdate.setImageLabel(newImageLabel);
        }
        return characterIllustrationRepository.save(illustrationToUpdate);
    }
}
