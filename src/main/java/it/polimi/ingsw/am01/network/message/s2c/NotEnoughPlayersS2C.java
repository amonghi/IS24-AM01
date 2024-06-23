package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the game cannot resume/start since there are not enough players connected.
 * @param actualPlayers the actual number of players
 * @see it.polimi.ingsw.am01.network.message.c2s.StartGameC2S
 * @see it.polimi.ingsw.am01.network.message.c2s.ResumeGameC2S
 * @see it.polimi.ingsw.am01.model.exception.NotEnoughPlayersException
 */
public record NotEnoughPlayersS2C(int actualPlayers) implements S2CNetworkMessage {
    public static final String ID = "NOT_ENOUGH_PLAYERS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
