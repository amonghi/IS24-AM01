package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.Utils;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;
import it.polimi.ingsw.am01.model.objective.SameCollectibleObjective;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A component that renders an objective card in-game.
 */
public class ObjectiveComponent extends Element {

    public static final int OBJECTIVE_W = 25;
    public static final int OBJECTIVE_H = 9;
    public static final int OBJECTIVE_INNER_CARD_W = 4;
    public static final int OBJECTIVE_INNER_CARD_H = 2;
    private final Objective objective;

    /**
     * Creates a new objective component for the given objective.
     *
     * @param objective the objective that this component represents
     */
    public ObjectiveComponent(Objective objective) {
        super(Dimensions.of(OBJECTIVE_W, OBJECTIVE_H));
        this.objective = objective;
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        Text.writeCentered(a, OBJECTIVE_W / 2, 1, this.objective.getPointsPerMatch() + "");

        switch (objective) {
            case SameCollectibleObjective sameCollectibleObjective -> {
                drawSameCollectible(a, sameCollectibleObjective);
            }
            case DifferentCollectibleObjective differentCollectibleObjective -> {
                drawDifferentCollectible(a, differentCollectibleObjective);
            }
            case PatternObjective patternObjective -> {
                drawPattern(a, patternObjective);
            }
        }
    }

    private void drawSameCollectible(DrawArea a, SameCollectibleObjective objective) {
        switch (objective.getRequiredCollectible()) {
            case Resource resource -> a.setRendition(Utils.getCardColorRendition(resource.getAssociatedColor()));
            default -> a.setRendition(GraphicalRendition.DEFAULT);
        }

        Line.rectangle(a, Position.ZERO, a.dimensions(), Line.Style.ROUNDED);

        String collectible = collectibleMapToString(Map.of(objective.getRequiredCollectible(), objective.getRequiredNumber()));

        Text.writeCentered(a, OBJECTIVE_W / 2, OBJECTIVE_H / 2, collectible);
    }

    private void drawDifferentCollectible(DrawArea a, DifferentCollectibleObjective objective) {
        Line.rectangle(a, Position.ZERO, a.dimensions(), Line.Style.ROUNDED);

        String collectible = collectibleMapToString(objective.getRequiredItems().stream().collect(Collectors.toMap(
                item -> item,
                item -> 1
        )));

        Text.writeCentered(a, OBJECTIVE_W / 2, OBJECTIVE_H / 2, collectible);
    }

    private String collectibleMapToString(Map<Collectible, Integer> collectibleMap) {
        return collectibleMap.entrySet().stream()
                .flatMap(collectibleIntegerEntry ->
                        Stream.generate(collectibleIntegerEntry::getKey)
                                .limit(collectibleIntegerEntry.getValue())
                )
                .map(Utils::getCollectibleEmoji)
                .collect(Collectors.joining());
    }

    private void drawPattern(DrawArea a, PatternObjective objective) {
        for (Map.Entry<PlayArea.Position, CardColor> entry : objective.getPattern().entrySet()) {
            PlayArea.Position pos = entry.getKey();
            CardColor color = entry.getValue();
            a.setRendition(Utils.getCardColorRendition(color));
            Line.rectangle(a,
                    new Position(
                            OBJECTIVE_W / 2 - OBJECTIVE_INNER_CARD_W / 2 + 2 * (pos.i() - pos.j()),
                            OBJECTIVE_H / 2 - OBJECTIVE_INNER_CARD_H / 2 - (pos.i() + pos.j())
                    ),
                    new Dimensions(OBJECTIVE_INNER_CARD_W, OBJECTIVE_INNER_CARD_H),
                    Line.Style.ROUNDED
            );
        }

        Line.rectangle(a, Position.ZERO, a.dimensions(), Line.Style.ROUNDED);
    }
}
