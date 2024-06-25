package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to select a color during {@code SETUP_COLOR} status.
 * @param color the color chosen
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record SelectColorC2S(PlayerColor color) implements C2SNetworkMessage {
    public static String ID = "SELECT_COLOR";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
