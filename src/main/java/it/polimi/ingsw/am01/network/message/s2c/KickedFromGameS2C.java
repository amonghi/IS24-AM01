package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the game has ended prematurely
 * due to an insufficient number of connected players.
 * @see it.polimi.ingsw.am01.model.event.GameAbortedEvent
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public record KickedFromGameS2C() implements S2CNetworkMessage {
    public static final String ID = "KICKED_FROM_GAME";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
