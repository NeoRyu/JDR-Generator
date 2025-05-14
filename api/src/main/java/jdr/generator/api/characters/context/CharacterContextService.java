package jdr.generator.api.characters.context;

import java.util.List;

/** Service interface for managing character context operations. */
public interface CharacterContextService {

  /**
   * Saves a new character context.
   *
   * @param context The CharacterContextEntity to save.
   * @return The saved CharacterContextEntity.
   */
  CharacterContextEntity save(CharacterContextEntity context);

  /**
   * Finds a character context by its ID.
   *
   * @param id The ID of the character context to find.
   * @return The found CharacterContextEntity.
   */
  CharacterContextEntity findById(long id);

  /**
   * Retrieves a list of all character contexts.
   *
   * @return A list of CharacterContextModel.
   */
  List<CharacterContextModel> getAllContexts();

  /**
   * Creates a CharacterContextModel from the provided DefaultContextJson data.
   *
   * @param data The DefaultContextJson containing context information.
   * @return The created CharacterContextModel.
   */
  CharacterContextModel createCharacterContextModel(DefaultContextJson data);
}
