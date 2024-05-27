package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdateDeckStatusS2C(CardColor resourceDeckColor,
                                  CardColor goldenDeckColor) implements S2CNetworkMessage {

    public static final String ID = "UPDATE_DECK_STATUS";

    @Override
    public String getId() {
        return ID;
    }
}
