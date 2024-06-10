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
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyScene implements Composition {

    private final TuiView view;

    public LobbyScene(TuiView view) {
        this.view = view;
    }

    public Component compose() {
        List<String> playerSlots = new ArrayList<>(view.getPlayersInGame());
        for (int i = 0; i < view.getMaxPlayers() - view.getPlayersInGame().size(); i++) {
            playerSlots.add("  ");
        }


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
                                new Column(List.of(
                                        new Text("Waiting players..."),
                                        Padding.hv(0, 1,
                                                new Text("%d/%d players joined"
                                                        .formatted(
                                                                view.getPlayersInGame().size(),
                                                                view.getMaxPlayers()
                                                        )
                                                )
                                        )
                                ))

                        )
                ),
                new FlexChild.Flexible(1,
                        Centered.both(
                                new Row(
                                        playerSlots.stream()
                                                .map(
                                                        player -> Padding.hv(4, 0, new Border(Line.Style.DEFAULT, Padding.hv(4, 2, new Text(player))))
                                                )
                                                .collect(Collectors.toList())
                                )
                        )
                )
        ));
    }
}
