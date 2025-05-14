package jdr.generator.api.characters.illustration;

/** Service interface for managing character illustration operations. */
public interface CharacterIllustrationService {

  /**
   * Saves a new character illustration.
   *
   * @param context The CharacterIllustrationEntity to save.
   * @return The saved CharacterIllustrationEntity.
   */
  CharacterIllustrationEntity save(CharacterIllustrationEntity context);

  /**
   * Finds a character illustration by its ID.
   *
   * @param id The ID of the character illustration to find.
   * @return The found CharacterIllustrationEntity.
   */
  CharacterIllustrationEntity findById(long id);
}
