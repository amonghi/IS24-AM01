package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.gui.model.Placement;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.component.layout.Scroll;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.game.GameAssets;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Stream;

public class PlayAreaComponent extends Composition {
    private final int xScroll;
    private final int yScroll;
    private final SortedSet<Placement> playArea;
    private final List<it.polimi.ingsw.am01.client.gui.model.Position> playablePositions;

    public PlayAreaComponent(
            int xScroll,
            int yScroll,
            SortedSet<Placement> playArea,
            List<it.polimi.ingsw.am01.client.gui.model.Position> playablePositions
    ) {
        this.xScroll = xScroll;
        this.yScroll = yScroll;
        this.playArea = playArea;
        this.playablePositions = playablePositions;
    }

    @Override
    protected Component compose() {
        return new Scroll(this.xScroll, this.yScroll, new PlayAreaScrollable(new ArrayList<>(playArea), this.playablePositions));
    }

    protected static class PlayAreaScrollable extends BaseLayout {
        private static final int X_OFFSET = CardFaceComponent.CARD_W - CardFaceComponent.CORNER_W;
        private static final int Y_OFFSET = CardFaceComponent.CARD_H - CardFaceComponent.CORNER_H;

        private final List<it.polimi.ingsw.am01.client.gui.model.Position> positions;
        private final List<Component> children;

        protected PlayAreaScrollable(
                List<Placement> playArea,
                List<it.polimi.ingsw.am01.client.gui.model.Position> playablePositions
        ) {
            // note: put the playable positions first, so that they are rendered below the cards
            this.positions = Stream.concat(
                    playablePositions.stream(),
                    playArea.stream().map(Placement::pos)
            ).toList();

            Stream<Component> playablePositionsStream = playablePositions.stream()
                    .map(PlayablePositionComponent::new);

            Stream<Component> cardsStream = playArea.stream()
                    .map(placement -> {
                        Card card = GameAssets.getInstance()
                                .getCardById(placement.id())
                                .orElseThrow();
                        CardFace face = card.getFace(placement.side());
                        CardColor cardColor = card.color();

                        return new CardFaceComponent(face, cardColor);
                    });

            this.children = Stream.concat(playablePositionsStream, cardsStream).toList();
        }

        @Override
        public void layout(Constraint constraint) {
            int minX = this.positions.stream().mapToInt(PlayAreaScrollable::getX).min().orElse(0);
            int minY = this.positions.stream().mapToInt(PlayAreaScrollable::getY).min().orElse(0);

            int xPositiveOffset = Math.max(-minX, 0);
            int yPositiveOffset = Math.max(-minY, 0);

            int playAreaW = 0;
            int playAreaH = 0;

            for (int i = 0; i < positions.size(); i++) {
                it.polimi.ingsw.am01.client.gui.model.Position cardPos = positions.get(i);
                Component child = children.get(i);

                // since this component is private, and it gets always rendered inside a Scroll,
                // we are just going to assume that there is always enough space,
                // so it's not really relevant which constraint we pass to the children
                child.layout(constraint);

                Position childPos = new Position(
                        (xPositiveOffset + getX(cardPos)) * X_OFFSET,
                        (yPositiveOffset + getY(cardPos)) * Y_OFFSET
                );
                child.setPosition(childPos);

                // update playArea W and H to ensure that it contains the component
                Position childBottomRight = childPos.add(child.dimensions().width(), child.dimensions().height());
                if (playAreaW < childBottomRight.x()) {
                    playAreaW = childBottomRight.x();
                }
                if (playAreaH < childBottomRight.y()) {
                    playAreaH = childBottomRight.y();
                }
            }

            // again, we do not care about the constraints because we assume that this component
            // is always going to be rendered inside a Scroll component
            this.setDimensions(new Dimensions(playAreaW, playAreaH));
        }

        @Override
        protected List<Component> children() {
            return this.children;
        }

        private static int getX(it.polimi.ingsw.am01.client.gui.model.Position position) {
            int i = position.i();
            int j = position.j();

            return i - j;
        }

        private static int getY(it.polimi.ingsw.am01.client.gui.model.Position position) {
            int i = position.i();
            int j = position.j();

            return -i - j;
        }
    }
}
