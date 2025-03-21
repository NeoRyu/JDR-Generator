package jdr.generator.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public String generate(ICharacterContext data) {
        final String apiUrl = "http://localhost:3000/generate";
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
