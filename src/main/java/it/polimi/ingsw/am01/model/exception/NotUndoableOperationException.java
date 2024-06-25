package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * This exception is thrown when an undo is requested on a {@code PlayArea} and the only one card placed is the starting card.
 * Starting card can't be removed from the {@code PlayArea}.
 * @see PlayArea#undoPlacement()
 */
public class NotUndoableOperationException extends Exception {
    public NotUndoableOperationException() {
        super();
    }
}
