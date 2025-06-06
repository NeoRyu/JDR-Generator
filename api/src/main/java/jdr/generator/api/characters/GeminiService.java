package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import jdr.generator.api.characters.context.*;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationService;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataModel;
import jdr.generator.api.characters.stats.CharacterJsonDataService;
import jdr.generator.api.config.GeminiGenerationConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service for interacting with the Gemini API for character generation and statistics.
 * */
@Service("geminiService")
@RequiredArgsConstructor
public class GeminiService implements GeminiGenerationConfiguration {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Value("${GEMINI_API_URL}")
  private String hostApiAi;

  private final RestTemplate restTemplate;
  private final CharacterDetailsService characterDetailsService;
  private final CharacterContextService characterContextService;
  private final CharacterIllustrationService characterIllustrationService;
  private final CharacterJsonDataService characterJsonDataService;
  private final ModelMapper modelMapper;

  @SuppressWarnings("SpringQualifierCopyableLombok")
  @Qualifier("openaiService")
  private final OpenaiService openaiService;

  @SuppressWarnings("SpringQualifierCopyableLombok")
  @Qualifier("freepikService")
  private final FreepikService freepikService;

  /**
   * Cleans a JSON string received from the Gemini API.
   *
   * @param jsonString The raw JSON string.
   * @return A cleaned JSON string, or "{}" if cleaning fails.
   */
  private String cleanJsonString(String jsonString) {
    if (jsonString == null) {
      return "{}";
    }
    try {
      JsonNode rootNode = OBJECT_MAPPER.readTree(jsonString);
      JsonNode responseNode = rootNode.get("response");

      if (responseNode == null) {
        // La clé "response" est absente, retourner le JSON tel quel (sans modifications).
        LOGGER.warn("Clé 'response' absente dans la réponse JSON, retournant le JSON tel quel.");
        return jsonString; // Retourner la chaîne JSON originale
      }

      String innerJsonString = responseNode.asText();

      // Vérifier si innerJsonString est un objet JSON valide
      if (innerJsonString.startsWith("{") && innerJsonString.endsWith("}")) {
        // innerJsonString est un objet JSON valide, le retourner tel quel
        return innerJsonString;
      } else {
        // innerJsonString n'est pas un objet JSON valide, tenter de le nettoyer
        JsonNode innerJsonNode = OBJECT_MAPPER.readTree(innerJsonString);

        // Ne jamais supprimer les accolades `{` et `}`
        return innerJsonNode.toString();
      }

    } catch (JsonProcessingException e) {
      LOGGER.warn("JSON invalide lors du nettoyage : {}", jsonString, e);
      return "{}";
    }
  }

