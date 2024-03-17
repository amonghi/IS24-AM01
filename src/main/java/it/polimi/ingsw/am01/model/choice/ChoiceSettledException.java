package it.polimi.ingsw.am01.model.choice;

public class ChoiceSettledException extends IllegalStateException {
    public ChoiceSettledException() {
        super("The choice has been already settled and cannot be changed anymore.");
    }
}
