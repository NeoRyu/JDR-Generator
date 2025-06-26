package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextService;
import jdr.generator.api.characters.context.IllustrationDrawStyle;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/** Service for interacting with the Freepik API for character generation and statistics. */
@Service("freepikService")
@RequiredArgsConstructor
public class FreepikService {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${FREEPIK_API_URL}")
    private String hostApiAi;

    private final RestTemplate restTemplate;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterContextService characterContextService;
    private final CharacterIllustrationService characterIllustrationService;
    private final ModelMapper modelMapper;

    /**
     * Asynchronously generates an illustration for a character using the Freepik API.
     *
     * @param promptDrawStyle The key and prompt defining the overall portrait style to generate
     * @param detailsEntity The details of the character for whom to generate the illustration.
     * @return A CompletableFuture representing the completion of the asynchronous operation.
     */
    @Async
    protected CompletableFuture<Void> generateIllustrationAsync(
            IllustrationDrawStyle promptDrawStyle, CharacterDetailsEntity detailsEntity) {
        LOGGER.info(
                "Démarrage asynchrone de la génération d'illustration via "
                        + "FreepikService pour le personnage {{id={}}}",
                detailsEntity.getId());
        try {
            final String promptPortrait =
                    promptDrawStyle.getBasePrompt() + " " + detailsEntity.getImage();
            final byte[] imageBytes = this.illustrate(promptPortrait);
            if (imageBytes != null && imageBytes.length > 0) {
                try {
                    this.characterIllustrationService.findByCharacterDetailsId(
                            detailsEntity.getId());
                    this.characterIllustrationService.updateIllustration(
                            detailsEntity.getId(), imageBytes, detailsEntity.getImage());
                    LOGGER.info(
                            "Illustration mise à jour et enregistrée pour le personnage {{id={}}}",
                            detailsEntity.getId());
                } catch (RuntimeException e) {
                    final CharacterIllustrationModel characterIllustrationModel =
                            CharacterIllustrationModel.builder()
                                    .imageLabel(detailsEntity.getImage())
                                    .imageBlob(imageBytes)
                                    .characterDetails(detailsEntity)
                                    .build();
                    CharacterIllustrationEntity characterIllustrationEntity =
                            this.modelMapper.map(
                                    characterIllustrationModel, CharacterIllustrationEntity.class);
                    this.characterIllustrationService.save(characterIllustrationEntity);
                    LOGGER.info(
                            "Illustration générée et enregistrée pour le personnage {{id={}}}",
                            detailsEntity.getId());
                }
            } else {
                LOGGER.warn(
                        "La génération d'illustration via FreepikService a retourné un "
                                + "blob null ou vide pour le personnage {{id={}}}",
                        detailsEntity.getId());
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error(
                    "Erreur lors de l'appel à l'API d'illustration (client) via "
                            + "FreepikService pour le personnage {{id={}}} : Status={}, Body={}",
                    detailsEntity.getId(),
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            LOGGER.error(
                    "Erreur lors de l'appel à l'API d'illustration (serveur) via "
                            + "FreepikService pour le personnage {{id={}}} : Status={}, Body={}",
                    detailsEntity.getId(),
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error(
                    "Erreur inattendue lors de la génération d'illustration via "
                            + "FreepikService pour le personnage {{id={}}} : {}",
                    detailsEntity.getId(),
                    e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Generates an image based on the provided prompt using the Freepik API.
     *
     * @param promptPortrait The complete prompt for the image generation.
     * @return A byte array representing the generated image, or null if an error occurs.
     * @throws RuntimeException if there is an error communicating with the Freepik API.
     */
    public byte[] illustrate(String promptPortrait) {
        final String apiUrl = hostApiAi + (hostApiAi.endsWith("/") ? "illustrate" : "/illustrate");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", promptPortrait);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling Freepik module /illustrate at {}", apiUrl);
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("Freepik /illustrate API response status: {}", response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                String errorBody =
                        response.getBody() != null ? response.getBody() : "No response body";
                LOGGER.error(
                        "Freepik /illustrate API request failed with status code: {} and body: {}",
                        response.getStatusCode(),
                        errorBody);
                throw new RuntimeException(
                        "Freepik /illustrate API request failed with status code: "
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
                            "Freepik module /illustrate returned JSON without "
                                    + "valid 'image' field for prompt: {}",
                            promptPortrait);
                    return null;
                }
            } catch (JsonProcessingException e) {
                LOGGER.error(
                        "Error parsing JSON response from Freepik /illustrate: {}", e.getMessage());
                return null;
            } catch (IllegalArgumentException e) {
                LOGGER.error(
                        "Error decoding Base64 image data received from Freepik module: {}",
                        e.getMessage());
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("Error during Freepik module /illustrate API call", e);
            throw new RuntimeException("Error during Freepik module /illustrate API call", e);
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
            LOGGER.info(
                    "Current promptDrawStyle before update: {}",
                    characterContext.getPromptDrawStyle());
            characterContext.setPromptDrawStyle(newDrawStyle);
            LOGGER.info("promptDrawStyle during update: {}", newDrawStyle);
            characterContext = characterContextService.saveAndFlush(characterContext);
            LOGGER.info("promptDrawStyle after update: {}", characterContext.getPromptDrawStyle());

            return newImageBlob;

        } catch (Exception e) {
            LOGGER.error("Error during illustration regeneration for character ID: {}", id, e);
            throw new RuntimeException("Error during illustration regeneration", e);
        }
    }
}
