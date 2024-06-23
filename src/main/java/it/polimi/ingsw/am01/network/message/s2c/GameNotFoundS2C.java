package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the specified game id is incorrect (there are no games with that id).
 * @param refusedGameId the refused game id
 */
public record GameNotFoundS2C(int refusedGameId) implements S2CNetworkMessage {
    public static final String ID = "GAME_NOT_FOUND";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
