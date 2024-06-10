package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Border;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.List;
import java.util.stream.Collectors;

public class GamesListScene implements Composition {

    private final TuiView view;

    public GamesListScene(TuiView view) {
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
                new FlexChild.Flexible(1,
                        new Column(
                                view.getGames().isEmpty() ? List.of(Centered.both(new Text("No game found"))) :
                                        view.getGames().entrySet().stream()
                                                .map(entry -> {
                                                    String t = "Game #%d (%d/%d)".formatted(
                                                            entry.getKey(),
                                                            entry.getValue().currentPlayersConnected(),
                                                            entry.getValue().maxPlayers()
                                                    );
                                                    return Centered.both(new Text(t));
                                                })
                                                .collect(Collectors.toList())
                        )
                )));
    }
}
