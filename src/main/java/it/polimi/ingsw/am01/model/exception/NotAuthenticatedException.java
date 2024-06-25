package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when an unauthenticated player tries to execute unauthorized commands.
 * @see it.polimi.ingsw.am01.controller.Controller
 */
public class NotAuthenticatedException extends IllegalMoveException {
    public NotAuthenticatedException() {
        super();
    }
}
