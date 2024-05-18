package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record DoubleSideChoiceS2C(Side invalidSideChoice) implements S2CNetworkMessage {
    public static final String ID = "DOUBLE_SIDE_CHOICE";

    @Override
    public String getId() {
        return ID;
    }
}
