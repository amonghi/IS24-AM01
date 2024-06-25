package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when a player specifies an invalid card in the command that he want to execute.
 * @see it.polimi.ingsw.am01.controller.Controller
 */
public class InvalidCardException extends Exception {
    public InvalidCardException() {
        super();
    }
}
