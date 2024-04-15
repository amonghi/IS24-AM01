package it.polimi.ingsw.am01.model.game;

public class SourceShouldNotBeEmptyException extends IllegalStateException {
    public SourceShouldNotBeEmptyException(String message) {
        super(message);
    }
}