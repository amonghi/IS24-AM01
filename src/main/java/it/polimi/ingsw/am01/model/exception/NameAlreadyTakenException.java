package it.polimi.ingsw.am01.model.exception;

public class NameAlreadyTakenException extends Exception{
    private final String refusedName;

    public NameAlreadyTakenException(String refusedName) {
        super();
        this.refusedName = refusedName;
    }

    public String getRefusedName() {
        return refusedName;
    }
}
