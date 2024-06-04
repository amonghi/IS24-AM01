package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.game.GameStatus;

public class GamePausedEvent implements ViewEvent {
    private final GameStatus gameStatus = GameStatus.SUSPENDED;

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
