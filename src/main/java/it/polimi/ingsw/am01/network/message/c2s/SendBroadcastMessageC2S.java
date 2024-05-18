package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record SendBroadcastMessageC2S(String content) implements C2SNetworkMessage {
    public static final String ID = "SEND_BROADCAST_MESSAGE";

    @Override
    public String getId() {
        return ID;
    }
}
