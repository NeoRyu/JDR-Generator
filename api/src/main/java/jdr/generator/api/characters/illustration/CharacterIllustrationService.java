package jdr.generator.api.characters.illustration;


public interface CharacterIllustrationService {

    CharacterIllustrationEntity save(CharacterIllustrationEntity context);

    CharacterIllustrationEntity findById(long id);

}