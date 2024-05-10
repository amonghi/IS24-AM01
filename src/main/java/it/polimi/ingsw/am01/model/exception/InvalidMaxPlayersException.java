package it.polimi.ingsw.am01.model.exception;

public class InvalidMaxPlayersException extends Exception{
    private final int invalidMaxPlayers;


    public InvalidMaxPlayersException(int invalidMaxPlayers) {
        super();
        this.invalidMaxPlayers = invalidMaxPlayers;
    }

    public int getInvalidMaxPlayers() {
        return invalidMaxPlayers;
    }
}
