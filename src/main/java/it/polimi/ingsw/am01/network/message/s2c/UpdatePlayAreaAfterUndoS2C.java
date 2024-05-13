package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdatePlayAreaAfterUndoS2C(String profile, int i, int j, int seq) implements S2CNetworkMessage {

    public static final String ID = "UNDO_PLACEMENT";

    @Override
    public String getId() {
        return ID;
    }
}
