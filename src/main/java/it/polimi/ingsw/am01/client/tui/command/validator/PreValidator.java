package it.polimi.ingsw.am01.client.tui.command.validator;

/**
 * A validator that gets called before a {@link it.polimi.ingsw.am01.client.tui.command.CommandNode}
 * has parsed its piece of command string.
 * <p>
 * If the validation fails, a {@link ValidationException} should be thrown.
 * <p>
 * This is useful to check if the command can be executed in the current state of the application.
 */
public interface PreValidator {
    /**
     * Performs the validation.
     *
     * @throws ValidationException If the validation fails.
     */
    void validate() throws ValidationException;
}
