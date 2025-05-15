package jdr.generator.api.characters.details;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Custom deserializer for handling boolean values from JSON.
 * */
public class CustomBooleanDeserializer extends JsonDeserializer<Boolean> {

  /**
   * Deserializes a JSON string to a Boolean value.
   *
   * @param jsonParser The JSON parser.
   * @param deserializationContext The deserialization context.
   * @return {@code true} if the string is "true" (case-insensitive), {@code false} if it's "false"
   *     (case-insensitive), and {@code false} for any other value.
   * @throws IOException If an I/O exception occurs during deserialization.
   */
  @Override
  public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String value = jsonParser.getText();
    if ("true".equalsIgnoreCase(value)) {
      return true;
    } else if ("false".equalsIgnoreCase(value)) {
      return false;
    } else {
      // Valeur par défaut si la chaîne n'est pas un booléen valide
      return false;
    }
  }
}
