package jdr.generator.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/characters")
public class CharactersController {

    private final GeminiService geminiService;

    CharactersController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @RequestMapping(value = {"/generate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public String generate(@RequestBody PromptCharacterContext data) {
        return geminiService.generate(data);
    }

    @RequestMapping(value = {"/illustrate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public String illustrate(@RequestBody String imagePrompt) {
        return geminiService.illustrate(imagePrompt);
    }

}

