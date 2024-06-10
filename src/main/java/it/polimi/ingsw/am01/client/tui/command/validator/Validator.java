package it.polimi.ingsw.am01.client.tui.command.validator;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public interface Validator {
    void validate(CommandContext context) throws ValidationException;
}
