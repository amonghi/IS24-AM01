package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.player.PlayerColor;

/**
 * This message tells the player the color choice made by a user ({@code SETUP_COLOR} status).
 * @param player the player who made the choice
 * @param color the color chosen
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 * @see it.polimi.ingsw.am01.model.event.PlayerChangedColorChoiceEvent
 */
public record UpdatePlayerColorS2C(String player,
                                   PlayerColor color) implements it.polimi.ingsw.am01.network.message.S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAYER_COLOR";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
