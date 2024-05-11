package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record GameJoinedS2C(int gameId, GameStatus gameStatus) implements S2CNetworkMessage {
    public static final String ID = "GAME_JOINED";

    @Override
    public String getId() {
        return ID;
    }

    // TODO: execute() will set gameStatus
}
