package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.CardFaceComponent;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameAssets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectStartingCardSideScene extends Composition {

    private final TuiView view;

    public SelectStartingCardSideScene(TuiView view) {
        this.view = view;
    }

    @Override
    public Component compose() {
        Card startingCard = GameAssets.getInstance()
                .getCardById(view.getStartingCardId())
                .orElseThrow();

        //TODO: add chat component
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
                new FlexChild.Flexible(1,
                        Flex.row(
                                Arrays.stream(Side.values())
                                        .map(side -> new FlexChild.Flexible(1,
                                                new Column(List.of(
                                                        Centered.horizontally(
                                                                new Text(side.toString())
                                                        ),
                                                        Centered.horizontally(
                                                                new CardFaceComponent(startingCard, side)
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
                                        new Row(
                                                view.getStartingCardPlacements().isEmpty() ? List.of(Padding.around(1, new Text("Nobody has chosen yet"))) :
                                                        view.getStartingCardPlacements()
                                                                .entrySet()
                                                                .stream()
                                                                .map(e -> new Column(List.of(
                                                                        new Text(e.getKey()),
                                                                        Padding.around(1,
                                                                                new CardFaceComponent(
                                                                                        GameAssets.getInstance().getCardById(e.getValue().id()).orElseThrow(),
                                                                                        e.getValue().side()
                                                                                )
                                                                        )
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
