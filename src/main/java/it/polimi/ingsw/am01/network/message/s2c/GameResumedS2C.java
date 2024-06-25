package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message notifies players that the game has been resumed.
 * @see it.polimi.ingsw.am01.network.message.c2s.ResumeGameC2S
 */
public record GameResumedS2C() implements S2CNetworkMessage {
    public static final String ID = "SET_RECOVER_STATUS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
