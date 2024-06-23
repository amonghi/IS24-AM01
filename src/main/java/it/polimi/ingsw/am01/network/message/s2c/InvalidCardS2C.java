package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message notifies the player that the id of the specified card is incorrect.
 * @param cardId the card id specified
 * @see it.polimi.ingsw.am01.network.message.c2s.DrawCardFromFaceUpCardsC2S
 * @see it.polimi.ingsw.am01.network.message.c2s.PlaceCardC2S
 */
public record InvalidCardS2C(int cardId) implements S2CNetworkMessage {
    public static final String ID = "INVALID_CARD";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
