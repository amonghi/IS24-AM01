package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.chat.MessageType;
import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record NewMessageS2C(MessageType messageType, String timestamp, String sender,
                            String content) implements S2CNetworkMessage {
    public static final String ID = "NEW_MESSAGE";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