  /**
   * Asynchronously generates illustration for a character using the Imagen API (via GeminiService).
   *
   * @param promptDrawStyle The key and prompt defining the overall portrait style to generate
   * @param entity The details of the character for generate the illustration.
   * @return A CompletableFuture representing the completion of the asynchronous operation.
   */
  @Async
  protected CompletableFuture<Void> generateIllustrationAsync(
          IllustrationDrawStyle promptDrawStyle,
          CharacterDetailsEntity entity) {
    LOGGER.info(
        "Démarrage asynchrone de la génération d'illustration via "
            + "Imagen (GeminiService) pour le personnage {{id={}}}",
        entity.getId());
    try {
      final String promptPortrait = promptDrawStyle.getBasePrompt() + " " + entity.getImage();
      final String imgBlob = this.illustrate(promptPortrait);
      if (imgBlob != null) {
        try {
          JsonNode imageNode = OBJECT_MAPPER.readTree(imgBlob).get("image");
          if (imageNode != null) {
            byte[] imageBytes = Base64.getDecoder().decode(imageNode.asText());

            try {
              this.characterIllustrationService.findByCharacterDetailsId(entity.getId());
              this.characterIllustrationService.updateIllustration(
                      entity.getId(), imageBytes, entity.getImage());
              LOGGER.info(
                      "Illustration mise à jour et enregistrée pour {{id={}}}",
                      entity.getId());

            } catch (RuntimeException e) {
              final CharacterIllustrationModel characterIllustrationModel =
                      CharacterIllustrationModel.builder()
                              .imageLabel(entity.getImage())
                              .imageBlob(imageBytes)
                              .imageDetails(entity)
                              .build();
              CharacterIllustrationEntity characterIllustrationEntity = this.modelMapper.map(
                      characterIllustrationModel, CharacterIllustrationEntity.class
              );
              this.characterIllustrationService.save(characterIllustrationEntity);
              LOGGER.info(
                      "Nouvelle illustration générée et enregistrée pour {{id={}}}",
                      entity.getId());
            }
          }
        } catch (JsonProcessingException e) {
          LOGGER.error(
              "Erreur lors de l'analyse du blob d'image pour le personnage {{id={}}} : {}",
              entity.getId(),
              e.getMessage());
        }
      } else {
        LOGGER.warn(
            "La génération d'illustration via Imagen (GeminiService) a retourné "
                + "un blob null pour le personnage {{id={}}}",
            entity.getId());
      }
    } catch (HttpClientErrorException e) {
      LOGGER.error(
          "Erreur lors de l'appel à l'API d'illustration (client) via "
              + "Imagen (GeminiService) pour le personnage {{id={}}} : Status={}, Body={}",
          entity.getId(),
          e.getStatusCode(),
          e.getResponseBodyAsString());
    } catch (HttpServerErrorException e) {
      LOGGER.error(
          "Erreur lors de l'appel à l'API d'illustration (serveur) via "
              + "Imagen (GeminiService) pour le personnage {{id={}}} : Status={}, Body={}",
          entity.getId(),
          e.getStatusCode(),
          e.getResponseBodyAsString());
    } catch (Exception e) {
      LOGGER.error(
          "Erreur inattendue lors de la génération d'illustration via "
              + "Imagen (GeminiService) pour le personnage {{id={}}} : {}",
          entity.getId(),
          e.getMessage());
    }
    return CompletableFuture.completedFuture(null);
  }

  /**
   * Generates character details based on the provided context using the Gemini API.
   *
   * @param data The context data for character generation.
   * @return The generated CharacterDetailsModel.
   * @throws RuntimeException if the Gemini API request fails or if an error parsing the response.
   */
  @Override
  @Transactional
  public CharacterDetailsModel generate(DefaultContextJson data) {
    final String apiUrl = hostApiAi + "/generate";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<DefaultContextJson> request = new HttpEntity<>(data, headers);
    ResponseEntity<String> response =
        restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      LOGGER.error(
          "API Gemini /generate request failed with status code: {}",
              response.getStatusCode());
      throw new RuntimeException("API Gemini /generate request failed");
    }

    CharacterDetailsModel characterDetailsModel;
    CharacterDetailsEntity characterDetailsEntity;

