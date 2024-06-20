package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.model.chat.MessageType;

import java.util.List;

public class ChatMessage extends Composition {

    private final TuiView view;
    private final View.Message message;

    public ChatMessage(View.Message message, TuiView view) {
        this.message = message;
        this.view = view;
    }

    @Override
    protected Component compose() {
        String recipient = message.type() == MessageType.DIRECT
                ? message.recipient()
                : "everyone";

        // TODO: show date

        return new Column(List.of(
                new Text(message.sender() + " to " + recipient + ":"),
                new Text(message.content())
        ));
    }
}
