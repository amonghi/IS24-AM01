package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.Map;

/**
 * This message tells players the new list of available games.
 * Only accessible games are shown in the list (not yet started).
 * For each game are shown: the number of users currently logged in and the maximum number of users allowed.
 * @param gamesStatMap
 * @see it.polimi.ingsw.am01.model.event.GameManagerEvent
 */
public record UpdateGameListS2C(Map<Integer, GameStat> gamesStatMap) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_LIST_INFO";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * This record represents game's data.
     * @param currentPlayersConnected the number of users currently logged in
     * @param maxPlayers the maximum number of users allowed
     */
    public record GameStat(int currentPlayersConnected, int maxPlayers) implements Serializable {
    }
}
