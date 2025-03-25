package jdr.generator.api.characters.details;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CharacterDetailsServiceImpl implements CharacterDetailsService {

    private final CharacterDetailsRepository characterDetailsRepository;    // JPA Repository
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterDetailsServiceImpl(
            final CharacterDetailsRepository characterDetailsRepository,
            ModelMapper modelMapper
    ) {
        this.characterDetailsRepository = characterDetailsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CharacterDetailsEntity save(CharacterDetailsEntity character) {
        return this.characterDetailsRepository.save(character);
    }

    @Override
    public CharacterDetailsEntity findById(Long id) {
        return characterDetailsRepository.findById(id).orElse(null);
    }

    @Override
    public List<CharacterDetailsModel> getAllCharacters() {
        return characterDetailsRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, CharacterDetailsModel.class))
                .collect(Collectors.toList());
    }

}
