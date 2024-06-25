package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when a player tries to perform actions in a game in which he is not registered.
 * @see it.polimi.ingsw.am01.controller.Controller
 */
public class PlayerNotInGameException extends Exception {

    public PlayerNotInGameException() {
        super();
    }
}
