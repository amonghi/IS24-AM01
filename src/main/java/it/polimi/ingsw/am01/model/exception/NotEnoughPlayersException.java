package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.game.Game;

/**
 * @see Game#startGame()
 */
public class NotEnoughPlayersException extends Exception {
    public NotEnoughPlayersException() {
        super();
    }
}
