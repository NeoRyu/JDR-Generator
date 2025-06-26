package jdr.generator.api.characters.details;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/** Custom deserializer for handling integer values from JSON. */
public class CustomIntegerDeserializer extends JsonDeserializer<Integer> {

  /**
   * Deserializes a JSON string to an Integer value.
   *
   * @param jsonParser The JSON parser.
   * @param deserializationContext The deserialization context.
   * @return The parsed Integer value, or 0 if the string is not a valid integer.
   * @throws IOException If an I/O exception occurs during deserialization.
   */
  @Override
  public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String value = jsonParser.getText();
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      // Valeur par défaut si la chaîne n'est pas un entier valide
      return 0;
    }
  }
}
