package jdr.generator.api.characters;

import java.util.List;
import jdr.generator.api.characters.context.DefaultContextJson;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing character-related operations.
 * */
@RestController
@RequestMapping("/characters")
public class CharactersController {
  private static final String geminiHost = "http://localhost:5173";
  private final GeminiService geminiService;
  private final CharacterDetailsService characterDetailsService;
  private final OpenaiService openaiService;

  /**
   * Constructor for the CharactersController.
   *
   * @param geminiService Service for interacting with the Gemini API.
   * @param characterDetailsService Service for managing character details.
   * @param openaiService Service for interacting with the OpenAI API.
   */
  CharactersController(
      GeminiService geminiService,
      CharacterDetailsService characterDetailsService,
      OpenaiService openaiService) {
    this.geminiService = geminiService;
    this.characterDetailsService = characterDetailsService;
    this.openaiService = openaiService;
  }

  /**
   * Updates an existing character's details.
   *
   * @param id The ID of the character to update.
   * @param updatedCharacter The updated character data.
   * @return The updated CharacterDetailsEntity.
   */
  @PutMapping("/details/{id}")
  @Transactional
  public CharacterDetailsEntity updateCharacter(
      @PathVariable Long id, @RequestBody CharacterFullModel updatedCharacter) {
    return characterDetailsService.updateCharacterDetails(id, updatedCharacter);
  }

  /**
   * Deletes a character by their ID.
   *
   * @param id The ID of the character to delete.
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCharacter(@PathVariable Long id) {
    characterDetailsService.deleteCharacter(id);
  }

  /**
   * Retrieves a list of all character details.
   *
   * @return A list of CharacterDetailsModel.
   */
  @GetMapping
  public List<CharacterDetailsModel> getAllCharacters() {
    return characterDetailsService.getAllCharacters();
  }

  /**
   * Retrieves a list of all full character models.
   *
   * @return A list of CharacterFullModel.
   */
  @GetMapping("/full")
  public List<CharacterFullModel> getAllCharactersFull() {
    return characterDetailsService.getAllCharactersFull();
  }

  /**
   * Generates a new character based on the provided context.
   *
   * @param data The context data for character generation.
   * @return The generated CharacterDetailsModel.
   */
  @RequestMapping(
      value = {"/generate"},
      method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = geminiHost)
  public CharacterDetailsModel generate(@RequestBody DefaultContextJson data) {
    return geminiService.generate(data);
  }

  /**
   * Regenerates an illustration for an existing character.
   * This method replaces the old POST /illustrate to use PUT and an ID.
   * It takes the character's ID and triggers the regeneration process
   * using the character's existing context and details to build the prompt.
   *
   * @param id The ID of the character for which to regenerate the illustration.
   * @return An array of bytes representing the regenerated image.
   */
  @PutMapping("/illustrate/{id}")
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = geminiHost)
  public byte[] illustrate(@PathVariable Long id) {
    return openaiService.regenerateIllustration(id);
    // return geminiService.regenerateIllustration(id);
  }

  /**
   * Retrieves statistics for a specific character.
   *
   * @param id The ID of the character.
   * @return A string containing the character's statistics.
   */
  @RequestMapping(
      value = {"/stats/{id}"},
      method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = geminiHost)
  public String stats(@PathVariable Long id) {
    return geminiService.stats(id);
  }
}
