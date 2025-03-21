package jdr.generator.api.controllers;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private GeminiService geminiService;

    /*
    private final CharacterRepository characterRepository;

    AIController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }
    */

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Object generate(@RequestBody ICharacterContext data) {
        Preconditions.checkNotNull(data);
        return geminiService.generate(data);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Object illustrate(@RequestBody String data) {
        Preconditions.checkNotNull(data);
        return geminiService.illustrate(data);
    }

}

