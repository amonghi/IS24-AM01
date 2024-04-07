package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * A {@code Message} for a specific {@link PlayerProfile} connected to the game
 * @see it.polimi.ingsw.am01.model.chat.Message
 */
public class DirectMessage extends Message {
    private final PlayerProfile recipient;

    /**
     * Construct a new direct message
     * @param sender The {@link PlayerProfile} who send the {@code Message}
     * @param recipient The {@link PlayerProfile} who receive the {@code Message}
     * @param content The content of the {@code Message}
     */
    public DirectMessage(PlayerProfile sender, PlayerProfile recipient, String content) {
        super(sender, content);
        this.recipient = recipient;
    }

    /**
     *
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not
     */
    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return recipient.equals(pp);
    }
}
