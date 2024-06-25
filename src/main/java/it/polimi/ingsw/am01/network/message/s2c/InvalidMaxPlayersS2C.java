package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the specified maximum threshold ({@code maxPlayers}) is not valid.
 * @param refusedMaxPlayers the invalid threshold
 * @see it.polimi.ingsw.am01.network.message.c2s.CreateGameAndJoinC2S
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public record InvalidMaxPlayersS2C(int refusedMaxPlayers) implements S2CNetworkMessage {
    public static final String ID = "INVALID_MAX_PLAYERS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
