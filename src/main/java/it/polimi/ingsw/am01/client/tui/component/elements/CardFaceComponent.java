package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.Utils;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardFaceComponent extends Element {
    public static final int CARD_W = 25;
    public static final int CARD_H = 9;
    public static final int CORNER_W = 6;
    public static final int CORNER_H = 3;
    private static final String CORNER_COVER_POINTS_SYMBOL = "▅██";

    private static final GraphicalRendition GOLDEN_RENDITION = GraphicalRendition.DEFAULT
            .withForeground(GraphicalRenditionProperty.ForegroundColor.YELLOW);

    private final CardFace face;
    private final CardColor cardColor;
    private final boolean isGolden;

    public CardFaceComponent(CardFace face, CardColor cardColor, boolean isGolden) {
        super(Dimensions.of(CARD_W, CARD_H));
        this.face = face;
        this.cardColor = cardColor;
        this.isGolden = isGolden;
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        a.setRendition(Utils.getCardColorRendition(cardColor));
        Line.rectangle(a, Position.ZERO, a.dimensions(), Line.Style.ROUNDED);

        drawPoints(a);
        drawCenterResources(a);
        drawPlacementConstraint(a);
        drawCorners(a);
    }

    private void drawPoints(DrawArea a) {
        if (this.face.getPoints().isEmpty()) {
            return;
        }

        String pointsString = switch (this.face.getPoints().get()) {
            case SimplePoints p -> Integer.toString(p.getPoints());
            case ItemPoints p -> p.getPointsPerItem() + Utils.getItemEmoji(p.getItem());
            case CornerCoverPoints p -> p.getPointsPerCorner() + " " + CORNER_COVER_POINTS_SYMBOL;
        };

        a.setRendition(GOLDEN_RENDITION);
        Text.writeCentered(a, CARD_W / 2, 1, pointsString);
    }

    private void drawCenterResources(DrawArea a) {
        String centerResourcesString = resourceMapToString(this.face.getCenterResources());

        a.foreground(GraphicalRenditionProperty.ForegroundColor.DEFAULT);
        Text.writeCentered(a, CARD_W / 2, CARD_H / 2, centerResourcesString);
    }

    private void drawPlacementConstraint(DrawArea a) {
        if (this.face.getPlacementConstraint().isEmpty()) {
            return;
        }

        PlacementConstraint constraint = this.face.getPlacementConstraint().get();
        String requiredResourcesString = resourceMapToString(constraint.getRequiredResources());

        a.foreground(GraphicalRenditionProperty.ForegroundColor.DEFAULT);
        Text.writeCentered(a, CARD_W / 2, CARD_H - 2, requiredResourcesString);
    }

    private String resourceMapToString(Map<Resource, Integer> resourceMap) {
        return resourceMap.entrySet().stream()
                .flatMap(resourceIntegerEntry ->
                        Stream.generate(resourceIntegerEntry::getKey)
                                .limit(resourceIntegerEntry.getValue())
                )
                .map(Utils::getResourceEmoji)
                .collect(Collectors.joining());
    }

    private void drawCorners(DrawArea a) {
        GraphicalRendition possiblyGoldenColor = this.isGolden
                ? GOLDEN_RENDITION
                : Utils.getCardColorRendition(this.cardColor);
        a.setRendition(possiblyGoldenColor);

        for (CornerPosition cp : CornerPosition.values()) {
            Corner corner = this.face.corner(cp);
            if (corner.isSocket()) {
                String emoji = corner.getCollectible().map(Utils::getCollectibleEmoji).orElse(null);
                this.drawCorner(a, cp.isTop(), cp.isLeft(), emoji);
            }
        }
    }

    private void drawCorner(DrawArea a, boolean cardT, boolean cardL, String emoji) {
        if (emoji != null && emoji.length() != 2) {
            throw new IllegalArgumentException("Emoji must be 2 characters");
        }

        Line.Style style = Line.Style.DEFAULT;

        // cardT and cardB indicate on what corner we are drawing, inside the card
        // t, b, l, r indica the position of top, bottom, left and right "inner corners"
        int t = cardT ? 0 : CARD_H - CORNER_H;
        int b = cardT ? CORNER_H - 1 : CARD_H - 1;
        int l = cardL ? 0 : CARD_W - CORNER_W;
        int r = cardL ? CORNER_W - 1 : CARD_W - 1;

        // vertical line
        Line.down(a,
                Position.of(cardL ? r : l, t),
                CORNER_H,
                style.tb()
        );
        // horizontal line
        Line.right(a,
                Position.of(l, cardT ? b : t),
                CORNER_W,
                style.lr()
        );

        // inner corner
        a.draw(
                Position.of(cardL ? r : l, cardT ? b : t),
                style.get(cardT, !cardT, cardL, !cardL)
        );
        // junction with horizontal
        a.draw(
                Position.of(cardL ? r : l, cardT ? t : b),
                style.get(!cardT, cardT, true, true)
        );
        // junction with vertical
        a.draw(
                Position.of(cardL ? l : r, cardT ? b : t),
                style.get(true, true, !cardL, cardL)
        );

        // emoji
        if (emoji != null) {
            Text.write(a, l + 2, t + 1, emoji);
        }
    }
}
