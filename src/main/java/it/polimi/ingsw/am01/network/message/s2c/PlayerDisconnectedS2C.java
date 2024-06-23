package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells all players in game that a user has logged out.
 * @param playerName the name of the player who has just disconnected
 * @see it.polimi.ingsw.am01.model.event.PlayerDisconnectedEvent
 */
public record PlayerDisconnectedS2C(String playerName) implements S2CNetworkMessage {
    public static final String ID = "PLAYER_DISCONNECTED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
