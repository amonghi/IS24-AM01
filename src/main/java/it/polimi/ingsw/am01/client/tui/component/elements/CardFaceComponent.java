package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.*;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardFaceComponent implements Component {
    private static final int CARD_W = 25;
    private static final int CARD_H = 9;
    private static final String CORNER_COVER_POINTS_SYMBOL = "▅██";

    private static char[][] toCharMatrix(String s) {
        String[] lines = s.split("\n");
        char[][] matrix = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            matrix[i] = lines[i].toCharArray();
        }
        return matrix;
    }

    private final CardFace face;

    public CardFaceComponent(CardFace face) {
        this.face = face;
    }

    @Override
    public Sized layout(Constraint constraint) {
        Dimensions d = Dimensions.constrained(constraint, CARD_W, CARD_H);
        return new Sized(this, d, List.of());
    }

    private static final int CORNER_W = 6;
    private static final int CORNER_H = 3;

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
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
            case ItemPoints p -> p.getPointsPerItem() + itemEmoji(p.getItem());
            case CornerCoverPoints p -> p.getPointsPerCorner() + " " + CORNER_COVER_POINTS_SYMBOL;
        };
        Text.writeCentered(a, CARD_W / 2, 1, pointsString);
    }

    private void drawCenterResources(DrawArea a) {
        String centerResourcesString = resourceMapToString(this.face.getCenterResources());
        Text.writeCentered(a, CARD_W / 2, CARD_H / 2, centerResourcesString);
    }

    private void drawPlacementConstraint(DrawArea a) {
        if (this.face.getPlacementConstraint().isEmpty()) {
            return;
        }

        PlacementConstraint constraint = this.face.getPlacementConstraint().get();
        String requiredResourcesString = resourceMapToString(constraint.getRequiredResources());
        Text.writeCentered(a, CARD_W / 2, CARD_H - 2, requiredResourcesString);
    }

    private String resourceMapToString(Map<Resource, Integer> resourceMap) {
        return resourceMap.entrySet().stream()
                .flatMap(resourceIntegerEntry ->
                        Stream.generate(resourceIntegerEntry::getKey)
                                .limit(resourceIntegerEntry.getValue())
                )
                .map(this::resourceEmoji)
                .collect(Collectors.joining());
    }

    private void drawCorners(DrawArea a) {
        for (CornerPosition cp : CornerPosition.values()) {
            Corner corner = this.face.corner(cp);
            if (corner.isSocket()) {
                String emoji = corner.getCollectible().map(this::collectibleEmoji).orElse(null);
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

    private String collectibleEmoji(Collectible cp) {
        return switch (cp) {
            case Item item -> itemEmoji(item);
            case Resource resource -> resourceEmoji(resource);
        };
    }

    private String itemEmoji(Item item) {
        return switch (item) {
            case QUILL -> "🪶";
            case INKWELL -> "🫙";
            case MANUSCRIPT -> "📜";
        };
    }

    private String resourceEmoji(Resource resource) {
        return switch (resource) {
            case PLANT -> "🌱";
            case FUNGI -> "🍄";
            case ANIMAL -> "🐺";
            case INSECT -> "🦋";
        };
    }
}
