package jdr.generator.api.characters.illustration;

/** Service interface for managing character illustration operations. */
public interface CharacterIllustrationService {

  /**
   * Saves a new character illustration.
   *
   * @param entity The CharacterIllustrationEntity to save.
   * @return The saved CharacterIllustrationEntity.
   */
  CharacterIllustrationEntity save(CharacterIllustrationEntity entity);

  CharacterIllustrationEntity saveAndFlush(CharacterIllustrationEntity entity);

  /**
   * Finds a character illustration by its ID.
   *
   * @param id The ID of the character illustration to find.
   * @return The found CharacterIllustrationEntity.
   */
  CharacterIllustrationEntity findById(long id);

  /**
   * Finds a character illustration by its associated character details ID.
   *
   * @param characterDetailsId The ID of the CharacterDetailsEntity to search by.
   * @return The found CharacterIllustrationEntity.
   * @throws RuntimeException if the illustration is not found for the given characterDetailsId.
   */
  CharacterIllustrationEntity findByCharacterDetailsId(Long characterDetailsId);

  /**
   * Updates an existing character illustration with a new image blob and optionally a new label.
   *
   * @param characterDetailsId The ID of the character details associated with the illustration.
   * @param newImageBlob The new image data as a byte array.
   * @param newImageLabel An optional new label for the image. If null, the existing label is kept.
   * @return The updated CharacterIllustrationEntity.
   */
  CharacterIllustrationEntity updateIllustration(
      Long characterDetailsId, byte[] newImageBlob, String newImageLabel);
}
