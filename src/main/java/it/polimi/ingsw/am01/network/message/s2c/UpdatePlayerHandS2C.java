package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

public record UpdatePlayerHandS2C(Set<Integer> currentHand) implements S2CNetworkMessage {

    public static final String ID = "UPDATE_PLAYER_HAND";

    @Override
    public String getId() {
        return ID;
    }
}
