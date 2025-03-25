package jdr.generator.api.characters.illustration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CharacterIllustrationServiceImpl implements CharacterIllustrationService {

    private final CharacterIllustrationRepository characterIllustrationRepository;

    @Override
    @Transactional
    public CharacterIllustrationEntity save(CharacterIllustrationEntity characterIllustrationEntity) {
        return characterIllustrationRepository.save(characterIllustrationEntity);
    }

    @Override
    public CharacterIllustrationEntity findById(long id) {
        return characterIllustrationRepository.findById(id).orElse(null);

    }

}
