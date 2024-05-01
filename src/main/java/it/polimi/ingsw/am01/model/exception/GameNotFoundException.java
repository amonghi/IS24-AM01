package it.polimi.ingsw.am01.model.exception;

public class GameNotFoundException extends Exception {
    private final int gameId;

    public GameNotFoundException(int gameId) {
        super();
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
