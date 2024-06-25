package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This exception is thrown when a player tries to send a {@code DirectMessage} to himself.
 * @see it.polimi.ingsw.am01.model.chat.DirectMessage#DirectMessage(PlayerProfile, PlayerProfile, String)
 */
public class MessageSentToThemselvesException extends Exception {
    private final PlayerProfile playerProfile;

    public MessageSentToThemselvesException(PlayerProfile pp) {
        super();
        this.playerProfile = pp;
    }

    /**
     * @return the player name
     */
    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }
}
