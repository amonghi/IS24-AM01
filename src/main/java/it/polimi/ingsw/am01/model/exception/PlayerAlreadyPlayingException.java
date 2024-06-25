package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This exception is thrown when a player tries to join a game twice.
 * @see it.polimi.ingsw.am01.model.game.Game#join(PlayerProfile)
 */
public class PlayerAlreadyPlayingException extends IllegalMoveException {
    private final String playerAlreadyPlaying;

    public PlayerAlreadyPlayingException(String playerAlreadyPlaying) {
        super();
        this.playerAlreadyPlaying = playerAlreadyPlaying;
    }

    /**
     * @return the name of the player
     */
    public String getPlayerAlreadyPlaying() {
        return playerAlreadyPlaying;
    }
}
