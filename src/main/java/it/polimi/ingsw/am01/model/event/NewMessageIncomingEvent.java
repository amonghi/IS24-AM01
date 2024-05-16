package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.chat.Message;

public record NewMessageIncomingEvent(Message message) implements ChatEvent {
}
