package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.controller.VirtualView;

/**
 * This exception is thrown when a client tries to perform illegal actions during game.
 * This type of exception causes the disconnection of the client that executed the command.
 * @see VirtualView#run()
 */
public class IllegalMoveException extends Exception {

    public IllegalMoveException() {
        super();
    }
}
