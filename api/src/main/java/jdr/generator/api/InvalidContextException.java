package jdr.generator.api;

/**
 * Exception thrown when an invalid data context is encountered.
 *
 * <p>This exception is a runtime exception, meaning it does not need to be declared in method
 * signatures. It is used to indicate that the provided data context is invalid or unsuitable for
 * the requested operation.
 */
public class InvalidContextException extends RuntimeException {
  /** Constructs a new InvalidContextException with a default message. */
  InvalidContextException() {
    super("> Invalid data context found !");
  }
}
