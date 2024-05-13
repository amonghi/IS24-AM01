package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.event.ChatEvent;
import it.polimi.ingsw.am01.model.event.NewMessageIncomingEvent;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The handler of the chat, allowing to send a message and view the MailBox of a specific {@link PlayerProfile}
 */
public class ChatManager implements EventEmitter<ChatEvent> {
    private final List<Message> messages;
    transient private EventEmitterImpl<ChatEvent> emitter;

    /**
     * Constructs a new {@code ChatManager} initializing an empty {@code List} to store the {@link Message}s
     */
    public ChatManager() {
        messages = new ArrayList<>();
        this.emitter = new EventEmitterImpl<>();
    }

    /**
     * Implements the event emitter if null
     *
     * @return The event emitter
     */
    private EventEmitterImpl<ChatEvent> getEmitter() {
        if (emitter == null) {
            emitter = new EventEmitterImpl<>();
        }
        return emitter;
    }

    /**
     * Send the message storing it in the messages {@code List}
     *
     * @param message The message to send
     */
    public synchronized void send(Message message) {
        messages.add(message);

        getEmitter().emit(new NewMessageIncomingEvent(message));
    }

    /**
     * @param pp The {@code PlayerProfile} for which to return the mailbox
     * @return An ordered {@code List} of messages sent by {@link PlayerProfile} and received by {@link PlayerProfile}.
     * Message order is based on timestamps
     */
    public synchronized List<Message> getMailbox(PlayerProfile pp) {
        return messages.stream()
                .filter(m -> m.isRecipient(pp) || m.getSender().equals(pp))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "ChatManager{" +
                messages +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatManager that = (ChatManager) o;
        return messages.equals(that.messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int hashCode() {
        return messages.hashCode();
    }

    @Override
    public Registration onAny(EventListener<ChatEvent> listener) {
        return getEmitter().onAny(listener);
    }


    @Override
    public <T extends ChatEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return getEmitter().on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return getEmitter().unregister(registration);
    }
}
