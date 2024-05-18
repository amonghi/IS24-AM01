package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.message.C2SMessageVisitor;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record SelectColorC2S(PlayerColor color) implements C2SNetworkMessage {
    public static String ID = "SELECT_COLOR";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(C2SMessageVisitor visitor) throws IllegalMoveException, NetworkException {
        visitor.visit(this);
    }
}
