package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public class UpdateGameStatusAndTurnEvent extends GameEvent{
    private final GameStatus gameStatus;
    private final TurnPhase turnPhase;
    private final PlayerProfile currentPlayer;


    public UpdateGameStatusAndTurnEvent(GameStatus gameStatus, TurnPhase turnPhase, PlayerProfile currentPlayer) {
        this.gameStatus = gameStatus;
        this.turnPhase = turnPhase;
        this.currentPlayer = currentPlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public PlayerProfile getCurrentPlayer() {
        return currentPlayer;
    }
}
