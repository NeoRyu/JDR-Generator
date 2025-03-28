package jdr.generator.api.characters.illustration;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CharacterIllustrationServiceImpl implements CharacterIllustrationService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterIllustrationRepository characterIllustrationRepository;

    @Override
    @Transactional
    public CharacterIllustrationEntity save(CharacterIllustrationEntity characterIllustrationEntity) {
        LOGGER.info("Saving illustration: {}", characterIllustrationEntity);
        try {
            return characterIllustrationRepository.save(characterIllustrationEntity);
        } catch (Exception e) {
            LOGGER.error("Error saving illustration: {}", characterIllustrationEntity, e);
            throw e;
        }
    }

    @Override
    public CharacterIllustrationEntity findById(long id) {
        LOGGER.info("Illustration findById: {}", id);
        return characterIllustrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illustration not found with id: " + id));
    }
}