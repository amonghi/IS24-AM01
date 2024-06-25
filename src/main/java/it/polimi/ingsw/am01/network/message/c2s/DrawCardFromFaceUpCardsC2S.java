package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to draw a specified {@code FaceUpCard}.
 * @param cardId the id of the {@code FaceUpCard} that the user chose
 * @see it.polimi.ingsw.am01.model.game.FaceUpCard
 */
public record DrawCardFromFaceUpCardsC2S(int cardId) implements C2SNetworkMessage {
    public static final String ID = "DRAW_CARD_FROM_FACE_UP_CARDS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
