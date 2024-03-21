package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<Message> messages;

    public Chat() {
        messages = new ArrayList<>();
    }


    public void sendDirectMessage(PlayerProfile sender, PlayerProfile recipient, String content){
        messages.add(new DirectMessage(sender, recipient, content));
    }

    public void sendBroadcastMessage(PlayerProfile sender, String content){
        messages.add(new BroadcastMessage(sender, content));
    }


    public List<Message> getMailbox(PlayerProfile pp){
        return messages.stream()
                .filter(m -> m.isRecipient(pp))
                .toList();
    }
}
