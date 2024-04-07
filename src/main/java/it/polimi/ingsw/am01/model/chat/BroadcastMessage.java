package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public class BroadcastMessage extends Message{
    public BroadcastMessage(PlayerProfile sender, String content) {
        super(sender, content);
    }

    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return !pp.equals(super.getSender());
    }
}
