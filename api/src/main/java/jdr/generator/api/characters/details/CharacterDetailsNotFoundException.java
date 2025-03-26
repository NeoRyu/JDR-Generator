package jdr.generator.api.characters.details;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterDetailsNotFoundException extends ResponseStatusException {

    public CharacterDetailsNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public CharacterDetailsNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Character details not found for ID: " + id);
    }
}