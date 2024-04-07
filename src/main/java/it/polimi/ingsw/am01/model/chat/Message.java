package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * A generic message to send in the chat
 *
 * @see it.polimi.ingsw.am01.model.chat.DirectMessage
 * @see it.polimi.ingsw.am01.model.chat.BroadcastMessage
 */
public abstract class Message {
    private final PlayerProfile sender;
    private final String content;

    /**
     * Constructs a new {@code Message}
     * @param sender The {@link PlayerProfile} who send the {@code Message}
     * @param content The content of the {@code Message}
     */
    public Message(PlayerProfile sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    /**
     *
     * @return A string representing the content of the {@code Message}
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @return the {@link PlayerProfile} of the {@code Message}'s sender
     */
    public PlayerProfile getSender() {
        return sender;
    }

    /**
     *
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not
     */
    public abstract boolean isRecipient(PlayerProfile pp);
}
