package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.ViewUtils;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.Utils;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.*;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayAreaScene extends Composition {

    private final TuiView view;

    public PlayAreaScene(TuiView view) {
        this.view = view;
    }

    @Override
    protected Component compose() {
        List<FlexChild> children = new ArrayList<>();
        Map<DeckLocation, Card> decks = view.getDecksColor().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> GameAssets.getInstance().getCardById(
                        ViewUtils.getIdFromColorAndDeckLocation(entry.getKey(), entry.getValue())
                ).orElseThrow())
        );

        List<Card> faceUpCards = view.getFaceUpCards().stream().map(id -> GameAssets.getInstance().getCardById(id).orElseThrow()).toList();

        String gameStatusText = view.getCurrentPlayer() + " is " + switch (view.getTurnPhase()) {
            case PLACING -> "placing";
            case DRAWING -> "drawing";
        };

        if (view.getGameStatus().equals(GameStatus.SUSPENDED)) {
            gameStatusText = "Game is suspended";
        }

        List<Component> faceUpCardComponents = new ArrayList<>();

        for (int i = 0; i < faceUpCards.size() / 2; i++) {
            List<Component> column = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                column.add(new CardFaceComponent(
                        faceUpCards.get(2 * i + j).getFace(Side.FRONT),
                        faceUpCards.get(2 * i + j).color()
                ));
            }
            faceUpCardComponents.add(new Column(column));
        }

        List<Component> hand = new ArrayList<>();

        for (int i = 0; i < view.getHand().size(); i++) {
            Card card = GameAssets.getInstance().getCardById(view.getHand().get(i)).orElseThrow();
            hand.add(new CardFaceComponent(card.getFace(view.getVisibleSideOf(i)), card.color()));
        }


        List<Component> board = new ArrayList<>(List.of(
                new Padding(0, 0, 4, 0, new Row( // TODO: empty deck
                        List.of(
                                new Column(List.of(
                                        new Text("Golden deck"),
                                        new CardFaceComponent(
                                                decks.get(DeckLocation.GOLDEN_CARD_DECK).getFace(Side.BACK),
                                                decks.get(DeckLocation.GOLDEN_CARD_DECK).color())
                                )),
                                new Column(List.of(
                                        new Text("Resource deck"),
                                        new CardFaceComponent(
                                                decks.get(DeckLocation.RESOURCE_CARD_DECK).getFace(Side.BACK),
                                                decks.get(DeckLocation.RESOURCE_CARD_DECK).color())
                                ))
                        )
                )),
                new Text("Face up cards")
        ));

        if (!faceUpCards.isEmpty()) {
            board.add(
                    new Row(faceUpCardComponents)
            );
        }

        if (view.isBoardVisible()) {
            children.add(
                    new FlexChild.Fixed(
                            new Border(Line.Style.DEFAULT,
                                    Centered.vertically(new Column(
                                            board
                                    ))
                            )
                    )

            );
        } else if (view.areObjectivesVisible()) {
            children.add(
                    new FlexChild.Fixed(
                            new Column(List.of(
                                    //objectives
                            ))
                    )
            );
        }


        children.add(
                new FlexChild.Flexible(1,
                        Flex.column(
                                List.of(
                                        new FlexChild.Flexible(1, new PlayAreaComponent(
                                                view.getPlayAreaScrollX(),
                                                view.getPlayAreaScrollY(),
                                                view.getPlacements(view.getFocusedPlayer().orElse(view.getPlayerName())),
                                                view.getPlayablePositions()
                                        )),
                                        new FlexChild.Fixed(Centered.horizontally(new Row(hand)))
                                )
                        )
                )
        );

        if (view.isChatVisible()) {
            children.add(
                    new FlexChild.Fixed(
                            new ChatBox(view)
                    )
            );
        }

        return Flex.column(List.of(
                        new FlexChild.Fixed(Flex.row(List.of(
                                new FlexChild.Fixed(
                                        Padding.hv(1, 1,
                                                new Border(Line.Style.DEFAULT,
                                                        new Text("Logged as %s".formatted(view.getPlayerName()))
                                                )
                                        )
                                ),
                                new FlexChild.Fixed(
                                        Centered.horizontally(
                                                Padding.hv(1, 1,
                                                        new Text(
                                                                view.getGameStatus().equals(GameStatus.SUSPENDED)
                                                                        ? GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE)
                                                                        : Utils.getPlayerColorRendition(view.getPlayerColors().get(view.getCurrentPlayer())), gameStatusText
                                                        )
                                                )
                                        )
                                )
                        ))),
                        new FlexChild.Flexible(1,
                                Flex.row(children)
                        )
                )
        );
    }
}
