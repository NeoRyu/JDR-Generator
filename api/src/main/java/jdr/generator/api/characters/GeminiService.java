package jdr.generator.api.characters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextMapper;
import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.context.CharacterContextService;
import jdr.generator.api.characters.details.CharacterDetailsMapper;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Service
public class GeminiService implements IGeminiGenerationConfig {

    final String hostApiIA = "http://localhost:3000/";

    private final CharacterDetailsService characterDetailsService;    // JPA Repository
    private final CharacterContextService characterContextService;    // JPA Repository

    public GeminiService(
            CharacterDetailsService characterDetailsService,
            CharacterContextService characterContextService
    ) {
        this.characterDetailsService = characterDetailsService;
        this.characterContextService = characterContextService;
    }

    @Override
    public CharacterDetailsModel generate(DefaultContextJson data) {
        final String apiUrl = hostApiIA + "generate";
        StringBuilder jsonResponse = new StringBuilder();
        CharacterDetailsModel character;
        try {
            // On contact le serveur local qui va envoyer notre prompt à GEMINI
            HttpURLConnection con = getHttpURLConnection(data, apiUrl);
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    jsonResponse.append(responseLine);
                }
            }

            // nettoyage de la réponse JSON et mapping vers l'objet CharacterModel :
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(String.valueOf(jsonResponse));
            String innerJsonString = jsonNode.get("response").asText();
            innerJsonString = innerJsonString
                    .replace("```json", "")
                    .replace("\\n", "")
                    .replace("\\t","")
                    .replace("\\","");
            innerJsonString = innerJsonString.substring(1, innerJsonString.length()-1);
            if (isValidJson(innerJsonString)) {
                character = objectMapper.readValue(innerJsonString, CharacterDetailsModel.class);
                System.out.println("{JSON extracted} Test extraction with CharacterModel.name :: " + character.name);
            } else {
                character = new CharacterDetailsModel();
                System.out.println("> The JSON obtained from the AI is invalid, cleaning in place did not resolve the issue :: " + innerJsonString);
            }

            CharacterContextModel characterContextModel = new CharacterContextModel();
            characterContextModel.promptSystem = data.promptSystem;
            characterContextModel.promptRace = data.promptRace;
            characterContextModel.promptGender = data.promptGender;
            characterContextModel.promptClass = data.promptClass;
            characterContextModel.promptDescription = data.promptDescription;
            CharacterContextEntity characterContextEntity = this.characterContextService.save(CharacterContextMapper.convertModelToEntity(characterContextModel));
            this.characterDetailsService.save(CharacterDetailsMapper.convertModelToEntity(character, characterContextEntity));

            // TODO : add custom context and character generated to database
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //
        return character;
    }

    public boolean isValidJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            System.out.println("> INVALID JSON RESPONSE");
            return false;
        }
        System.out.println("> VALID JSON RESPONSE");
        return true;
    }


    @Override
    public String illustrate(String data) {
        final String apiUrl = hostApiIA + "illustrate";
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection con = getHttpURLConnection(data, apiUrl);
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.toString();
    }

    private static HttpURLConnection getHttpURLConnection(Object data, String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInputString = mapper.writeValueAsString(data);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return con;
    }
}
