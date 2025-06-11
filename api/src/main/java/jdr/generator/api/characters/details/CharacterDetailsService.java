package jdr.generator.api.characters.details;

import java.util.List;
import jdr.generator.api.characters.CharacterFullModel;

/**
 * Service interface for managing character details operations.
 * */
public interface CharacterDetailsService {

  /**
   * Saves a new character's details.
   *
   * @param entity The CharacterDetailsEntity to save.
   * @return The saved CharacterDetailsEntity.
   */
  CharacterDetailsEntity save(CharacterDetailsEntity entity);
  CharacterDetailsEntity saveAndFlush(CharacterDetailsEntity entity);
  /**
   * Finds character details by their ID.
   *
   * @param id The ID of the character details to find.
   * @return The found CharacterDetailsEntity.
   */
  CharacterDetailsEntity findById(Long id);

  /**
   * Updates an existing character's details.
   *
   * @param id The ID of the character to update.
   * @param updatedCharacter The updated character data.
   * @return The updated CharacterDetailsEntity.
   */
  CharacterDetailsEntity updateCharacterDetails(Long id, CharacterFullModel updatedCharacter);

  /**
   * Deletes a character by their ID.
   *
   * @param id The ID of the character to delete.
   */
  void deleteCharacter(Long id);

  /**
   * Retrieves a list of all character details.
   *
   * @return A list of CharacterDetailsModel.
   */
  List<CharacterDetailsModel> getAllCharacters();

  /**
   * Retrieves a list of all full character models.
   *
   * @return A list of CharacterFullModel.
   */
  List<CharacterFullModel> getAllCharactersFull();
}
