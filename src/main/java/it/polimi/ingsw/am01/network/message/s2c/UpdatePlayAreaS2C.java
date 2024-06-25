package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players the new {@code PlayArea}'s status of a player who has just placed a card.
 * The message shows only the last placement made.
 * @param playerName the owner of the {@code PlayArea}
 * @param i the i coordinate of the new placement
 * @param j the j coordinate of the new placement
 * @param cardId the id of the card placed
 * @param side the side of the card placed
 * @param seq the sequence number of the new placement
 * @param points points earned from the new placement
 * @see it.polimi.ingsw.am01.model.event.CardPlacedEvent
 * @see it.polimi.ingsw.am01.model.game.PlayArea
 */
public record UpdatePlayAreaS2C(String playerName, int i, int j, int cardId, Side side, int seq,
                                int points) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAY_AREA";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
