package jdr.generator.api.characters.details;

import jdr.generator.api.characters.CharacterFullModel;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.context.CharacterContextRepository;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationRepository;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataModel;
import jdr.generator.api.characters.stats.CharacterJsonDataService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation of the {@link CharacterDetailsService} interface. */
@Service
public class CharacterDetailsServiceImpl implements CharacterDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(CharacterDetailsServiceImpl.class);

  private final ModelMapper modelMapper;
  private final CharacterContextRepository characterContextRepository;
  private final CharacterDetailsRepository characterDetailsRepository;
  private final CharacterIllustrationRepository characterIllustrationRepository;
  private final CharacterJsonDataService characterJsonDataService;

  /**
   * Constructor for the CharacterDetailsServiceImpl.
   *
   * @param modelMapper The ModelMapper for mapping entities to models.
   * @param characterContextRepository The repository for accessing character context data.
   * @param characterDetailsRepository The repository for accessing character details data.
   * @param characterIllustrationRepository The repository for accessing character illustration data.
   * @param characterJsonDataService The service for accessing character JSON data.
   */
  @Autowired
  public CharacterDetailsServiceImpl(
          final ModelMapper modelMapper,
          final CharacterContextRepository characterContextRepository,
          final CharacterDetailsRepository characterDetailsRepository,
          final CharacterIllustrationRepository characterIllustrationRepository,
          final CharacterJsonDataService characterJsonDataService) {
    this.characterDetailsRepository = characterDetailsRepository;
    this.modelMapper = modelMapper;
    this.characterContextRepository = characterContextRepository;
    this.characterIllustrationRepository = characterIllustrationRepository;
    this.characterJsonDataService = characterJsonDataService;
  }

  /**
   * Saves a new character's details.
   *
   * @param character The CharacterDetailsEntity to save.
   * @return The saved CharacterDetailsEntity.
   */
  @Override
  @Transactional
  public CharacterDetailsEntity save(final CharacterDetailsEntity character) {
    logger.info("Saving character details: {}", character);
    return this.characterDetailsRepository.save(character);
  }

  /**
   * Finds character details by their ID.
   *
   * @param id The ID of the character details to find.
   * @return The found CharacterDetailsEntity, or null if not found.
   */
  @Override
  public CharacterDetailsEntity findById(Long id) {
    logger.info("Finding character details by ID: {}", id);
    CharacterDetailsEntity entity = characterDetailsRepository.findById(id).orElse(null);
    if (entity == null) {
      logger.warn("Character details not found for ID: {}", id);
    }
    return entity;
  }

  /**
   * Updates an existing character's details.
   *
   * @param id The ID of the character to update.
   * @param model The updated character data.
   * @return The updated CharacterDetailsEntity.
   * @throws CharacterDetailsNotFoundException if no character details are found for the given ID.
   * @throws IllegalArgumentException if the provided details in the model are null.
   */
  @Override
  public CharacterDetailsEntity updateCharacterDetails(
          final Long id, final CharacterFullModel model) {
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

  /**
   * Deletes a character by their ID.
   *
   * @param id The ID of the character to delete.
   */
  @Override
  @Transactional
  public void deleteCharacter(Long id) {
    logger.info("Deleting character details by ID: {}", id);
    characterDetailsRepository.deleteById(id);
  }

  /**
   * Retrieves a list of all character details.
   *
   * @return A list of CharacterDetailsModel. Returns an empty list if no characters are found or an
   * error occurs.
   */
  @Override
  public List<CharacterDetailsModel> getAllCharacters() {
    logger.info("Fetching all characters details.");
    try {
      List<CharacterDetailsModel> characters =
              characterDetailsRepository.findAll().parallelStream()
                      .map(entity -> modelMapper.map(entity, CharacterDetailsModel.class))
                      .collect(Collectors.toList());

      logger.debug("Fetched {} characters details.", characters.size());
      return characters;

    } catch (Exception e) {
      logger.info("Empty list when fetching all characters details: {}", e.getMessage());
      return Collections.emptyList();
    }
  }

  /**
   * Retrieves a list of all full character models, including their details, context, illustration,
   * and JSON data.
   *
   * @return A list of CharacterFullModel. Returns an empty list if no characters are found or an
   * error occurs during processing.
   */
  @Override
  @Transactional(readOnly = true)
  public List<CharacterFullModel> getAllCharactersFull() {
    logger.info("Fetching all characters full details.");
    try {
      final List<CharacterDetailsEntity> detailsEntities = characterDetailsRepository.findAll();
      List<CharacterFullModel> fullModels =
              detailsEntities.stream()
                      .map(
                              detailsEntity -> {
                                try {
                                  // Récupération des contextes et illustrations (potentiellement en une seule
                                  // requête)
                                  CharacterContextEntity contextEntity =
                                          characterContextRepository
                                                  .findById(detailsEntity.getContextId())
                                                  .orElseThrow(
                                                          () ->
                                                                  new RuntimeException(
                                                                          "Context not found for character: "
                                                                                  + detailsEntity.getId()));
                                  CharacterIllustrationEntity illustrationEntity =
                                          characterIllustrationRepository
                                                  .findByImageDetails(detailsEntity)
                                                  .orElse(null); // Illustration peut être null
                                  CharacterJsonDataEntity jsonDataEntity =
                                          characterJsonDataService
                                                  .findByCharacterDetailsId(detailsEntity.getId())
                                                  .orElse(null); // JsonData peut être null

                                  final CharacterDetailsModel detailsModel =
                                          modelMapper.map(detailsEntity, CharacterDetailsModel.class);
                                  final CharacterContextModel contextModel =
                                          modelMapper.map(contextEntity, CharacterContextModel.class);
                                  final CharacterIllustrationModel illustrationModel =
                                          illustrationEntity != null
                                                  ? modelMapper.map(
                                                  illustrationEntity, CharacterIllustrationModel.class)
                                                  : null;
                                  final CharacterJsonDataModel jsonDataModel =
                                          jsonDataEntity != null
                                                  ? modelMapper.map(jsonDataEntity, CharacterJsonDataModel.class)
                                                  : null;

                                  return new CharacterFullModel(
                                          detailsModel, contextModel, illustrationModel, jsonDataModel);
                                } catch (Exception e) {
                                  logger.error(
                                          "Error processing character details: {}, model: {}",
                                          detailsEntity.getId(),
                                          modelMapper.map(detailsEntity, CharacterDetailsModel.class),
                                          e);
                                  return null; // ou une valeur par défaut, ou lancez une exception
                                }
                              })
                      .filter(java.util.Objects::nonNull)
                      .collect(Collectors.toList());
      logger.debug("Fetched {} characters full details.", fullModels.size());
      return fullModels;
    } catch (Exception e) {
      logger.info("Empty list when fetching all characters full details: {}", e.getMessage());
      return Collections.emptyList();
    }
  }
}