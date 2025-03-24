package jdr.generator.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                    jsonResponse.append(cleanResponseToValidJson(responseLine));
                }
            }
            if (isValidJson(String.valueOf(jsonResponse))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                CharacterModel character = gson.fromJson(String.valueOf(jsonResponse), CharacterModel.class);
                System.out.println(character.toString());
            }

            // TODO : add custom context and character generated to database
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonResponse.toString();
    }

    private String cleanResponseToValidJson(String responseLine) {
        String data = responseLine.trim();
        if (data.contains("```json")) {
            data = data.replace("```json", "");
        }
        if (data.contains("```")) {
            data = data.replace("```", "");
        }
        if (data.contains("{\"response\":\"\\n")) {
            data = data.replace("{\"response\":\"\\n", "");
            int pos = data.lastIndexOf("\\n}");
            if (pos > -1) {
                data = data.substring(0, pos) + data.substring(pos + "\\n}".length());
            }
        }
        return data.trim();
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
