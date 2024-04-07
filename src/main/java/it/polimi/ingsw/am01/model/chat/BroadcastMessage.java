package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * A {@code Message} for every {@link PlayerProfile} connected to the game
 * @see it.polimi.ingsw.am01.model.chat.Message
 */
public class BroadcastMessage extends Message{

    /**
     * Construct a new broadcast {@code Message}
     * @param sender The {@link PlayerProfile} who send the {@code Message}
     * @param content The content of the {@code Message}
     */
    public BroadcastMessage(PlayerProfile sender, String content) {
        super(sender, content);
    }

    /**
     *
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not.
     * The sender is not considered as a valid recipient
     */
    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return !pp.equals(super.getSender());
    }
}