    try {
      // Nettoyage spécifique de la réponse Gemini (VERSION FONCTIONNELLE)
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      String innerJsonString =
          jsonNode
              .get("response")
              .asText()
              .replace("```json", "")
              .replace("\\n", "")
              .replace("\\t", "")
              .replace("\\", "");
      innerJsonString = innerJsonString.substring(1, innerJsonString.length() - 1);

      CharacterContextModel characterContextModel =
          this.characterContextService.createCharacterContextModel(data);
      final CharacterContextEntity characterContextEntity =
          characterContextService.save(
              modelMapper.map(characterContextModel, CharacterContextEntity.class));

      characterDetailsModel = OBJECT_MAPPER.readValue(innerJsonString, CharacterDetailsModel.class);
      characterDetailsModel =
          characterDetailsModel.toBuilder()
              .createdAt(java.util.Date.from(java.time.Instant.now()))
              .updatedAt(java.util.Date.from(java.time.Instant.now()))
              .contextId(characterContextEntity.getId())
              .build();
      characterDetailsEntity =
          characterDetailsService.save(
              modelMapper.map(characterDetailsModel, CharacterDetailsEntity.class));

      LOGGER.info(
          "Created CharacterModel {{id={}}} :: {} [{} {{idContext={}}}]",
          characterDetailsEntity.getId(),
          characterDetailsModel.name,
          characterContextModel.promptGender,
          characterContextEntity.getId());

      CharacterIllustrationEntity illustrationEntity = new CharacterIllustrationEntity();
      illustrationEntity.setImageDetails(characterDetailsEntity);
      illustrationEntity.setImageLabel(characterDetailsEntity.getImage());
      illustrationEntity = characterIllustrationService.save(illustrationEntity);
      LOGGER.info("Initial illustration entity created for character ID: {}. ID: {}",
              characterDetailsEntity.getId(), illustrationEntity.getId());

      // Lancement asynchrone de la génération de l'illustration via une IA générative
      final IllustrationDrawStyle promptDrawStyle =
              IllustrationDrawStyle.fromKeyOrDefault(data.getPromptDrawStyle());
      this.freepikService.generateIllustrationAsync(promptDrawStyle, characterDetailsEntity);
      this.generateIllustrationAsync(promptDrawStyle, characterDetailsEntity);
      this.openaiService.generateIllustrationAsync(promptDrawStyle, characterDetailsEntity);

      String statsJson = this.stats(characterDetailsEntity.getId());
      LOGGER.info("Stats JSON generated by GeminiService: {}", statsJson);
      try {
        String cleanedJson = cleanJsonString(statsJson);
        LOGGER.info("Cleaned Stats JSON: {}", cleanedJson);
        this.saveCharacterJsonData(characterDetailsEntity.getId(), cleanedJson);
        LOGGER.info("Character JSON stats data saved successfully");
      } catch (JsonProcessingException e) {
        LOGGER.error(
            "Erreur lors de l'analyse du JSON stats généré par GeminiService : {}",
                e.getMessage());
        this.saveCharacterJsonData(
            characterDetailsEntity.getId(), statsJson.isEmpty() ? "{}" : statsJson);
        LOGGER.warn("Character JSON stats data saved with raw JSON due to parsing error.");
      }

      // ScalaMessage.main(new String[]{});

      return characterDetailsModel;
    } catch (JsonProcessingException e) {
      LOGGER.error("Error parsing JSON response from Gemini: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates an illustration based on the provided prompt using the Gemini API.
   *
   * @param promptPortrait The complete prompt for the image generation.
   * @return A JSON string containing the illustration data.
   * @throws RuntimeException if the Gemini API request fails.
   */
  @Override
  public String illustrate(String promptPortrait) {
    final String apiUrl = hostApiAi + "illustrate";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    JSONObject requestBody = new JSONObject();
    requestBody.put("prompt", promptPortrait);
    HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
    ResponseEntity<String> response =
        restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      LOGGER.error("API illustrate request failed with status code: {}",
              response.getStatusCode());
      throw new RuntimeException("API illustrate request failed");
    }

    LOGGER.info("Illustration API response: {}", response.getBody());
    return response.getBody();
  }

  /**
   * Regenerates an illustration for an existing character based on its stored details,
   * similar to OpenaiService.
   *
   * @param id The ID of the character for which to regenerate the illustration.
   * @return An array of bytes representing the regenerated image.
   * @throws RuntimeException if the illustration regeneration fails.
   */
  @Transactional
  public byte[] regenerateIllustration(Long id) {
    LOGGER.info("Regenerating illustration via Gemini (Imagen) for character ID: {}", id);
    try {
      CharacterDetailsEntity characterDetails = characterDetailsService.findById(id);
      CharacterContextEntity characterContext = characterContextService.findById(id);
      final IllustrationDrawStyle promptDrawStyle =
              IllustrationDrawStyle.fromKeyOrDefault(characterContext.getPromptDrawStyle());
      final String promptPortrait = promptDrawStyle.getBasePrompt()
              + " " + characterDetails.getImage();
      final String imgBlobResponse = illustrate(promptPortrait);
      if (imgBlobResponse == null) {
        LOGGER.warn("Image generation via Imagen (GeminiService) "
                + "returned a null blob for character {{id={}}}", id);
        throw new RuntimeException("Image generation returned null.");
      }

      JsonNode imageNode = OBJECT_MAPPER.readTree(imgBlobResponse).get("image");
      if (imageNode == null) {
        LOGGER.error("Image node not found in response for character {{id={}}}", id);
        throw new RuntimeException("Image data not found in API response.");
      }
      byte[] newImageBlob = Base64.getDecoder().decode(imageNode.asText());

      // Update the existing illustration
      characterIllustrationService.updateIllustration(
              id, newImageBlob, characterDetails.getImage());
      LOGGER.info("Illustration regenerated and updated for character {{id={}}}", id);

      return newImageBlob;

    } catch (HttpClientErrorException e) {
      LOGGER.error(
              "Erreur lors de l'appel à l'API d'illustration (client) via "
                      + "Imagen (GeminiService) pour la régénération du "
                      + "portrait du personnage {{id={}}} : Status={}, Body={}",
              id, e.getStatusCode(), e.getResponseBodyAsString());
      throw new RuntimeException(
              "Erreur client lors de la régénération de l'illustration: "
                      + e.getResponseBodyAsString(), e);
    } catch (HttpServerErrorException e) {
      LOGGER.error(
              "Erreur lors de l'appel à l'API d'illustration (serveur) via "
                      + "Imagen (GeminiService) pour la régénération du personnage "
                      + "{{id={}}} : Status={}, Body={}",
              id, e.getStatusCode(), e.getResponseBodyAsString());
      throw new RuntimeException(
              "Erreur serveur lors de la régénération de l'illustration: "
                      + e.getResponseBodyAsString(), e);
    } catch (JsonProcessingException e) {
      LOGGER.error(
              "Erreur lors de l'analyse du blob d'image pour la "
                      + "régénération du personnage {{id={}}} : {}",
              id, e.getMessage());
      throw new RuntimeException("Erreur lors de l'analyse de la réponse d'illustration.", e);
    } catch (Exception e) {
      LOGGER.error(
              "Erreur inattendue lors de la régénération d'illustration via "
                      + "Imagen (GeminiService) pour le personnage {{id={}}} : {}",
              id, e.getMessage());
      throw new RuntimeException("Erreur inattendue lors de la régénération de l'illustration.", e);
    }
  }

  /**
   * Retrieves character statistics from the Gemini API for a given character ID.
   *
   * @param characterId The ID of the character.
   * @return A JSON string containing the character's statistics.
   * @throws RuntimeException if the Gemini API request fails.
   */
  @Override
  public String stats(Long characterId) {
    CharacterDetailsEntity character = this.characterDetailsService.findById(characterId);
    CharacterContextEntity context =
        this.characterContextService.findById(character.getContextId());
    String data =
        "promptSystem: '"
            + context.getPromptSystem()
            + "'\n"
            + "promptRace: '"
            + context.getPromptRace()
            + "'\n"
            + "promptGender: '"
            + context.getPromptGender()
            + "'\n"
            + "promptClass: '"
            + context.getPromptClass()
            + "'\n"
            + "promptDescription: '"
            + context.getPromptDescription()
            + "'\n"
            + "name: '"
            + character.getName()
            + "'\n"
            + "age: '"
            + character.getAge()
            + "'\n"
            + "education: '"
            + character.getEducation()
            + "'\n"
            + "profession: '"
            + character.getProfession()
            + "'\n"
            + "reasonForProfession: '"
            + character.getReasonForProfession()
            + "'\n"
            + "workPreferences: '"
            + character.getWorkPreferences()
            + "'\n"
            + "changeInSelf: '"
            + character.getChangeInSelf()
            + "'\n"
            + "changeInWorld: '"
            + character.getChangeInWorld()
            + "'\n"
            + "goal: '"
            + character.getGoal()
            + "'\n"
            + "reasonForGoal: '"
            + character.getReasonForGoal()
            + "'\n";

    final String apiUrl = hostApiAi + "/stats";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    JSONObject requestBody = new JSONObject();
    requestBody.put("data", data);
    HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
    ResponseEntity<String> response =
        restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      LOGGER.error(
          "API Gemini /stats request failed with status code: {}",
              response.getStatusCode());
      throw new RuntimeException("API Gemini /stats request failed");
    }

    LOGGER.info("Statistiques API response (from Gemini): {}", response.getBody());
    return response.getBody();
  }

  /**
   * Saves the character's JSON statistics data.
   *
   * @param characterDetailsId The ID of the character details.
   * @param jsonData The JSON data to save.
   * @throws JsonProcessingException if there is an error processing the JSON data.
   */
  private void saveCharacterJsonData(Long characterDetailsId, String jsonData)
      throws JsonProcessingException {
    final CharacterJsonDataModel jsonDataModel =
        CharacterJsonDataModel.builder()
            .characterDetailsId(characterDetailsId)
            .jsonData(jsonData)
            .createdAt(java.util.Date.from(java.time.Instant.now()))
            .updatedAt(java.util.Date.from(java.time.Instant.now()))
            .build();
    CharacterJsonDataEntity jsonDataEntity =
        modelMapper.map(jsonDataModel, CharacterJsonDataEntity.class);
    characterJsonDataService.save(jsonDataEntity);
  }
}
