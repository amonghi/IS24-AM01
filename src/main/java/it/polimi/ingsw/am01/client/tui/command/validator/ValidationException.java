package it.polimi.ingsw.am01.client.tui.command.validator;

public class ValidationException extends Exception {
    public ValidationException() {
        super("Unknown command");
    }

    public ValidationException(String message) {
        super(message);
    }
}
