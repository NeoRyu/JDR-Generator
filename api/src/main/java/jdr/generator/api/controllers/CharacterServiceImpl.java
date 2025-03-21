package jdr.generator.api.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterServiceImpl implements CharacterService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterRepository characterRepository;    // JPA Repository

    @Autowired
    public CharacterServiceImpl(
            final CharacterRepository characterRepository
    ) {
        this.characterRepository = characterRepository;
    }

    @Override
    public CharacterEntity findById(long id) {
        LOGGER.info("Characters findById : {}", id);
        return this.characterRepository.findById(id).orElse(null);
    }

}
