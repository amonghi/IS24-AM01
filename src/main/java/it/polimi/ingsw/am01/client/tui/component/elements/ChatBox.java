package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;

public class ChatBox extends Composition {

    private final List<Component> messagesComponents;

    public ChatBox(TuiView view) {
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
                new Column(List.of(
                        Centered.horizontally(Padding.hv(0, 1, new Text("Chat"))),
                        messagesComponents.isEmpty()
                                ? new Text("No messages yet")
                                : new ChatScrollable(messagesComponents)
                ))
        );
    }

    protected static class ChatScrollable extends Layout {
        public ChatScrollable(List<Component> messagesComponents) {
            super(messagesComponents.reversed());
        }

        @Override
        public void layout(Constraint constraint) {
            Dimensions d = constraint.max();
            this.setDimensions(d);

            int y = constraint.max().height();

            for (int i = 0; i < this.children().size() && y >= 0; i++) {
                Component child = this.children().get(i);

                Constraint availableSpace = Constraint.max(Dimensions.of(constraint.max().width(), y));
                child.layout(availableSpace);

                int childTop = y - child.dimensions().height();
                child.setPosition(Position.of(0, childTop));

                y = childTop;
            }
        }
    }
}
