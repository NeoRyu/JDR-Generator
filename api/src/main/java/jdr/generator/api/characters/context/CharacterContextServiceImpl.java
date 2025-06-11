package jdr.generator.api.characters.context;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link CharacterContextService} interface.
 * */
@Service
public class CharacterContextServiceImpl implements CharacterContextService {
  private static final Logger LOGGER = LogManager.getLogger();

  private final CharacterContextRepository characterContextRepository;
  private final ModelMapper modelMapper;

  /**
   * Constructor for the CharacterContextServiceImpl.
   *
   * @param characterContextRepository The repository for accessing character context data.
   * @param modelMapper The ModelMapper for mapping entities to models.
   */
  @Autowired
  public CharacterContextServiceImpl(
      final CharacterContextRepository characterContextRepository, ModelMapper modelMapper) {
    this.characterContextRepository = characterContextRepository;
    this.modelMapper = modelMapper;
  }

  /**
   * Saves a new character context.
   *
   * @param entity The CharacterContextEntity to save.
   * @return The saved CharacterContextEntity.
   */
  @Override
  @Transactional
  public CharacterContextEntity save(CharacterContextEntity entity) {
    LOGGER.info("Saving context: {}", entity);
    try {
      return this.characterContextRepository.save(entity);
    } catch (Exception e) {
      LOGGER.error("Error saving context: {}", entity, e);
      throw e;
    }
  }

  @Transactional
  public CharacterContextEntity saveAndFlush(CharacterContextEntity entity) {
    LOGGER.info("Saving and flush context: {}", entity);
    try {
      return this.characterContextRepository.saveAndFlush(entity);
    } catch (Exception e) {
      LOGGER.error("Error saving and flush context: {}", entity, e);
      throw e;
    }
  }

  /**
   * Finds a character context by its ID.
   *
   * @param id The ID of the character context to find.
   * @return The found CharacterContextEntity.
   * @throws RuntimeException if the context is not found.
   */
  @Override
  public CharacterContextEntity findById(long id) {
    LOGGER.info("Context findById: {}", id);
    return this.characterContextRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Context not found with id: " + id));
  }

  /**
   * Retrieves a list of all character contexts.
   *
   * @return A list of CharacterContextModel.
   */
  @Override
  public List<CharacterContextModel> getAllContexts() {
    LOGGER.info("Fetching all contexts.");
    return this.characterContextRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, CharacterContextModel.class))
        .collect(Collectors.toList());
  }

  /**
   * Creates a CharacterContextModel from the provided DefaultContextJson data.
   *
   * @param data The DefaultContextJson containing context information.
   * @return The created CharacterContextModel.
   */
  @Override
  public CharacterContextModel createCharacterContextModel(DefaultContextJson data) {
    LOGGER.info("Creating character context model from data: {}", data);
    CharacterContextModel characterContextModel = new CharacterContextModel();
    characterContextModel.promptSystem = data.getPromptSystem();
    characterContextModel.promptRace = data.getPromptRace();
    characterContextModel.promptGender = data.getPromptGender();
    characterContextModel.promptClass = data.getPromptClass();
    characterContextModel.promptDrawStyle = data.getPromptDrawStyle();
    characterContextModel.promptDescription = data.getPromptDescription();
    characterContextModel.createdAt = java.util.Date.from(java.time.Instant.now());
    return characterContextModel;
  }
}
