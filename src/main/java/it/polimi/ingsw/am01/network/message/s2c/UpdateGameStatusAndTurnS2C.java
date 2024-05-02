package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdateGameStatusAndTurnS2C(GameStatus gameStatus, TurnPhase turnPhase, String currentPlayerName) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS_AND_TURN";

    @Override
    public String getId() {
        return ID;
    }
}
