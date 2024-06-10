package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Border;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.component.layout.Row;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.model.player.PlayerColor;

import java.util.List;
import java.util.stream.Collectors;

public class SelectColorScene implements Composition {

    private final TuiView view;

    public SelectColorScene(TuiView view) {
        this.view = view;
    }

    @Override
    public Component compose() {
        return Flex.column(List.of(
                new FlexChild.Fixed(
                        Padding.hv(1, 1,
                                new Border(Line.Style.DEFAULT,
                                        new Text("Logged as %s".formatted(view.getPlayerName()))
                                )
                        )
                ),
                new FlexChild.Fixed(
                        Centered.horizontally(
                                Padding.hv(0, 3,
                                        new Text("Select your color with command 'select color'")
                                )
                        )
                ),
                new FlexChild.Fixed(
                        Centered.horizontally(new Text("Choices"))
                ),
                new FlexChild.Flexible(1,
                        Centered.horizontally(
                                new Border(Line.Style.DEFAULT,
                                        new Column(
                                                view.getPlayerColors().isEmpty() ? List.of(Padding.around(1, new Text("Nobody has chosen yet"))) :
                                                        view.getPlayerColors()
                                                                .entrySet()
                                                                .stream()
                                                                .map(entry -> Padding.around(1,
                                                                        new Row(List.of(
                                                                                new Text(entry.getKey()
                                                                                        + " has chosen "),
                                                                                new Text(getGraphicalRendition(entry.getValue()), entry.getValue().toString().toLowerCase())
                                                                        )))
                                                                )
                                                                .collect(Collectors.toList())
                                        )
                                )
                        )
                )
        ));
    }

    private GraphicalRendition getGraphicalRendition(PlayerColor color) {
        return switch (color) {
            case RED -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED);
            case BLUE -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.BLUE);
            case GREEN -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN);
            case YELLOW -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.YELLOW);
        };
    }
}
