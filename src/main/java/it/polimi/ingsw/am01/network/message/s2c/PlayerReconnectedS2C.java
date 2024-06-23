package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players that a user has just reconnected.
 * @param playerName the name of the player that has just reconnected
 * @see it.polimi.ingsw.am01.model.event.PlayerReconnectedEvent
 */
public record PlayerReconnectedS2C(String playerName) implements S2CNetworkMessage {
    public static final String ID = "PLAYER_RECONNECTED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
