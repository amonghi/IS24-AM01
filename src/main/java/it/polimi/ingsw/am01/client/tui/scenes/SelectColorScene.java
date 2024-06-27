package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.Utils;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.ChatBox;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Border;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.component.layout.Row;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This scene permits players to select color ({@code SETUP_COLOR} status).
 *
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public class SelectColorScene extends Composition {

    private final TuiView view;

    public SelectColorScene(TuiView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component compose() {

        List<FlexChild> children = new ArrayList<>();

        children.add(
                new FlexChild.Flexible(5,
                        Flex.column(List.of(
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
                                new FlexChild.Flexible(1,
                                        Centered.both(
                                                new Row(
                                                        view.getPlayersInGame()
                                                                .stream()
                                                                .map(
                                                                        player -> Padding.hv(4, 0,
                                                                                new Border(Line.Style.DEFAULT, Padding.hv(4, 2, new Text(
                                                                                        view.getPlayerColors().containsKey(player)
                                                                                                ? Utils.getPlayerColorRendition(view.getPlayerColor(player))
                                                                                                : GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE)
                                                                                        ,
                                                                                        player
                                                                                )))
                                                                        )
                                                                )
                                                                .collect(Collectors.toList())
                                                )
                                        )
                                )
                        ))
                )
        );

        if (view.isChatVisible()) {
            children.add(
                    new FlexChild.Flexible(1, new ChatBox(view))
            );
        }

        return Flex.row(children);
    }
}
