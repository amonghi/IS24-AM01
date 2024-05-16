package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * A generic message to send in the chat
 *
 * @see it.polimi.ingsw.am01.model.chat.DirectMessage
 * @see it.polimi.ingsw.am01.model.chat.BroadcastMessage
 */
public abstract class Message {
    private final PlayerProfile sender;
    private final String content;
    private final LocalDateTime timestamp;

    /**
     * Constructs a new {@code Message} and set his timestamp to {@code LocalDateTime.now()}
     *
     * @param sender  The {@link PlayerProfile} who send the {@code Message}
     * @param content The content of the {@code Message}
     */
    public Message(PlayerProfile sender, String content) {
        this.sender = sender;
        this.content = content;
        timestamp = LocalDateTime.now();
    }

    /**
     * @return A string representing the content of the {@code Message}
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the {@link PlayerProfile} of the {@code Message}'s sender
     */
    public PlayerProfile getSender() {
        return sender;
    }

    /**
     * @return the timestamp (as a {@code LocalDateTime}) of the {@code Message}
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not
     */
    public abstract boolean isRecipient(PlayerProfile pp);

    /**
     * @return The type of message
     * @see MessageType
     */
    public abstract MessageType getMessageType();

    /**
     * Provides recipient of the {@code Message}, if it is defined
     *
     * @return the recipient of the message if it is defined, otherwise {@code Optional.empty()}
     * @see DirectMessage
     * @see BroadcastMessage
     */
    public Optional<PlayerProfile> getRecipient() {
        return Optional.empty();
    }
}
