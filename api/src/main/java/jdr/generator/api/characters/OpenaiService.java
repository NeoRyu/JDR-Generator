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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;


@Service("openaiService") // Nommer le service pour l'injection
@RequiredArgsConstructor
public class OpenaiService implements IGeminiGenerationConfig { // Implémenter l'interface commune

    private static final Logger LOGGER = LogManager.getLogger(); // Utiliser la méthode de GeminiService
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${OPENAI_API_URL}") // Utiliser la propriété spécifique à OpenAI
    private String hostApiIA;

    private final RestTemplate restTemplate;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterContextService characterContextService;
    private final CharacterIllustrationService characterIllustrationService;
    private final CharacterJsonDataService characterJsonDataService;
    private final ModelMapper modelMapper;


    // --- Méthode cleanJsonString copiée et nettoyée ---
    private String cleanJsonString(String jsonString) {
        if (jsonString == null) {
            return "{}";
        }
        try {
            JsonNode rootNode = OBJECT_MAPPER.readTree(jsonString);
            JsonNode responseNode = rootNode.get("response");

            if (responseNode == null) {
                LOGGER.warn("Clé 'response' absente dans la réponse JSON, retournant le JSON tel quel.");
                return jsonString;
            }

            String innerJsonString = responseNode.asText();

            // Vérifier si innerJsonString est un objet JSON valide
            if (innerJsonString.startsWith("{") && innerJsonString.endsWith("}")) {
                return innerJsonString;
            } else {
                // Tenter une seconde lecture pour gérer les chaînes de JSON encodées
                try {
                    JsonNode innerNode = OBJECT_MAPPER.readTree(innerJsonString);
                    return innerNode.toString();
                } catch (JsonProcessingException innerE) {
                    LOGGER.warn("La chaîne interne de la clé 'response' n'est pas un JSON valide ou une chaîne JSON encodée, retournant le texte tel quel.", innerE);
                    return innerJsonString; // Retourne la chaîne non parsable telle quelle
                }
            }

        } catch (JsonProcessingException e) {
            LOGGER.warn("JSON invalide lors du nettoyage initial : {}", jsonString, e);
            return "{}";
        }
    }
    // --- Fin méthode cleanJsonString ---


    @Override
    @Transactional
    public CharacterDetailsModel generate(DefaultContextJson data) {
        // 1. Appel au endpoint /generate (texte background) du module Node.js OpenAI
        final String generateApiUrl = hostApiIA + (hostApiIA.endsWith("/") ? "generate" : "/generate"); // S'assurer du slash final
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DefaultContextJson> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /generate at {}", generateApiUrl);
            response = restTemplate.exchange(generateApiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /generate API response status: {}", response.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error calling OpenAI /generate API: {}", e.getMessage(), e);
            throw new RuntimeException("Error communicating with OpenAI API for text generation", e);
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            String errorBody = response.getBody() != null ? response.getBody() : "No response body";
            LOGGER.error("OpenAI /generate API request failed with status code: {} and body: {}", response.getStatusCode(), errorBody);
            throw new RuntimeException("OpenAI /generate API request failed with status code: " + response.getStatusCode());
        }

