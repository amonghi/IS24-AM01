package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

public class Column extends Layout {
    private final List<Component> children;

    public Column(List<Component> children) {
        super(children);
        this.children = children;
    }

    @Override
    public void layout(Constraint constraint) {
        int filledH = 0;

        for (Component child : this.children) {
            child.layout(constraint.shrinkMax(0, filledH));

            Position placementPosition = Position.of(0, filledH);
            child.setPosition(placementPosition);

            filledH += child.dimensions().height();
        }

        int w = this.children().stream()
                .mapToInt(value -> value.dimensions().width())
                .max()
                .orElse(0);

        Dimensions d = Dimensions.constrained(constraint, w, filledH);
        this.setDimensions(d);
    }
}
