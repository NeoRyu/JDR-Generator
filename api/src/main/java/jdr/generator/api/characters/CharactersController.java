package jdr.generator.api.characters;

import jdr.generator.api.characters.details.CharacterDetailsModel;
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
    public CharacterDetailsModel generate(@RequestBody DefaultContextJson data) {
        return geminiService.generate(data);
    }

    @RequestMapping(value = {"/illustrate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public String illustrate(@RequestBody String imagePrompt) {
        return geminiService.illustrate(imagePrompt);
    }

}

