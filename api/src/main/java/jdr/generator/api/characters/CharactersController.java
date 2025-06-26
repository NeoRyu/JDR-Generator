package jdr.generator.api.characters;

import java.util.List;
import jdr.generator.api.characters.context.DefaultContextJson;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.RegenerateIllustrationRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

/** Controller for managing character-related operations. */
@RestController
@RequestMapping("/characters")
public class CharactersController {
  private static final String webModuleHost = "http://localhost:5173,http://localhost:3080";
  private final CharacterDetailsService characterDetailsService;
  private final PdfGeneratorService pdfGeneratorService;
  private final GeminiService geminiService;
  private final OpenaiService openaiService;
  private final FreepikService freepikService;

  /**
   * Constructor for the CharactersController.
   *
   * @param geminiService Service for interacting with the Gemini API.
   * @param characterDetailsService Service for managing character details.
   * @param openaiService Service for interacting with the OpenAI API.
   * @param freepikService Service for interacting with the Freepik API.
   */
  CharactersController(
      CharacterDetailsService characterDetailsService,
      GeminiService geminiService,
      OpenaiService openaiService,
      FreepikService freepikService,
      PdfGeneratorService pdfGeneratorService) {
    this.characterDetailsService = characterDetailsService;
    this.geminiService = geminiService;
    this.openaiService = openaiService;
    this.freepikService = freepikService;
    this.pdfGeneratorService = pdfGeneratorService;
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
  @CrossOrigin(origins = webModuleHost)
  public CharacterDetailsModel generate(@RequestBody DefaultContextJson data) {
    return geminiService.generate(data);
  }

  /**
   * Regenerates an illustration for an existing character. This method replaces the old POST
   * /illustrate to use PUT and an ID. It takes the character's ID and triggers the regeneration
   * process using the character's existing context and details to build the prompt.
   *
   * @param request The request body containing the character ID and the desired draw style.
   * @return An array of bytes representing the regenerated image.
   */
  @PutMapping("/illustrate")
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = webModuleHost)
  public byte[] illustrate(@RequestBody RegenerateIllustrationRequestDto request) {
    return freepikService.regenerateIllustration(request.getId(), request.getDrawStyle());
    // return openaiService.regenerateIllustration(request.getId(), request.getDrawStyle());
    // return geminiService.regenerateIllustration(request.getId(), request.getDrawStyle());

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
  @CrossOrigin(origins = webModuleHost)
  public String stats(@PathVariable Long id) {
    return geminiService.stats(id);
  }

  /**
   * PDF Generation
   *
   * @param id L'ID du personnage pour lequel générer le PDF.
   * @return byte[] du PDF généré
   */
  @GetMapping(value = "/pdf/generate/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
  @CrossOrigin(origins = webModuleHost)
  public byte[] getCharacterPdf(@PathVariable Long id) {
    return pdfGeneratorService.generateCharacterPdf(id);
  }
}
