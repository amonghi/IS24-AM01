package it.polimi.ingsw.am01.model.choice;

public class DoubleChoiceException extends IllegalStateException {
    public DoubleChoiceException() {
        super("A choice can only be made once.");
    }
}
