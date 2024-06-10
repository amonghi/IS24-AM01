package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Border;
import it.polimi.ingsw.am01.client.tui.component.elements.CardFaceComponent;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameAssets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectStartingCardSideScene implements Composition {

    private final TuiView view;

    public SelectStartingCardSideScene(TuiView view) {
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
                                Padding.around(3,
                                        new Text("Select a side with command 'select side'")
                                )
                        )
                ),
                new FlexChild.Flexible(2,
                        Flex.row(
                                Arrays.stream(Side.values())
                                        .map(side -> new FlexChild.Flexible(1,
                                                new Column(List.of(
                                                        Centered.horizontally(
                                                                new Text(side.toString())
                                                        ),
                                                        Centered.horizontally(
                                                                new CardFaceComponent(
                                                                        GameAssets.getInstance().getCardById(
                                                                                view.getStartingCardId()
                                                                        ).get().getFace(side)//FIXME: cannot be empty
                                                                )
                                                        )
                                                )
                                                )))
                                        .collect(Collectors.toList())
                        )
                ),
                new FlexChild.Fixed(
                        Centered.horizontally(new Text("Choices"))
                ),
                new FlexChild.Flexible(1,
                        Centered.horizontally(
                                new Border(Line.Style.DEFAULT,
                                        new Column(
                                                view.getStartingCardPlacements().isEmpty() ? List.of(Padding.around(1, new Text("Nobody has chosen yet"))) :
                                                        view.getStartingCardPlacements()
                                                                .entrySet()
                                                                .stream()
                                                                .map(entry -> Padding.around(1,
                                                                        new Text(entry.getKey()
                                                                                + " has chosen "
                                                                                + entry.getValue().side().toString().toLowerCase()
                                                                                + " of card "
                                                                                + entry.getValue().id()
                                                                        )
                                                                ))
                                                                .collect(Collectors.toList())
                                        )
                                )
                        )
                )
        ));
    }
}
