package it.polimi.ingsw.am01.model.exception;

/**
 * This exception is thrown when actions are performed on a non-existent game.
 * @see it.polimi.ingsw.am01.controller.VirtualView
 */
public class GameNotFoundException extends Exception {
    private final int gameId;

    public GameNotFoundException(int gameId) {
        super();
        this.gameId = gameId;
    }

    /**
     * @return the refused game's id
     */
    public int getGameId() {
        return gameId;
    }
}
