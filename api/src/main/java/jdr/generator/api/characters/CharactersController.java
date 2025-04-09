package jdr.generator.api.characters;

import jdr.generator.api.characters.context.DefaultContextJson;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsModel;
import jdr.generator.api.characters.details.CharacterDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/characters")
public class CharactersController {
    private final String geminiHost = "http://localhost:5173";
    private final jdr.generator.api.characters.GeminiService geminiService;
    private final CharacterDetailsService characterDetailsService;

    CharactersController(
            GeminiService geminiService,
            CharacterDetailsService characterDetailsService
    ) {
        this.geminiService = geminiService;
        this.characterDetailsService = characterDetailsService;
    }

    @PutMapping("/details/{id}")
    @Transactional
    public CharacterDetailsEntity updateCharacter(@PathVariable Long id, @RequestBody CharacterFullModel updatedCharacter) {
        return characterDetailsService.updateCharacterDetails(id, updatedCharacter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCharacter(@PathVariable Long id) {
        characterDetailsService.deleteCharacter(id);
    }

    @GetMapping
    public List<CharacterDetailsModel> getAllCharacters() {
        return characterDetailsService.getAllCharacters();
    }

    @GetMapping("/full")
    public List<CharacterFullModel> getAllCharactersFull() {
        return characterDetailsService.getAllCharactersFull();
    }

    @RequestMapping(value = {"/generate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = geminiHost)
    public CharacterDetailsModel generate(@RequestBody DefaultContextJson data) {
        return geminiService.generate(data);
    }

    @RequestMapping(value = {"/illustrate"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = geminiHost)
    public String illustrate(@RequestBody String imagePrompt) {
        return geminiService.illustrate(imagePrompt);
    }

    @RequestMapping(value = {"/stats/{id}"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = geminiHost)
    public String stats(@PathVariable Long id) {
        return geminiService.stats(id);
    }

}
