package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

public record SetBoardAndHandS2C(Set<Integer> commonObjectives, Set<Integer> faceUpCards,
                                 Set<Integer> hand) implements S2CNetworkMessage {

    public static final String ID = "SET_BOARD_AND_HAND";

    @Override
    public String getId() {
        return ID;
    }
}
