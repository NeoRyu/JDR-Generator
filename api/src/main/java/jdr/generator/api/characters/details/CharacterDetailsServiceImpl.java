package jdr.generator.api.characters.details;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterDetailsServiceImpl implements CharacterDetailsService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterDetailsRepository characterDetailsRepository;    // JPA Repository

    @Autowired
    public CharacterDetailsServiceImpl(
            final CharacterDetailsRepository characterDetailsRepository
    ) {
        this.characterDetailsRepository = characterDetailsRepository;
    }

    @Override
    public CharacterDetailsEntity save(CharacterDetailsEntity character) {
        return this.characterDetailsRepository.save(character);
    }

    @Override
    public CharacterDetailsEntity findById(long id) {
        LOGGER.info("Character findById : {}", id);
        return this.characterDetailsRepository.findById(id).orElse(null);
    }

}
