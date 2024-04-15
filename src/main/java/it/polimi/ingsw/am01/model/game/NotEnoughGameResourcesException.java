package it.polimi.ingsw.am01.model.game;

public class NotEnoughGameResourcesException extends IllegalStateException {
    public NotEnoughGameResourcesException(String message) {
        super(message);
    }
}