package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.model.chat.MessageType;

import java.time.LocalDateTime;
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
        LocalDateTime localDateTime = LocalDateTime.parse(message.timestamp());
        String hour = "%s%d".formatted(localDateTime.getHour() < 10 ? "0" : "", localDateTime.getHour());
        String minute = "%s%d".formatted(localDateTime.getMinute() < 10 ? "0" : "", localDateTime.getMinute());
        String second = "%s%d".formatted(localDateTime.getSecond() < 10 ? "0" : "", localDateTime.getSecond());

        String header = message.type() == MessageType.DIRECT
                ? (
                message.recipient().equals(view.getPlayerName())
                        ? message.sender() + " to you"
                        : "You to " + message.recipient()
        )
                : message.sender();


        GraphicalRendition directRendition = GraphicalRendition.DEFAULT
                .withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE)
                .withWeight(GraphicalRenditionProperty.Weight.DIM)
                .withItalics(GraphicalRenditionProperty.Italics.ON);


        return new Column(List.of(
                message.type() == MessageType.DIRECT
                        ? new Paragraph(directRendition, "(" + hour + ":" + minute + ":" + second + ") " + header + ": " + message.content())
                        : new Paragraph("(" + hour + ":" + minute + ":" + second + ") " + header + ": " + message.content())
        ));
    }
}
