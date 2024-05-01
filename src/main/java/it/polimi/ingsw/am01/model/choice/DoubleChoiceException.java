package it.polimi.ingsw.am01.model.choice;

public class DoubleChoiceException extends Exception {
    public DoubleChoiceException() {
        super("A choice can only be made once.");
    }
}
