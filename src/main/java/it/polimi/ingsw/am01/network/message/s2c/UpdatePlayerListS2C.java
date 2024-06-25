package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.List;

/**
 * This message tells players the new list of users connected to the game.
 * This message is sent when a player leaves or joins the game.
 * @param playerList the new players' list
 * @see it.polimi.ingsw.am01.model.event.PlayerJoinedEvent
 * @see it.polimi.ingsw.am01.model.event.PlayerLeftEvent
 */
public record UpdatePlayerListS2C(List<String> playerList) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAYER_LIST";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
