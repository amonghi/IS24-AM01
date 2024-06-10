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

public class SelectObjectiveScene implements Composition {

    private final TuiView view;

    public SelectObjectiveScene(TuiView view) {
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
                                        new Text("Select your secret objective with command 'select objective'")
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
                                                view.getPlayersHaveChosenSecretObjective().isEmpty() ? List.of(Padding.around(1, new Text("Nobody has chosen yet"))) :
                                                        view.getPlayersHaveChosenSecretObjective()
                                                                .stream()
                                                                .map(player -> Padding.around(1,
                                                                        new Text(player + " has chosen his secret objective")
                                                                ))
                                                                .collect(Collectors.toList())
                                        )
                                )
                        )
                )
        ));
    }
}
