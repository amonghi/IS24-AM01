package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to resume a game that is on {@code RESTORING} status.
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record ResumeGameC2S() implements C2SNetworkMessage {
    public static final String ID = "RESUME_GAME";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
