package it.polimi.ingsw.am01.client.tui.command.validator;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

/**
 * A validator that gets called after a {@link it.polimi.ingsw.am01.client.tui.command.CommandNode}
 * has parsed its piece of command string.
 * <p>
 * If the validation fails, a {@link ValidationException} should be thrown.
 */
public interface PostValidator {
    /**
     * Performs the validation.
     *
     * @param context The context that contains the arguments that need to be validated.
     * @throws ValidationException If the validation fails.
     */
    void validate(CommandContext context) throws ValidationException;
}
