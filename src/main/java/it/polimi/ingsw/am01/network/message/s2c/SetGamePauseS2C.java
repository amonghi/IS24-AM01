package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public class SetGamePauseS2C implements S2CNetworkMessage {
    public static final String ID = "SET_PAUSE";
    private final GameStatus gameStatus = GameStatus.SUSPENDED;

    public SetGamePauseS2C() {
    }

    @Override
    public String getId() {
        return ID;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public String toString() {
        return "SetGamePauseS2C{" +
                "gameStatus=" + gameStatus +
                '}';
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
