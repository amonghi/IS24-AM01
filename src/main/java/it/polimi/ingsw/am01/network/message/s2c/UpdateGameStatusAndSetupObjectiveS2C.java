package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdateGameStatusAndSetupObjectiveS2C(int objectiveId1, int objectiveId2) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS_AND_SETUP_OBJECTIVE";
    private static final GameStatus gameStatus = GameStatus.SETUP_OBJECTIVE;

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
