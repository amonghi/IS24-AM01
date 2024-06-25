package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when there are not enough game resources (cards or objectives).
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public class NotEnoughGameResourcesException extends IllegalStateException {
    public NotEnoughGameResourcesException(String message) {
        super(message);
    }
}