package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/** Service for interacting with the OpenAI API for character generation and statistics. */
@Service("openaiService")
@RequiredArgsConstructor
public class OpenaiService implements GeminiGenerationConfiguration {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${OPENAI_API_URL}")
    private String hostApiAi;

    private final RestTemplate restTemplate;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterContextService characterContextService;
    private final CharacterIllustrationService characterIllustrationService;
    private final CharacterJsonDataService characterJsonDataService;
    private final ModelMapper modelMapper;

    /**
     * Validates if a given JSON string is well-formed.
     *
     * @param jsonString The JSON string to validate.
     * @return The original JSON string if valid, or "{}" if invalid.
     */
    private String cleanJsonString(String jsonString) {
        if (jsonString == null) {
            return "{}";
        }
        try {
            OBJECT_MAPPER.readTree(jsonString); // Tentative de parsing pour vérifier la validité
            return jsonString;
        } catch (JsonProcessingException e) {
            LOGGER.warn("La chaîne '{}' n'est pas un JSON valide : {}", jsonString, e.getMessage());
            return "{}";
        }
    }

    /**
     * Asynchronously generates an illustration for a character using the OpenAI API.
     *
     * @param promptDrawStyle The key and prompt defining the overall portrait style to generate
     * @param entity The details of the character for whom to generate the illustration.
     * @return A CompletableFuture representing the completion of the asynchronous operation.
     */
    @Async
    protected CompletableFuture<Void> generateIllustrationAsync(
            IllustrationDrawStyle promptDrawStyle, CharacterDetailsEntity entity) {
        LOGGER.info(
                "Démarrage asynchrone de la génération d'illustration via "
                        + "OpenaiService pour le personnage {{id={}}}",
                entity.getId());
        try {
            final String promptPortrait = promptDrawStyle.getBasePrompt() + " " + entity.getImage();
            final byte[] imageBytes = this.illustrate(promptPortrait);
            if (imageBytes != null && imageBytes.length > 0) {
                try {
                    this.characterIllustrationService.findByCharacterDetailsId(entity.getId());
                    this.characterIllustrationService.updateIllustration(
                            entity.getId(), imageBytes, entity.getImage());
                    LOGGER.info(
                            "Illustration mise à jour et enregistrée pour le personnage {{id={}}}",
                            entity.getId());
                } catch (RuntimeException e) {
                    final CharacterIllustrationModel characterIllustrationModel =
                            CharacterIllustrationModel.builder()
                                    .imageLabel(entity.getImage())
                                    .imageBlob(imageBytes)
                                    .characterDetails(entity)
                                    .build();
                    CharacterIllustrationEntity characterIllustrationEntity =
                            this.modelMapper.map(
                                    characterIllustrationModel, CharacterIllustrationEntity.class);
                    this.characterIllustrationService.save(characterIllustrationEntity);
                    LOGGER.info(
                            "Illustration générée et enregistrée pour le personnage {{id={}}}",
                            entity.getId());
                }
            } else {
                LOGGER.warn(
                        "La génération d'illustration via OpenaiService a retourné un "
                                + "blob null ou vide pour le personnage {{id={}}}",
                        entity.getId());
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error(
                    "Erreur lors de l'appel à l'API d'illustration (client) via "
                            + "OpenaiService pour le personnage {{id={}}} : Status={}, Body={}",
                    entity.getId(),
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            LOGGER.error(
                    "Erreur lors de l'appel à l'API d'illustration (serveur) via "
                            + "OpenaiService pour le personnage {{id={}}} : Status={}, Body={}",
                    entity.getId(),
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error(
                    "Erreur inattendue lors de la génération d'illustration via "
                            + "OpenaiService pour le personnage {{id={}}} : {}",
                    entity.getId(),
                    e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Generates character details based on the provided context using the OpenAI API.
     *
     * @param data The context data for character generation.
     * @return The generated CharacterDetailsModel.
     * @throws RuntimeException if there is an error communicating with the OpenAI API or processing
     *     the response.
     */
    @Override
    @Transactional
    public CharacterDetailsModel generate(DefaultContextJson data) {
        final String generateApiUrl =
                hostApiAi + (hostApiAi.endsWith("/") ? "generate" : "/generate");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DefaultContextJson> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /generate at {}", generateApiUrl);
            response =
                    restTemplate.exchange(generateApiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /generate API response status: {}", response.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error calling OpenAI /generate API: {}", e.getMessage(), e);
            throw new RuntimeException(
                    "Error communicating with OpenAI API for text generation", e);
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            String errorBody = response.getBody() != null ? response.getBody() : "No response body";
            LOGGER.error(
                    "OpenAI /generate API request failed with status code: {} and body: {}",
                    response.getStatusCode(),
                    errorBody);
            throw new RuntimeException(
                    "OpenAI /generate API request failed with status code: "
                            + response.getStatusCode());
        }

        CharacterDetailsModel characterDetailsModel;
        String statsJson;
        try {
            String innerJsonString = cleanJsonString(response.getBody());
            if (innerJsonString.trim().isEmpty() || "{}".equals(innerJsonString.trim())) {
                LOGGER.error("OpenAI /generate API returned empty or invalid data after cleaning.");
                throw new RuntimeException("OpenAI /generate API returned empty or invalid data.");
            }
            LOGGER.debug("Generated Background JSON (cleaned): {}", innerJsonString);

            CharacterContextModel characterContextModel =
                    this.characterContextService.createCharacterContextModel(data);
            final CharacterContextEntity characterContextEntity =
                    characterContextService.save(
                            modelMapper.map(characterContextModel, CharacterContextEntity.class));
            LOGGER.info("Character context saved with ID: {}", characterContextEntity.getId());

            characterDetailsModel =
                    OBJECT_MAPPER.readValue(innerJsonString, CharacterDetailsModel.class);
            characterDetailsModel =
                    characterDetailsModel.toBuilder()
                            .createdAt(Date.from(Instant.now()))
                            .updatedAt(Date.from(Instant.now()))
                            .contextId(characterContextEntity.getId())
                            .build();
            final CharacterDetailsEntity characterDetailsEntity =
                    characterDetailsService.save(
                            modelMapper.map(characterDetailsModel, CharacterDetailsEntity.class));
            LOGGER.info(
                    "Character details saved with ID: {} and name: {}",
                    characterDetailsEntity.getId(),
                    characterDetailsModel.name);

            LOGGER.info(
                    "Created CharacterModel {{id={}}} :: {} [{} {{idContext={}}}]",
                    characterDetailsEntity.getId(),
                    characterDetailsModel.name,
                    characterContextModel.promptGender,
                    characterContextEntity.getId());

            // Lancement asynchrone de la génération de l'illustration via ce service
            final IllustrationDrawStyle promptDrawStyle =
                    IllustrationDrawStyle.fromKeyOrDefault(data.getPromptDrawStyle());
            generateIllustrationAsync(promptDrawStyle, characterDetailsEntity);

            LOGGER.info(
                    "Calling OpenAI module /stats via OpenaiService.stats for char ID: {}",
                    characterDetailsEntity.getId());
            try {
                statsJson = this.stats(characterDetailsEntity.getId());
                LOGGER.debug("Generated Stats JSON: {}", statsJson);

                if (statsJson != null && !statsJson.trim().isEmpty()) {
                    try {
                        OBJECT_MAPPER.readTree(statsJson);
                        this.saveCharacterJsonData(characterDetailsEntity, statsJson);
                        LOGGER.info(
                                "Character JSON stats data saved successfully for ID: {}",
                                characterDetailsEntity.getId());
                    } catch (JsonProcessingException e) {
                        LOGGER.error(
                                "Erreur lors de l'analyse du JSON stats généré par OpenAI : {}",
                                e.getMessage());
                        LOGGER.warn("Saving raw stats JSON due to parsing error: {}", statsJson);
                        try {
                            this.saveCharacterJsonData(
                                    characterDetailsEntity,
                                    !statsJson.isEmpty() ? statsJson : "{}");
                        } catch (Exception saveRawError) {
                            LOGGER.error(
                                    "Error saving raw stats JSON for ID {}",
                                    characterDetailsEntity.getId(),
                                    saveRawError);
                        }
                        throw new RuntimeException(
                                "Error parsing generated stats JSON from OpenAI", e);

                    } catch (Exception e) {
                        LOGGER.error(
                                "Error saving character JSON data for ID {}",
                                characterDetailsEntity.getId(),
                                e);
                        throw new RuntimeException("Error saving character JSON data", e);
                    }
                } else {
                    LOGGER.warn(
                            "No stats JSON received from OpenAI module /stats, "
                                    + "skipping save for ID {}.",
                            characterDetailsEntity.getId());
                }
            } catch (Exception e) {
                LOGGER.error(
                        "Error during OpenAI module /stats call for character ID {}",
                        characterDetailsEntity.getId(),
                        e);
                throw new RuntimeException("Error during stats generation process", e);
            }

            return characterDetailsModel;

        } catch (JsonProcessingException e) {
            LOGGER.error(
                    "Error processing JSON response from OpenAI /generate: {}", e.getMessage());
            throw new RuntimeException("Error processing JSON response from OpenAI /generate", e);
        } catch (RuntimeException e) {
            LOGGER.error(
                    "RuntimeException during OpenaiService orchestration: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error during OpenaiService orchestration", e);
            throw new RuntimeException("Unexpected error during OpenaiService orchestration", e);
        }
    }

    /**
     * Generates an image based on the provided prompt using the OpenAI API.
     *
     * @param promptPortrait The prompt for the image generation.
     * @return A byte array representing the generated image, or null if an error occurs.
     * @throws RuntimeException if there is an error communicating with the OpenAI API.
     */
    @Override
    public byte[] illustrate(String promptPortrait) {
        final String apiUrl = hostApiAi + (hostApiAi.endsWith("/") ? "illustrate" : "/illustrate");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", promptPortrait);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /illustrate at {}", apiUrl);
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /illustrate API response status: {}", response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                String errorBody =
                        response.getBody() != null ? response.getBody() : "No response body";
                LOGGER.error(
                        "OpenAI /illustrate API request failed with status code: {} and body: {}",
                        response.getStatusCode(),
                        errorBody);
                throw new RuntimeException(
                        "OpenAI /illustrate API request failed with status code: "
                                + response.getStatusCode());
            }

            try {
                JsonNode rootNode = OBJECT_MAPPER.readTree(response.getBody());
                JsonNode imageNode = rootNode.get("image");
                if (imageNode != null && imageNode.isTextual()) {
                    LOGGER.info("Illustration API response received and parsed successfully.");
                    return Base64.getDecoder().decode(imageNode.asText());
                } else {
                    LOGGER.error(
                            "OpenAI module /illustrate returned JSON without "
                                    + "valid 'image' field for prompt: {}",
                            promptPortrait);
                    return null;
                }
            } catch (JsonProcessingException e) {
                LOGGER.error(
                        "Error parsing JSON response from OpenAI /illustrate: {}", e.getMessage());
                return null;
            } catch (IllegalArgumentException e) {
                LOGGER.error(
                        "Error decoding Base64 image data received from OpenAI module: {}",
                        e.getMessage());
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("Error during OpenAI module /illustrate API call", e);
            throw new RuntimeException("Error during OpenAI module /illustrate API call", e);
        }
    }

    /**
     * Regenerates an illustration for an existing character based on its stored details.
     *
     * @param id The ID of the character for which to regenerate the illustration.
     * @param newDrawStyle An optional new drawing style to use for the illustration. If null or
     *     empty,
     * @return An array of bytes representing the regenerated image.
     */
    @Transactional
    public byte[] regenerateIllustration(Long id, String newDrawStyle) {
        LOGGER.info("Regenerating illustration for character ID: {}", id);
        try {
            CharacterDetailsEntity characterDetails = characterDetailsService.findById(id);
            CharacterContextEntity characterContext = characterContextService.findById(id);

            final IllustrationDrawStyle promptDrawStyle =
                    (newDrawStyle != null && !newDrawStyle.isEmpty())
                            ? IllustrationDrawStyle.fromKeyOrDefault(newDrawStyle)
                            : IllustrationDrawStyle.fromKeyOrDefault(
                                    characterContext.getPromptDrawStyle());
            final String promptPortrait =
                    promptDrawStyle.getBasePrompt() + " " + characterDetails.getImage();
            byte[] newImageBlob = illustrate(promptPortrait);
            characterIllustrationService.updateIllustration(
                    id, newImageBlob, characterDetails.getImage());

            // Mise à jour du style choisi par l'utilisateur
            characterContext.setPromptDrawStyle(newDrawStyle);
            characterContextService.saveAndFlush(characterContext);

            return newImageBlob;

        } catch (Exception e) {
            LOGGER.error("Error during illustration regeneration for character ID: {}", id, e);
            throw new RuntimeException("Error during illustration regeneration", e);
        }
    }

    /**
     * Retrieves character statistics from the OpenAI API for a given character ID.
     *
     * @param characterDetailsId The ID of the character details.
     * @return A JSON string containing the character's statistics.
     * @throws RuntimeException if there is an error communicating with the OpenAI API or if
     *     character details or context are not found.
     */
    @Override
    public String stats(Long characterDetailsId) {
        LOGGER.info(
                "Preparing data for OpenAI module /stats for character ID: {}", characterDetailsId);

        CharacterDetailsEntity characterDetailsEntity =
                characterDetailsService.findById(characterDetailsId);
        if (characterDetailsEntity == null) {
            LOGGER.error(
                    "Character details entity not found for stats call for ID: {}",
                    characterDetailsId);
            throw new RuntimeException("Character details not found for ID: " + characterDetailsId);
        }

        Long contextId = characterDetailsEntity.getContextId();
        CharacterContextEntity characterContextEntity = characterContextService.findById(contextId);
        if (characterContextEntity == null) {
            LOGGER.error("Character context entity not found for stats call for ID: {}", contextId);
            throw new RuntimeException("Character context not found for ID: " + contextId);
        }

        String dataToSend =
                "promptSystem: '"
                        + characterContextEntity.getPromptSystem()
                        + "'\n"
                        + "promptRace: '"
                        + characterContextEntity.getPromptRace()
                        + "'\n"
                        + "promptGender: '"
                        + characterContextEntity.getPromptGender()
                        + "'\n"
                        + "promptClass: '"
                        + characterContextEntity.getPromptClass()
                        + "'\n"
                        + "promptDescription: '"
                        + characterContextEntity.getPromptDescription()
                        + "'\n"
                        + "name: '"
                        + characterDetailsEntity.getName()
                        + "'\n"
                        + "age: '"
                        + characterDetailsEntity.getAge()
                        + "'\n"
                        + "education: '"
                        + characterDetailsEntity.getEducation()
                        + "'\n"
                        + "profession: '"
                        + characterDetailsEntity.getProfession()
                        + "'\n"
                        + "reasonForProfession: '"
                        + characterDetailsEntity.getReasonForProfession()
                        + "'\n"
                        + "workPreferences: '"
                        + characterDetailsEntity.getWorkPreferences()
                        + "'\n"
                        + "changeInSelf: '"
                        + characterDetailsEntity.getChangeInSelf()
                        + "'\n"
                        + "changeInInSelf: '"
                        + characterDetailsEntity.getChangeInSelf()
                        + "'\n"
                        + "changeInWorld: '"
                        + characterDetailsEntity.getChangeInWorld()
                        + "'\n"
                        + "goal: '"
                        + characterDetailsEntity.getGoal()
                        + "'\n"
                        + "reasonForGoal: '"
                        + characterDetailsEntity.getReasonForGoal()
                        + "'\n";

        final String apiUrl = hostApiAi + (hostApiAi.endsWith("/") ? "stats" : "/stats");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("data", dataToSend);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /stats at {}", apiUrl);
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /stats API response status: {}", response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                String errorBody =
                        response.getBody() != null ? response.getBody() : "No response body";
                LOGGER.error(
                        "OpenAI /stats API request failed with status code: {} and body: {}",
                        response.getStatusCode(),
                        errorBody);
                throw new RuntimeException(
                        "OpenAI /stats API request failed with status code: "
                                + response.getStatusCode());
            }

            LOGGER.info(
                    "Statistiques API response received from OpenAI module: {}",
                    response.getBody());
            return response.getBody();

        } catch (Exception e) {
            LOGGER.error("Error during OpenAI module /stats API call", e);
            throw new RuntimeException("Error during OpenAI module /stats API call", e);
        }
    }

    /**
     * Saves the character's JSON data to the database.
     *
     * @param detailsEntity The ID of the character details.
     * @param jsonData The JSON data to be saved.
     */
    private void saveCharacterJsonData(CharacterDetailsEntity detailsEntity, String jsonData) {
        try {
            final CharacterJsonDataModel jsonDataModel =
                    CharacterJsonDataModel.builder()
                            .characterDetails(detailsEntity)
                            .jsonData(jsonData)
                            .createdAt(Date.from(Instant.now()))
                            .updatedAt(Date.from(Instant.now()))
                            .build();
            CharacterJsonDataEntity jsonDataEntity =
                    modelMapper.map(jsonDataModel, CharacterJsonDataEntity.class);
            characterJsonDataService.save(jsonDataEntity);
            LOGGER.info("Character JSON data saved successfully for ID: {}", detailsEntity);
        } catch (Exception e) {
            LOGGER.error("Error saving character JSON data for ID {}", detailsEntity, e);
            throw new RuntimeException("Error saving character JSON data", e);
        }
    }
}
