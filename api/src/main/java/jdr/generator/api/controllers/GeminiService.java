package jdr.generator.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    @Override
    public String generate(PromptCharacterContext data) {
        final String apiUrl = "http://localhost:3000/generate";
        StringBuilder jsonResponse = new StringBuilder();
        try {
            HttpURLConnection con = getHttpURLConnection(data, apiUrl);
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    jsonResponse.append(responseLine);
                }
            }

            // Cleaning and Mapping JSON response to POJO CharacterModel :
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(String.valueOf(jsonResponse));
            String innerJsonString = jsonNode.get("response").asText();
            innerJsonString = innerJsonString.replace("```json", "").replace("\\n", "").replace("\\t","").replace("\\","");
            innerJsonString = innerJsonString.substring(1, innerJsonString.length()-1);
            if (isValidJson(innerJsonString)) {
                CharacterModel character = objectMapper.readValue(innerJsonString, CharacterModel.class);
                System.out.println("{JSON extracted} Test extraction with CharacterModel.name :: " + character.name);
            } else {
                System.out.println("> The JSON obtained from the AI is invalid, cleaning in place did not resolve the issue :: " + innerJsonString);
            }

            // TODO : add custom context and character generated to database
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonResponse.toString();
    }

    public boolean isValidJson(String json) {
        System.out.println(json);
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            System.out.println("=> INVALID JSON RESPONSE OBJECT ...");
            return false;
        }
        System.out.println("=> VALID JSON RESPONSE OBJECT !");
        return true;
    }


    @Override
    public String illustrate(String data) {
        final String apiUrl = "http://localhost:3000/illustrate";
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
