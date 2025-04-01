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
    private final ModelMapper modelMapper;

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

            CharacterDetailsModel characterDetailsModel = objectMapper.readValue(innerJsonString, CharacterDetailsModel.class);
            characterDetailsModel.createdAt = java.util.Date.from(java.time.Instant.now());
            characterDetailsModel.setContextId(characterContextEntity.getId()); // set contextId
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

                        CharacterIllustrationModel characterIllustrationModel = new CharacterIllustrationModel();
                        characterIllustrationModel.setImageLabel(parent.getImage());
                        characterIllustrationModel.setImageBlob(imageBytes);
                        characterIllustrationModel.setImageDetails(parent);

                        CharacterIllustrationEntity characterIllustrationEntity = this.modelMapper.map(characterIllustrationModel, CharacterIllustrationEntity.class);
                        characterIllustrationEntity = this.characterIllustrationService.save(characterIllustrationEntity);
                    }
                } catch (JsonProcessingException e) {
                    LOGGER.error("Error parsing image blob: {}", e.getMessage());
                }
            }

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



}