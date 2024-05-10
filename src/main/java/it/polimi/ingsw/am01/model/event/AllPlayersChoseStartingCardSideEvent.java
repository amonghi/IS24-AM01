package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;

public class AllPlayersChoseStartingCardSideEvent implements GameEvent {
    private final GameStatus gameStatus = GameStatus.SETUP_COLOR;

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
