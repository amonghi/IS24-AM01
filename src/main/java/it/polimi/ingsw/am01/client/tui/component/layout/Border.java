package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;

public class Border extends SingleChildLayout {
    private final Line.Style style;

    public Border(Line.Style style, Component child) {
        super(child);
        this.style = style;
    }

    @Override
    public void layout(Constraint constraint) {
        this.child().layout(constraint.shrinkMax(2, 2));
        this.child().setPosition(Position.of(1, 1));

        Dimensions d = this.child().dimensions().grow(2, 2);
        this.setDimensions(d);
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        super.draw(ctx, a);
        Line.rectangle(a, Position.ZERO, a.dimensions(), this.style);
    }
}
