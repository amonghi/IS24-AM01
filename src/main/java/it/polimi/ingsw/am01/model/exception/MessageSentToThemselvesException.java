package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public class MessageSentToThemselvesException extends Exception {
    private final PlayerProfile playerProfile;

    public MessageSentToThemselvesException(PlayerProfile pp) {
        super();
        this.playerProfile = pp;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }
}
