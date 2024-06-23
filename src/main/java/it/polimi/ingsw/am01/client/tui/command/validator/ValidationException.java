package it.polimi.ingsw.am01.client.tui.command.validator;

/**
 * An exception that is thrown when a validation fails.
 *
 * @see PreValidator
 * @see PostValidator
 */
public class ValidationException extends Exception {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }
}
