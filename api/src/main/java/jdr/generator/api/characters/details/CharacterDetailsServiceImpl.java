package jdr.generator.api.characters.details;

import jdr.generator.api.characters.CharacterFullModel;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.context.CharacterContextRepository;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CharacterDetailsServiceImpl implements CharacterDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CharacterDetailsServiceImpl.class);

    private final ModelMapper modelMapper;
    private final CharacterContextRepository characterContextRepository;
    private final CharacterDetailsRepository characterDetailsRepository;
    private final CharacterIllustrationRepository characterIllustrationRepository;

    @Autowired
    public CharacterDetailsServiceImpl(
            final ModelMapper modelMapper,
            final CharacterContextRepository characterContextRepository,
            final CharacterDetailsRepository characterDetailsRepository,
            final CharacterIllustrationRepository characterIllustrationRepository
    ) {
        this.characterDetailsRepository = characterDetailsRepository;
        this.modelMapper = modelMapper;
        this.characterContextRepository = characterContextRepository;
        this.characterIllustrationRepository = characterIllustrationRepository;
    }

    @Override
    @Transactional
    public CharacterDetailsEntity save(final CharacterDetailsEntity character) {
        logger.info("Saving character details: {}", character);
        return this.characterDetailsRepository.save(character);
    }

    @Override
    public CharacterDetailsEntity findById(Long id) {
        logger.info("Finding character details by ID: {}", id);
        CharacterDetailsEntity entity = characterDetailsRepository.findById(id).orElse(null);
        if (entity == null) {
            logger.warn("Character details not found for ID: {}", id);
        }
        return entity;
    }

    @Override
    public CharacterDetailsEntity updateCharacterDetails(final Long id, final CharacterFullModel model) {
        CharacterDetailsEntity entity = this.findById(id);
        if (entity != null) {
            logger.debug("Character details found for update: {}", entity);
            if (model.getDetails() != null) {
                modelMapper.map(model.getDetails(), entity);
                CharacterDetailsEntity updatedEntity = this.characterDetailsRepository.save(entity);
                logger.info("Character details updated for ID: {}", id);
                return updatedEntity;
            } else {
                logger.warn("Character details update failed, details are null for ID: {}", id);
                throw new IllegalArgumentException("Character details update failed, details are null.");
            }
        } else {
            logger.warn("Character details not found for update, ID: {}", id);
            throw new CharacterDetailsNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        logger.info("Deleting character details by ID: {}", id);
        characterDetailsRepository.deleteById(id);
    }

    @Override
    public List<CharacterDetailsModel> getAllCharacters() {
        logger.info("Fetching all characters details.");
        try {
            List<CharacterDetailsModel> characters = characterDetailsRepository.findAll()
                    .parallelStream()
                    .map(entity -> modelMapper.map(entity, CharacterDetailsModel.class))
                    .collect(Collectors.toList());

            logger.debug("Fetched {} characters details.", characters.size());
            return characters;

        } catch (Exception e) {
            logger.info("Empty list when fetching all characters details: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterFullModel> getAllCharactersFull() {
        logger.info("Fetching all characters full details.");
        try {
            final List<CharacterDetailsEntity> detailsEntities = characterDetailsRepository.findAll();
            List<CharacterFullModel> fullModels = detailsEntities.stream().map(detailsEntity -> {
                try {
                    // Récupération des contextes et illustrations (potentiellement en une seule requête)
                    CharacterContextEntity contextEntity = characterContextRepository.findById(detailsEntity.getContextId())
                            .orElseThrow(() -> new RuntimeException("Context not found for character: " + detailsEntity.getId()));
                    CharacterIllustrationEntity illustrationEntity = characterIllustrationRepository.findByImageDetails(detailsEntity)
                            .orElse(null); // Illustration peut être null

                    final CharacterDetailsModel detailsModel = modelMapper.map(detailsEntity, CharacterDetailsModel.class);
                    final CharacterContextModel contextModel = modelMapper.map(contextEntity, CharacterContextModel.class);
                    final CharacterIllustrationModel illustrationModel = illustrationEntity != null ? modelMapper.map(illustrationEntity, CharacterIllustrationModel.class) : null;

                    return new CharacterFullModel(detailsModel, contextModel, illustrationModel);
                } catch (Exception e) {
                    logger.error("Error processing character details: {}, model: {}", detailsEntity.getId(), modelMapper.map(detailsEntity, CharacterDetailsModel.class), e);
                    return null; // ou une valeur par défaut, ou lancez une exception
                }
            }).filter(java.util.Objects::nonNull).collect(Collectors.toList());
            logger.debug("Fetched {} characters full details.", fullModels.size());
            return fullModels;
        } catch (Exception e) {
            logger.info("Empty list when fetching all characters full details: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
