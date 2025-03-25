package jdr.generator.api.characters.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

;


@Service
public class CharacterContextServiceImpl implements CharacterContextService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterContextRepository characterContextRepository;    // JPA Repository
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterContextServiceImpl(
            final CharacterContextRepository characterContextRepository,
            ModelMapper modelMapper
    ) {
        this.characterContextRepository = characterContextRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CharacterContextEntity save(CharacterContextEntity Context) {
        return this.characterContextRepository.save(Context);
    }

    @Override
    public CharacterContextEntity findById(long id) {
        LOGGER.info("Context findById : {}", id);
        return this.characterContextRepository.findById(id).orElse(null);
    }

    @Override
    public List<CharacterContextModel> getAllContexts() {
        return this.characterContextRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, CharacterContextModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CharacterContextModel createCharacterContextModel(DefaultContextJson data) {
        CharacterContextModel characterContextModel = new CharacterContextModel();
        characterContextModel.promptSystem = data.getPromptSystem();
        characterContextModel.promptRace = data.getPromptRace();
        characterContextModel.promptGender = data.getPromptGender();
        characterContextModel.promptClass = data.getPromptClass();
        characterContextModel.promptDescription = data.getPromptDescription();
        characterContextModel.createdAt = java.util.Date.from(java.time.Instant.now());
        return characterContextModel;
    }

}
