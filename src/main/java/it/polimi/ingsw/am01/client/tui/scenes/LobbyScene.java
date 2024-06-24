package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.ChatBox;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyScene extends Composition {

    private final TuiView view;

    public LobbyScene(TuiView view) {
        this.view = view;
    }

    public Component compose() {
        List<String> playerSlots = new ArrayList<>(view.getPlayersInGame());
        for (int i = 0; i < view.getMaxPlayers() - view.getPlayersInGame().size(); i++) {
            playerSlots.add("  ");
        }

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
                                                new Column(List.of(
                                                        new Text("Awaiting players..."),
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
