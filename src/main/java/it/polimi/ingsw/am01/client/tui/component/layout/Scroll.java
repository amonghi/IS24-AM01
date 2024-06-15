package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

public class Scroll extends SingleChildLayout {
    private static final Constraint MAX_CONSTRAINT = Constraint.max(Dimensions.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

    private final int xOff;
    private final int yOff;

    public Scroll(int xOff, int yOff, Component child) {
        super(child);
        this.xOff = xOff;
        this.yOff = yOff;
    }

    @Override
    public void layout(Constraint constraint) {
        this.child().layout(MAX_CONSTRAINT);
        this.setDimensions(constraint.max());
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        RenderingContext newCtx = new RenderingContext(
                ctx.global(),

                // this tells the component it is rendering at 0,0
                // FIXME: cannot report real position because it could be negative or off-screen
                new RenderingContext.Local(Position.ZERO)
        );

        Position localOffset = ctx.local().getOffset();
        int xOff = localOffset.x() + this.xOff;
        int yOff = localOffset.y() + this.yOff;

        DrawArea relativeArea = a.getRelativeArea(xOff, yOff, child().dimensions());
        child().draw(newCtx, relativeArea);
    }

}
