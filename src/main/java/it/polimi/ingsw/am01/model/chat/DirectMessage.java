package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.exception.MessageSentToThemselvesException;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code Message} for a specific {@link PlayerProfile} connected to the game
 *
 * @see it.polimi.ingsw.am01.model.chat.Message
 */
public class DirectMessage extends Message {
    private final PlayerProfile recipient;

    /**
     * Construct a new direct message
     *
     * @param sender    The {@link PlayerProfile} who send the {@code Message}
     * @param recipient The {@link PlayerProfile} who receive the {@code Message}
     * @param content   The content of the {@code Message}
     * @throws MessageSentToThemselvesException if {@code sender} is equal to {@code recipient}
     */
    public DirectMessage(PlayerProfile sender, PlayerProfile recipient, String content) throws MessageSentToThemselvesException {
        super(sender, content);

        if (sender.equals(recipient)) {
            throw new MessageSentToThemselvesException(sender);
        }

        this.recipient = recipient;
    }

    /**
     * @param pp The {@link PlayerProfile} to determine if he or she is a recipient
     * @return Whether the specified {@link PlayerProfile} is a recipient or not
     */
    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return recipient.equals(pp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageType getMessageType() {
        return MessageType.DIRECT;
    }

    /**
     *
     * @return the recipient of the message
     * @see Message
     */
    @Override
    public Optional<PlayerProfile> getRecipient() {
        return Optional.ofNullable(recipient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DirectMessage{"
                + getTimestamp().toLocalTime().truncatedTo(ChronoUnit.SECONDS)
                + " (" + getSender().name() + " -> " + recipient.name() + "):"
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

        DirectMessage that = (DirectMessage) o;
        return recipient.equals(that.recipient) &&
                getSender().equals(that.getSender()) &&
                getContent().equals(that.getContent()) &&
                getTimestamp().equals(that.getTimestamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(recipient, getSender(), getContent(), getTimestamp());
    }
}
