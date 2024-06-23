package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that he cannot perform the desired action as it is not in game.
 * @see it.polimi.ingsw.am01.model.exception.PlayerNotInGameException
 */
public record PlayerNotInGameS2C() implements S2CNetworkMessage {
    public static final String ID = "PLAYER_NOT_IN_GAME";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
