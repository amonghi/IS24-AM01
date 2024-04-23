package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Objects;

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
     */
    public DirectMessage(PlayerProfile sender, PlayerProfile recipient, String content) {
        super(sender, content);
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
    public String toString() {
        return "DirectMessage{"
                + "(" + super.getSender().getName() + " -> " + recipient.getName() + "):"
                + super.getContent()
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
                getContent().equals(that.getContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(recipient, getSender(), getContent());
    }
}
