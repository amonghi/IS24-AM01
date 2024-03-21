package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public abstract class Message {
    private PlayerProfile sender;
    private String content;

    public Message(PlayerProfile sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public PlayerProfile getSender() {
        return sender;
    }

    public abstract boolean isRecipient(PlayerProfile pp);
}
