package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;

public class GameFinishedEvent implements GameEvent {
    private final GameStatus gameStatus = GameStatus.FINISHED;
    private final Map<PlayerProfile, Integer> playerScores;


    public GameFinishedEvent(Map<PlayerProfile, Integer> playerScores) {
        this.playerScores = playerScores;
    }


    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Map<PlayerProfile, Integer> getPlayerScores() {
        return playerScores;
    }
}
