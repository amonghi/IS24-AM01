package it.polimi.ingsw.am01.model.exception;

public class PlayerAlreadyPlayingException extends Exception{
    private final String playerAlreadyPlaying;

    public PlayerAlreadyPlayingException(String playerAlreadyPlaying) {
        super();
        this.playerAlreadyPlaying = playerAlreadyPlaying;
    }

    public String getPlayerAlreadyPlaying() {
        return playerAlreadyPlaying;
    }
}
