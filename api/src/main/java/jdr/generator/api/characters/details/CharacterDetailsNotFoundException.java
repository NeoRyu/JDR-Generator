package jdr.generator.api.characters.details;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Exception thrown when character details are not found. */
public class CharacterDetailsNotFoundException extends ResponseStatusException {

    /**
     * Constructor with a specific message.
     *
     * @param message The detail message.
     */
    public CharacterDetailsNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Constructor with a character ID.
     *
     * @param id The ID of the character that was not found.
     */
    public CharacterDetailsNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Character details not found for ID: " + id);
    }
}
