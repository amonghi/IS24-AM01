package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<Message> messages;

    public Chat() {
        messages = new ArrayList<>();
    }


    public void send(Message message) {
        messages.add(message);
    }

    public List<Message> getMailbox(PlayerProfile pp) {
        return messages.stream()
                .filter(m -> m.isRecipient(pp))
                .toList();
    }
}
