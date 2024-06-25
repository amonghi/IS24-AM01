package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when actions are performed during wrong {@code GameStatus}.
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public class IllegalGameStateException extends IllegalMoveException {
    public IllegalGameStateException() {
        super();
    }
}
