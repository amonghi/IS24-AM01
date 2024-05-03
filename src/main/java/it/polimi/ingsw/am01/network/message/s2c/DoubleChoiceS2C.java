package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record DoubleChoiceS2C() implements S2CNetworkMessage {

    //TODO: decide if make it specific for objectives or use this message also for DoubleSideChoiceS2C(Side)
    public static final String ID = "DOUBLE_CHOICE";

    @Override
    public String getId() {
        return ID;
    }
}
