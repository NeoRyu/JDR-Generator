package jdr.generator.api.characters.details;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomIntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // Valeur par défaut si la chaîne n'est pas un entier valide
            return 0;
        }
    }
}