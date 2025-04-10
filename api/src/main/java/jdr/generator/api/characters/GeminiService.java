package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.context.CharacterContextService;
import jdr.generator.api.characters.context.DefaultContextJson;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationModel;
import jdr.generator.api.characters.illustration.CharacterIllustrationService;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataModel;
import jdr.generator.api.characters.stats.CharacterJsonDataService;
import jdr.generator.api.config.IGeminiGenerationConfig;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Service
@RequiredArgsConstructor
public class GeminiService implements IGeminiGenerationConfig {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String hostApiIA = "http://localhost:3000/";

    private final RestTemplate restTemplate;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterContextService characterContextService;
    private final CharacterIllustrationService characterIllustrationService;
    private final CharacterJsonDataService characterJsonDataService;
    private final ModelMapper modelMapper;

    private String cleanJsonString(String jsonString) {
        if (jsonString == null) {
            return "{}";
        }
        try {
            JsonNode rootNode = OBJECT_MAPPER.readTree(jsonString);
            JsonNode responseNode = rootNode.get("response");

            if (responseNode == null) {
                // La clé "response" est absente, retourner le JSON tel quel (sans modifications)
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

    @Override
    @Transactional
    public CharacterDetailsModel generate(DefaultContextJson data) {
        final String apiUrl = hostApiIA + "generate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DefaultContextJson> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("API request failed with status code: {}", response.getStatusCode());
            throw new RuntimeException("API request failed");
        }

        try {
            // Nettoyage de la réponse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String innerJsonString = jsonNode.get("response").asText()
                    .replace("```json", "")
                    .replace("\\n", "")
                    .replace("\\t", "")
                    .replace("\\", "");
            innerJsonString = innerJsonString.substring(1, innerJsonString.length() - 1);


            CharacterContextModel characterContextModel = this.characterContextService.createCharacterContextModel(data);
            final CharacterContextEntity characterContextEntity = characterContextService.save(
                    modelMapper.map(characterContextModel, CharacterContextEntity.class)
            );

            CharacterDetailsModel characterDetailsModel = OBJECT_MAPPER.readValue(innerJsonString, CharacterDetailsModel.class);
            characterDetailsModel = characterDetailsModel.toBuilder()
                    .createdAt(java.util.Date.from(java.time.Instant.now()))
                    .updatedAt(java.util.Date.from(java.time.Instant.now()))
                    .contextId(characterContextEntity.getId())
                    .build();
            final CharacterDetailsEntity characterDetailsEntity = characterDetailsService.save(
                    modelMapper.map(characterDetailsModel, CharacterDetailsEntity.class)
            );

            LOGGER.info("Created CharacterModel {{id={}}} :: {} [{} {{idContext={}}}]",
                    characterDetailsEntity.getId(), characterDetailsModel.name, characterContextModel.promptGender, characterContextEntity.getId());

            final String imgBlob = this.illustrate(characterDetailsModel.image);
            if (imgBlob != null) {
                try {
                    JsonNode imageNode = OBJECT_MAPPER.readTree(imgBlob).get("image");
                    if (imageNode != null) {
                        byte[] imageBytes = Base64.getDecoder().decode(imageNode.asText());

                        // Récupérer l'entité CharacterDetailsEntity depuis la base de données
                        CharacterDetailsEntity parent = this.characterDetailsService.findById(characterDetailsEntity.getId());

                        final CharacterIllustrationModel characterIllustrationModel = CharacterIllustrationModel.builder()
                                .imageLabel(parent.getImage())
                                .imageBlob(imageBytes)
                                .imageDetails(parent)
                                .build();
                        CharacterIllustrationEntity characterIllustrationEntity = this.modelMapper.map(characterIllustrationModel, CharacterIllustrationEntity.class);
                        this.characterIllustrationService.save(characterIllustrationEntity);
                    }
                } catch (JsonProcessingException e) {
                    LOGGER.error("Error parsing image blob: {}", e.getMessage());
                }
            }

            String statsJson = this.stats(characterDetailsEntity.getId());
            LOGGER.info("Stats JSON: {}", statsJson);
            try {
                String cleanedJson = cleanJsonString(statsJson);
                LOGGER.info("Cleaned JSON: {}", cleanedJson);
                this.saveCharacterJsonData(characterDetailsEntity.getId(), cleanedJson);
                LOGGER.info("Character JSON data saved successfully");
            } catch (JsonProcessingException e) {
                LOGGER.error("Erreur lors de l'analyse du JSON stats : {}", e.getMessage());
                this.saveCharacterJsonData(characterDetailsEntity.getId(), statsJson.isEmpty() ? "{}" : statsJson);
                LOGGER.warn("Character JSON data saved with JSON due to parsing error.");
            }

            // FINALLY WE RETURN CHARACTER DETAILS
            return characterDetailsModel;
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing JSON response: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String illustrate(String data) {
        final String apiUrl = hostApiIA + "illustrate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", data);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("API illustrate request failed with status code: {}", response.getStatusCode());
            throw new RuntimeException("API illustrate request failed");
        }

        LOGGER.info("Illustration API response: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public String stats(Long characterId) {
        CharacterDetailsEntity character = this.characterDetailsService.findById(characterId);
        CharacterContextEntity context = this.characterContextService.findById(character.getContextId());
        String data = "promptSystem: '" + context.getPromptSystem() +"'\n"
                + "promptRace: '" + context.getPromptRace() +"'\n"
                + "promptGender: '" + context.getPromptGender() +"'\n"
                + "promptClass: '" + context.getPromptClass() +"'\n"
                + "promptDescription: '" + context.getPromptDescription() +"'\n"
                + "name: '" + character.getName() +"'\n"
                + "age: '" + character.getAge() +"'\n"
                + "education: '" + character.getEducation() +"'\n"
                + "profession: '" + character.getProfession() +"'\n"
                + "reasonForProfession: '" + character.getReasonForProfession() +"'\n"
                + "workPreferences: '" + character.getWorkPreferences() +"'\n"
                + "changeInSelf: '" + character.getChangeInSelf() +"'\n"
                + "changeInWorld: '" + character.getChangeInWorld() +"'\n"
                + "goal: '" + character.getGoal() +"'\n"
                + "reasonForGoal: '" + character.getReasonForGoal() +"'\n";

        final String apiUrl = hostApiIA + "stats";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("data", data);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("API stats request failed with status code: {}", response.getStatusCode());
            throw new RuntimeException("API stats request failed");
        }

        LOGGER.info("Statistiques API response: {}", response.getBody());
        return response.getBody();
    }

    private void saveCharacterJsonData(Long characterDetailsId, String jsonData) throws JsonProcessingException {
        final CharacterJsonDataModel jsonDataModel = CharacterJsonDataModel.builder()
                .characterDetailsId(characterDetailsId)
                .jsonData(jsonData)
                .createdAt(java.util.Date.from(java.time.Instant.now()))
                .updatedAt(java.util.Date.from(java.time.Instant.now()))
                .build();
        CharacterJsonDataEntity jsonDataEntity = modelMapper.map(jsonDataModel, CharacterJsonDataEntity.class);
        characterJsonDataService.save(jsonDataEntity);
    }

}