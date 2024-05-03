package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdateDeckStatusS2C(boolean resourceCardDeckIsEmpty,
                                  boolean goldenCardDeckIsEmpty) implements S2CNetworkMessage {

    public static final String ID = "UPDATE_DECK_STATUS";

    @Override
    public String getId() {
        return ID;
    }
}
