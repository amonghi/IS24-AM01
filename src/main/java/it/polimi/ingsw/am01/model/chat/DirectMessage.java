package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public class DirectMessage extends Message {
    private final PlayerProfile recipient;
    public DirectMessage(PlayerProfile sender, PlayerProfile recipient, String content) {
        super(sender, content);
        this.recipient = recipient;
    }

    @Override
    public boolean isRecipient(PlayerProfile pp) {
        return recipient.equals(pp);
    }
}
