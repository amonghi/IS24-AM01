package it.polimi.ingsw.am01.model.exception;

/**
 * @see it.polimi.ingsw.am01.model.game.Game#Game(int, int)
 */
public class InvalidMaxPlayersException extends Exception {
    private final int invalidMaxPlayers;

    public InvalidMaxPlayersException(int invalidMaxPlayers) {
        super();
        this.invalidMaxPlayers = invalidMaxPlayers;
    }

    /**
     * @return the invalid {@code maxPlayers}
     */
    public int getInvalidMaxPlayers() {
        return invalidMaxPlayers;
    }
}
