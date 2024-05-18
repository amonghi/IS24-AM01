package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

public record UpdateFaceUpCardsS2C(Set<Integer> faceUpCards) implements S2CNetworkMessage {

    public static final String ID = "UPDATE_FACE_UP_CARDS";

    @Override
    public String getId() {
        return ID;
    }
}
