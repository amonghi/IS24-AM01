package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * A {@code Message} for every {@link PlayerProfile} connected to the game
 *
 * @see it.polimi.ingsw.am01.model.chat.Message
 */
public class BroadcastMessage extends Message {

    /**
     * Construct a new broadcast {@code Message}
     *
     * @param sender  The {@link PlayerProfile} who send the {@code Message}
     * @param content The content of the {@code Message}
     */
    public BroadcastMessage(PlayerProfile sender, String content) {
        super(sender, content);
    }

    /**
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not.
     * The sender is not considered as a valid recipient
     */
    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return !pp.equals(super.getSender());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageType getMessageType() {
        return MessageType.BROADCAST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BroadcastMessage{"
                + getTimestamp().toLocalTime().truncatedTo(ChronoUnit.SECONDS)
                + " (" + getSender().getName() + "):"
                + getContent()
                + "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BroadcastMessage that = (BroadcastMessage) o;
        return getSender().equals(that.getSender()) &&
                getContent().equals(that.getContent()) &&
                getTimestamp().equals(that.getTimestamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getContent(), getTimestamp());
    }
}
