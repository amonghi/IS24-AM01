package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.Border;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;

public class ChatBox extends Composition {

    private final TuiView view;
    private final List<Component> messagesComponents;

    public ChatBox(TuiView view) {
        this.view = view;
        this.messagesComponents = new ArrayList<>();
        for (View.Message m : view.getMessages()) {
            messagesComponents.add(
                    Padding.hv(0, 1,
                            new ChatMessage(m, view)
                    )
            );
        }
    }

    @Override
    protected Component compose() {
        return new Border(Line.Style.DEFAULT,
                messagesComponents.isEmpty() ? new Text("No messages yet") : new Column(messagesComponents)
        );
    }
}
