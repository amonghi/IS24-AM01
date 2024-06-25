package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.DrawSource;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is sent to the player when he tries to draw a card from an empty {@code DrawSource}.
 * @param drawSource the source chosen
 * @see it.polimi.ingsw.am01.network.message.c2s.DrawCardFromFaceUpCardsC2S
 * @see it.polimi.ingsw.am01.network.message.c2s.DrawCardFromDeckC2S
 * @see DrawSource
 */
public record EmptySourceS2C(DrawSource drawSource) implements S2CNetworkMessage {
    public static final String ID = "EMPTY_SOURCE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
