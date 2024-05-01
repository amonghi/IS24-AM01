package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdateGameStatusS2C(GameStatus gameStatus) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS";

    @Override
    public String getId() {
        return ID;
    }
}