        CharacterDetailsModel characterDetailsModel;
        String statsJson; // Déclarer la variable ici pour une portée correcte
        try {
            // Nettoyage de la réponse JSON en utilisant la logique copiée
            String innerJsonString = cleanJsonString(response.getBody());
            if (innerJsonString.trim().isEmpty() || "{}".equals(innerJsonString.trim())) {
                LOGGER.error("OpenAI /generate API returned empty or invalid background data after cleaning.");
                throw new RuntimeException("OpenAI /generate API returned empty or invalid background data.");
            }
            LOGGER.debug("Generated Background JSON (cleaned): {}", innerJsonString);


            // Sauvegarde du contexte (logique identique à GeminiService)
            CharacterContextModel characterContextModel = this.characterContextService.createCharacterContextModel(data);
            final CharacterContextEntity characterContextEntity = characterContextService.save(
                    modelMapper.map(characterContextModel, CharacterContextEntity.class)
            );
            LOGGER.info("Character context saved with ID: {}", characterContextEntity.getId());


            // Parsing et sauvegarde des détails du personnage (texte) (logique identique à GeminiService)
            characterDetailsModel = OBJECT_MAPPER.readValue(innerJsonString, CharacterDetailsModel.class);
            characterDetailsModel = characterDetailsModel.toBuilder()
                    .createdAt(Date.from(Instant.now()))
                    .updatedAt(Date.from(Instant.now()))
                    .contextId(characterContextEntity.getId()) // Assurez-vous que CharacterDetailsModel a bien un contextId
                    .build();
            final CharacterDetailsEntity characterDetailsEntity = characterDetailsService.save(
                    modelMapper.map(characterDetailsModel, CharacterDetailsEntity.class)
            );
            LOGGER.info("Character details saved with ID: {} and name: {}", characterDetailsEntity.getId(), characterDetailsModel.name);

            LOGGER.info("Created CharacterModel {{id={}}} :: {} [{} {{idContext={}}}]",
                    characterDetailsEntity.getId(), characterDetailsModel.name, characterContextModel.promptGender, characterContextEntity.getId());


            // 2. Appel au endpoint /illustrate (image) du module Node.js OpenAI
            final String imagePrompt = characterDetailsModel.image;
            String imgBlob; // On s'attend à recevoir le JSON string { "image": "..." }

            if (imagePrompt != null && !imagePrompt.trim().isEmpty()) {
                try {
                    LOGGER.info("Calling OpenAI module /illustrate via OpenaiService.illustrate with prompt: {}", imagePrompt);
                    // Appel à la méthode illustrate de CE service OpenaiService
                    // Cette méthode dans OpenaiService doit retourner un String JSON comme {"image" : "..." } pour correspondre à la logique de parsing ci-dessous
                    imgBlob = this.illustrate(imagePrompt);
                    LOGGER.info("Illustration JSON blob received from OpenAI module.");


                    if (imgBlob != null && !imgBlob.trim().isEmpty()) {
                        try {
                            // Logique de parsing et de décodage Base64 copiée de GeminiService.generate
                            JsonNode imageNode = OBJECT_MAPPER.readTree(imgBlob).get("image");
                            if (imageNode != null && imageNode.isTextual()) {
                                byte[] imageBytes = Base64.getDecoder().decode(imageNode.asText());

                                // Récupérer l'entité CharacterDetailsEntity depuis la base de données pour le lien
                                // Utiliser le service pour trouver par ID
                                CharacterDetailsEntity parent = this.characterDetailsService.findById(characterDetailsEntity.getId());
                                if (parent == null) {
                                    LOGGER.error("Character details entity not found for illustration link for ID: {}", characterDetailsEntity.getId());
                                    // Décider si c'est une erreur fatale ou si l'on continue sans sauvegarder l'image
                                    throw new RuntimeException("Character details entity not found for illustration link for ID: " + characterDetailsEntity.getId());
                                }

                                final CharacterIllustrationModel characterIllustrationModel = CharacterIllustrationModel.builder()
                                        .imageLabel(parent.getImage()) // Utilise le prompt de l'image comme label
                                        .imageBlob(imageBytes) // Stocke les bytes décodés
                                        .imageDetails(parent) // Lien vers les détails du personnage (Entity)
                                        // createdAt et updatedAt ne sont pas gérés par ce builder selon la structure GeminiService, mais le service de sauvegarde les gère.
                                        .build();
                                CharacterIllustrationEntity characterIllustrationEntity = this.modelMapper.map(characterIllustrationModel, CharacterIllustrationEntity.class);
                                this.characterIllustrationService.save(characterIllustrationEntity);
                                LOGGER.info("Character illustration saved successfully for ID: {}", characterDetailsEntity.getId());
                            } else {
                                LOGGER.error("OpenAI module /illustrate returned JSON without valid 'image' field for prompt: {}", imagePrompt);
                                // Décider si une erreur ici doit arrêter la transaction
                                throw new RuntimeException("OpenAI module /illustrate returned invalid illustration JSON.");
                            }
                        } catch (IllegalArgumentException e) {
                            LOGGER.error("Error decoding Base64 image data received from OpenAI module for saving for ID {}: {}", characterDetailsEntity.getId(), e.getMessage());
                            throw new RuntimeException("Error decoding Base64 image data for saving", e);
                        } catch (JsonProcessingException e) {
                            LOGGER.error("Error parsing JSON blob received from OpenAI module /illustrate: {}", e.getMessage());
                            throw new RuntimeException("Error parsing JSON blob from OpenAI module /illustrate", e);
                        }
                        catch (Exception e) { // Capture les erreurs de sauvegarde du service
                            LOGGER.error("Error processing or saving illustration data after receiving from OpenAI module for ID {}", characterDetailsEntity.getId(), e);
                            throw new RuntimeException("Error during illustration processing or saving", e);
                        }
                    } else {
                        LOGGER.warn("OpenAI module /illustrate returned empty or null image blob for prompt: {}", imagePrompt);
                    }
                } catch (Exception e) { // Capture les erreurs d'appel à this.illustrate (qui appelle le module Node.js)
                    LOGGER.error("Error calling OpenAI module /illustrate for prompt {}: {}", imagePrompt, e.getMessage(), e);
                    throw new RuntimeException("Error during illustration process with OpenAI", e);
                }
            } else {
                LOGGER.warn("No image prompt provided in generated character details from OpenAI, skipping illustration.");
            }


            // 3. Appel au endpoint /stats (statistiques) du module Node.js OpenAI
            // L'ID du personnage vient de l'entité characterDetailsEntity sauvegardée
            LOGGER.info("Calling OpenAI module /stats via OpenaiService.stats for character ID: {}", characterDetailsEntity.getId());
            try {
                // Appel à la méthode stats de CE service OpenaiService
                statsJson = this.stats(characterDetailsEntity.getId());
                LOGGER.debug("Generated Stats JSON: {}", statsJson);

                // Sauvegarde des données JSON de stats
                if (statsJson != null && !statsJson.trim().isEmpty()) {
                    try {
                        OBJECT_MAPPER.readTree(statsJson); // Tente de parser pour valider
                        this.saveCharacterJsonData(characterDetailsEntity.getId(), statsJson); // Utilise la méthode de sauvegarde copiée
                        LOGGER.info("Character JSON stats data saved successfully for ID: {}", characterDetailsEntity.getId());
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Erreur lors de l'analyse du JSON stats généré par OpenAI : {}", e.getMessage());
                        LOGGER.warn("Saving raw stats JSON due to parsing error: {}", statsJson);
                        try {
                            this.saveCharacterJsonData(characterDetailsEntity.getId(), !statsJson.isEmpty() ? statsJson : "{}");
                        } catch (Exception saveRawError) {
                            LOGGER.error("Error saving raw stats JSON for ID {}", characterDetailsEntity.getId(), saveRawError);
                        }
                        throw new RuntimeException("Error parsing generated stats JSON from OpenAI", e);

                    } catch (Exception e) { // Capture les erreurs du service de sauvegarde
                        LOGGER.error("Error saving character JSON data for ID {}", characterDetailsEntity.getId(), e);
                        throw new RuntimeException("Error saving character JSON data", e);
                    }
                } else {
                    LOGGER.warn("No stats JSON received from OpenAI module /stats, skipping save for ID {}.", characterDetailsEntity.getId());
                }
            } catch (Exception e) { // Capture les erreurs d'appel à this.stats (qui appelle le module Node.js)
                LOGGER.error("Error during OpenAI module /stats call for character ID {}", characterDetailsEntity.getId(), e);
                throw new RuntimeException("Error during stats generation process", e);
            }

            // Retourne le modèle de détails complété
            return characterDetailsModel;

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing JSON response from OpenAI /generate: {}", e.getMessage());
            throw new RuntimeException("Error processing JSON response from OpenAI /generate", e);
        } catch (RuntimeException e) { // Capturer nos propres RuntimeExceptions pour loguer
            LOGGER.error("RuntimeException during OpenaiService orchestration: {}", e.getMessage(), e);
            throw e;
        }
        catch (Exception e) { // Capturer toute autre exception inattendue
            LOGGER.error("Unexpected error during OpenaiService orchestration", e);
            throw new RuntimeException("Unexpected error during OpenaiService orchestration", e);
        }
    }


    /**
     * Appelle au endpoint /illustrate du module Node.js OpenAI pour générer une illustration.
     * Doit retourner un String contenant un objet JSON avec la clé "image".
     * @param imagePrompt Le prompt pour la génération de l'image.
     * @return La chaîne JSON recue du module Node.js OpenAI (doit contenir {"image" : "..." }).
     */
    @Override // Implémente la méthode de l'interface
    public String illustrate(String imagePrompt) {
        final String apiUrl = hostApiIA + (hostApiIA.endsWith("/") ? "illustrate" : "/illustrate"); // S'assurer du slash final
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", imagePrompt);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /illustrate at {}", apiUrl);
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /illustrate API response status: {}", response.getStatusCode());


            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                String errorBody = response.getBody() != null ? response.getBody() : "No response body";
                LOGGER.error("OpenAI /illustrate API request failed with status code: {} and body: {}", response.getStatusCode(), errorBody);
                throw new RuntimeException("OpenAI /illustrate API request failed with status code: " + response.getStatusCode());
            }

            // On renvoie la chaîne brute, la logique de parsing est dans generate()
            LOGGER.info("Illustration API response received from OpenAI module."); // Log du body retiré ici pour éviter d'afficher potentiellement de très longues chaînes Base64
            return response.getBody();

        } catch (Exception e) { // Capturer les erreurs d'appel HTTP ou autres
            LOGGER.error("Error during OpenAI module /illustrate API call", e);
            throw new RuntimeException("Error during OpenAI module /illustrate API call", e);
        }
    }

    /**
     * Appelle au endpoint /stats du module Node.js OpenAI pour générer les statistiques.
     * @param characterDetailsId L'ID du détail du personnage.
     * @return La réponse texte (JSON string) du module Node.js OpenAI.
     */
    @Override // Implémente la méthode de l'interface
    public String stats(Long characterDetailsId) {
        LOGGER.info("Preparing data for OpenAI module /stats for character ID: {}", characterDetailsId);

        // On récupère désormais les données en utilisant les services injectés
        CharacterDetailsEntity characterDetailsEntity = characterDetailsService.findById(characterDetailsId);
        if (characterDetailsEntity == null) {
            LOGGER.error("Character details entity not found for stats call for ID: {}", characterDetailsId);
            throw new RuntimeException("Character details not found for ID: " + characterDetailsId);
        }

        CharacterContextEntity characterContextEntity = characterContextService.findById(characterDetailsEntity.getContextId());
        if (characterContextEntity == null) {
            LOGGER.error("Character context entity not found for stats call for ID: {}", characterDetailsEntity.getContextId());
            throw new RuntimeException("Character context not found for ID: " + characterDetailsEntity.getContextId());
        }

        // Construction de l'objet JSON à envoyer au module Node.js pour /stats (logique identique à GeminiService)
        // Utiliser directement les getters sur les entités récupérées, comme dans GeminiService
        String dataToSend = "promptSystem: '" + characterContextEntity.getPromptSystem() +"'\n"
                + "promptRace: '" + characterContextEntity.getPromptRace() +"'\n"
                + "promptGender: '" + characterContextEntity.getPromptGender() +"'\n"
                + "promptClass: '" + characterContextEntity.getPromptClass() +"'\n"
                + "promptDescription: '" + characterContextEntity.getPromptDescription() +"'\n"
                + "name: '" + characterDetailsEntity.getName() +"'\n"
                + "age: '" + characterDetailsEntity.getAge() +"'\n"
                + "education: '" + characterDetailsEntity.getEducation() +"'\n"
                + "profession: '" + characterDetailsEntity.getProfession() +"'\n"
                + "reasonForProfession: '" + characterDetailsEntity.getReasonForProfession() +"'\n"
                + "workPreferences: '" + characterDetailsEntity.getWorkPreferences() +"'\n"
                + "changeInSelf: '" + characterDetailsEntity.getChangeInSelf() +"'\n"
                + "changeInWorld: '" + characterDetailsEntity.getChangeInWorld() +"'\n"
                + "goal: '" + characterDetailsEntity.getGoal() +"'\n"
                + "reasonForGoal: '" + characterDetailsEntity.getReasonForGoal() +"'\n";

        final String apiUrl = hostApiIA + (hostApiIA.endsWith("/") ? "stats" : "/stats"); // S'assurer du slash final
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("data", dataToSend); // Mettre l'objet des données dans la clé "data"
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response;
        try {
            LOGGER.info("Calling OpenAI module /stats at {}", apiUrl);
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            LOGGER.info("OpenAI /stats API response status: {}", response.getStatusCode());


            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                String errorBody = response.getBody() != null ? response.getBody() : "No response body";
                LOGGER.error("OpenAI /stats API request failed with status code: {} and body: {}", response.getStatusCode(), errorBody);
                throw new RuntimeException("OpenAI /stats API request failed with status code: " + response.getStatusCode());
            }

            // On renvoie la chaîne brute, la logique de nettoyage/parsing pour la sauvegarde est dans generate()
            LOGGER.info("Statistiques API response received from OpenAI module: {}", response.getBody());
            return response.getBody();

        }  catch (Exception e) { // Capturer les erreurs d'appel HTTP ou autres
            LOGGER.error("Error during OpenAI module /stats API call", e);
            throw new RuntimeException("Error during OpenAI module /stats API call", e);
        }
    }

    // --- Méthode saveCharacterJsonData copiée et nettoyée ---
    private void saveCharacterJsonData(Long characterDetailsId, String jsonData) {
        try {
            final CharacterJsonDataModel jsonDataModel = CharacterJsonDataModel.builder()
                    .characterDetailsId(characterDetailsId)
                    .jsonData(jsonData)
                    .createdAt(Date.from(Instant.now()))
                    .updatedAt(Date.from(Instant.now()))
                    .build();
            CharacterJsonDataEntity jsonDataEntity = modelMapper.map(jsonDataModel, CharacterJsonDataEntity.class);
            characterJsonDataService.save(jsonDataEntity);
            LOGGER.info("Character JSON data saved successfully for ID: {}", characterDetailsId);
        } catch (Exception e) { // Capture les exceptions du service de sauvegarde
            LOGGER.error("Error saving character JSON data for ID {}", characterDetailsId, e);
            throw new RuntimeException("Error saving character JSON data", e);
        }
    }
    // --- Fin méthode saveCharacterJsonData ---
}