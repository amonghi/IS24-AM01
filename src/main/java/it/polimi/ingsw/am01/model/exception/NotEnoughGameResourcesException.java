package it.polimi.ingsw.am01.model.exception;

public class NotEnoughGameResourcesException extends IllegalStateException {
    public NotEnoughGameResourcesException(String message) {
        super(message);
    }
}