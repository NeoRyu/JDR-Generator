package jdr.generator.api;

/**
 * Utility class for enforcing preconditions in RESTful services.
 *
 * <p>This class provides a static method for checking if a data object is found (not null) and
 * throwing an exception if it is not.
 */
public class RestPreconditions {

  /**
   * Checks if the given data object is not null.
   *
   * @param <T> The type of the data object.
   * @param data The data object to check.
   * @return The data object if it is not null.
   * @throws Exception if the data object is null.
   * @throws InvalidContextException If the data object is null.
   */
  public static <T> T checkFound(T data) throws Exception {
    if (data == null) {
      throw new InvalidContextException();
    }
    return data;
  }
}
