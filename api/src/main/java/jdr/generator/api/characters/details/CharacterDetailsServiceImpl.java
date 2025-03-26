package jdr.generator.api.characters.details;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CharacterDetailsServiceImpl implements CharacterDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CharacterDetailsServiceImpl.class);

    private final ModelMapper modelMapper;
    private final CharacterDetailsRepository characterDetailsRepository;    // JPA Repository

    @Autowired
    public CharacterDetailsServiceImpl(
            final CharacterDetailsRepository characterDetailsRepository,
            final ModelMapper modelMapper
    ) {
        this.characterDetailsRepository = characterDetailsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CharacterDetailsEntity save(final CharacterDetailsEntity character) {
        logger.info("Saving character details: {}", character);
        return this.characterDetailsRepository.save(character);
    }

    @Override
    public CharacterDetailsEntity findById(Long id) {
        logger.info("Finding character details by ID: {}", id);
        CharacterDetailsEntity entity = characterDetailsRepository.findById(id).orElse(null);
        if (entity == null) {
            logger.warn("Character details not found for ID: {}", id);
        }
        return entity;
    }

    @Override
    public CharacterDetailsEntity updateCharacterDetails(final Long id, final CharacterDetailsModel model) {
        CharacterDetailsEntity entity = this.findById(id);
        if (entity != null) {
            logger.debug("Character details found for update: {}", entity);
            modelMapper.map(model, entity);
            CharacterDetailsEntity updatedEntity = this.characterDetailsRepository.save(entity);
            logger.info("Character details updated for ID: {}", id);
            return updatedEntity;
        } else {
            logger.warn("Character details not found for update, ID: {}", id);
            throw new CharacterDetailsNotFoundException(id);
        }
    }

    @Override
    public List<CharacterDetailsModel> getAllCharacters() {
        logger.info("Fetching all characters details.");
        try {
            List<CharacterDetailsModel> characters = characterDetailsRepository.findAll()
                    .parallelStream()
                    .map(entity -> modelMapper.map(entity, CharacterDetailsModel.class))
                    .collect(Collectors.toList());

            logger.debug("Fetched {} characters details.", characters.size());
            return characters;

        } catch (Exception e) {
            logger.info("Empty list when fetching all characters details: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
