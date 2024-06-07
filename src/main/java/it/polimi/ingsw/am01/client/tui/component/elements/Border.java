package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.*;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

import java.util.List;

public class Border implements Component {
    private final Line.Style style;
    private final Component child;

    public Border(Line.Style style, Component child) {
        this.style = style;
        this.child = child;
    }

    @Override
    public Sized layout(Constraint constraint) {
        SizedPositioned child = this.child
                .layout(constraint.shrinkMax(2, 2))
                .placeAt(Position.of(1, 1));

        return new Sized(
                this,
                child.dimensions().grow(2, 2),
                List.of(child)
        );
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        Line.rectangle(a, Position.ZERO, a.dimensions(), this.style);
    }
}
