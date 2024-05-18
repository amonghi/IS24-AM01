package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;

public record UpdatePlayerColorS2C(String player,
                                   PlayerColor color) implements it.polimi.ingsw.am01.network.message.S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAYER_COLOR";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
