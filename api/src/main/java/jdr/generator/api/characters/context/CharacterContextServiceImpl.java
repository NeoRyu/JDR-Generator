package jdr.generator.api.characters.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterContextServiceImpl implements CharacterContextService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterContextRepository characterContextRepository;    // JPA Repository

    @Autowired
    public CharacterContextServiceImpl(
            final CharacterContextRepository characterContextRepository
    ) {
        this.characterContextRepository = characterContextRepository;
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

}
