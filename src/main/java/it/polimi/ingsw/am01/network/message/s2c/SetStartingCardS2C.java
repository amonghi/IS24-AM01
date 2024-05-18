package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record SetStartingCardS2C(int startingCardId) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_STATUS_AND_SET_STARTING_CARD";
    private static final GameStatus gameStatus = GameStatus.SETUP_STARTING_CARD_SIDE;

    @Override
    public String getId() {
        return ID;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
