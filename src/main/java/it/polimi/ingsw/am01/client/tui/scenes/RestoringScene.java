package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This scene is shown when a player reconnects following a crash server
 * and has to wait for other players before he can resume the game.
 *
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public class RestoringScene extends Composition {

    private final TuiView view;

    public RestoringScene(TuiView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component compose() {
        List<String> playerSlots = new ArrayList<>(view.getPlayersInGame());

        return Flex.row(List.of(
                new FlexChild.Flexible(1,
                        Flex.column(List.of(
                                new FlexChild.Fixed(
                                        Padding.hv(1, 1,
                                                new Border(Line.Style.DEFAULT,
                                                        new Text("Logged as %s".formatted(view.getPlayerName()))
                                                )
                                        )
                                ),
                                new FlexChild.Fixed(
                                        new Column(List.of(
                                                Centered.horizontally(
                                                        new Text("Awaiting players...")
                                                ),
                                                Centered.horizontally(
                                                        Padding.hv(0, 1,
                                                                new Text("You can resume the game with command 'resume game'")
                                                        )
                                                ),
                                                Centered.horizontally(
                                                        new Text("(if there are at least two players connected)")
                                                )
                                        ))
                                ),
                                new FlexChild.Flexible(1,
                                        Centered.both(
                                                new Row(
                                                        playerSlots.stream()
                                                                .map(
                                                                        player -> Padding.hv(4, 0,
                                                                                new Column(List.of(
                                                                                        new Border(Line.Style.DEFAULT, Padding.hv(4, 2, new Text(player))),
                                                                                        view.isConnected(player) ? new Text(GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN), "Connected") :
                                                                                                new Text(GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED), "Disconnected")
                                                                                ))
                                                                        )
                                                                )
                                                                .collect(Collectors.toList())
                                                )
                                        )
                                )
                        ))
                )
        ));
    }
}
