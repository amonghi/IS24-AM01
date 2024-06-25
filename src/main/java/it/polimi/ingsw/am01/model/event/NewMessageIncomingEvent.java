package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.chat.Message;

/**
 * This event is emitted when a player sends a new message.
 * @param message the new message
 * @see it.polimi.ingsw.am01.model.chat.ChatManager#send(Message) ChatManager.send(message)
 * @see Message
 */
public record NewMessageIncomingEvent(Message message) implements ChatEvent {
}
