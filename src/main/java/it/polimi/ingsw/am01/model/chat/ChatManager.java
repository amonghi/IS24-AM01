package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * The handler of the chat, allowing to send a message and view the MailBox of a specific {@link PlayerProfile}
 */
public class ChatManager {
    private final List<Message> messages;

    /**
     * Constructs a new {@code ChatManager} initializing an empty {@code List} to store the {@link Message}s
     */
    public ChatManager() {
        messages = new ArrayList<>();
    }

    /**
     * Send the message storing it in the messages {@code List}
     * @param message The message to send
     */
    public void send(Message message) {
        messages.add(message);
    }

    /**
     *
     * @param pp The {@code PlayerProfile} for which to return the mailbox
     * @return The {@code List} of messages for which the specified {@link PlayerProfile} is recipient
     */
    public List<Message> getMailbox(PlayerProfile pp) {
        return messages.stream()
                .filter(m -> m.isRecipient(pp))
                .toList();
    }
}
