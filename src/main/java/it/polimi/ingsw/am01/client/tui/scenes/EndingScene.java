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
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class EndingScene implements Composition {

    private final TuiView view;

    public EndingScene(TuiView view) {
        this.view = view;
    }

    @Override
    public Component compose() {
        SortedMap<String, Integer> finalPlacements = view.getFinalPlacements();
        Map<String, Integer> finalScores = view.getFinalScores();

        List<String> players = new ArrayList<>(finalPlacements.keySet());

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
                                        new Text("Game finished")
                                )
                        )
                ),
                new FlexChild.Flexible(1,
                        Centered.horizontally(
                                new Border(Line.Style.DEFAULT,
                                        new Column(
                                                players.stream().map(player ->
                                                        new Row(
                                                                List.of(
                                                                        new Text(finalPlacements.get(player) + "."),
                                                                        new Padding(0, 15, 0, 3, new Text(player)),
                                                                        new Text(finalScores.get(player).toString())
                                                                )
                                                        )
                                                ).collect(Collectors.toList())
                                        )
                                )
                        )
                )
        ));
    }
}