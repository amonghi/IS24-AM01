package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is used to detect player disconnections during games.
 * @see it.polimi.ingsw.am01.controller.VirtualView
 */
public record PingS2C() implements S2CNetworkMessage {
    public static final String ID = "PING";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
