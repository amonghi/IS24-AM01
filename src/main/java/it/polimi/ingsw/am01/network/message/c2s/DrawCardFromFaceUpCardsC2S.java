package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record DrawCardFromFaceUpCardsC2S(int cardId) implements C2SNetworkMessage {

    public static final String ID = "DRAW_CARD_FROM_FACE_UP_CARDS";

    @Override
    public String getId() {
        return ID;
    }
}
