package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Map;

/**
 * This message allows players to receive the final scores of the game, once it has ended.
 * @param finalScores a {@link Map} that contains the final scores
 */
public record GameFinishedS2C(Map<String, Integer> finalScores) implements S2CNetworkMessage {
    public static final String ID = "GAME_FINISHED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
