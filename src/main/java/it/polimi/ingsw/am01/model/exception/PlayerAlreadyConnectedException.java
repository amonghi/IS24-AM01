package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This exception is thrown when trying to reconnect a player already connected to the game.
 * @see it.polimi.ingsw.am01.model.game.Game#handleReconnection(PlayerProfile)
 */
public class PlayerAlreadyConnectedException extends IllegalMoveException {
    public PlayerAlreadyConnectedException() {
        super();
    }
}
