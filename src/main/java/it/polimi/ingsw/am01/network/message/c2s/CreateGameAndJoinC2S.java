package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to create a new game.
 * User sending such request will be automatically added in the newly created game (implicit join).
 * @param maxPlayers the maximum number of players allowed to participate in the game.
 */
public record CreateGameAndJoinC2S(int maxPlayers) implements C2SNetworkMessage {
    public static final String ID = "CREATE_GAME_AND_JOIN";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
