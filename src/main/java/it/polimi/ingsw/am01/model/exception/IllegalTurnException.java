package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when a player tries to perform an action not during his turn.
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public class IllegalTurnException extends IllegalMoveException {
    public IllegalTurnException() {
        super();
    }
}
