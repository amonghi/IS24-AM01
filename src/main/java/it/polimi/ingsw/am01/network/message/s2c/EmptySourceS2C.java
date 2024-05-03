package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.DrawSource;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record EmptySourceS2C(DrawSource drawSource) implements S2CNetworkMessage {

    public static final String ID = "EMPTY_SOURCE";

    @Override
    public String getId() {
        return ID;
    }
}
