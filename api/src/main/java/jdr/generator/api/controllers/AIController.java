package jdr.generator.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ai")
public class AIController {

    private final GeminiService geminiService;

    AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @RequestMapping(value = {"/generate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String generate(@RequestBody ICharacterContext data) {
        return geminiService.generate(data);
    }

    @RequestMapping(value = {"/illustrate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String illustrate(@RequestBody String imagePrompt) {
        return geminiService.illustrate(imagePrompt);
    }

}

