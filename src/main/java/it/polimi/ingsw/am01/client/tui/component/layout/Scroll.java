package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

public class Scroll<T extends Component & Scroll.Scrollable> extends Component {
    private static final Constraint MAX_CONSTRAINT = Constraint.max(Dimensions.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

    private final T scrollable;
    private final int xOff;
    private final int yOff;

    public Scroll(int xOff, int yOff, T scrollable) {
        this.scrollable = scrollable;
        this.xOff = xOff;
        this.yOff = yOff;
    }

    @Override
    public void layout(Constraint constraint) {
        this.scrollable.layout(MAX_CONSTRAINT);
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

        int xCenter = a.dimensions().width() / 2;
        int yCenter = a.dimensions().height() / 2;

        Position anchor = this.scrollable.getAnchor();
        int xOffInternal = this.xOff - anchor.x() + xCenter;
        int yOffInternal = this.yOff - anchor.y() + yCenter;

        Position externalOff = ctx.local().getOffset();
        int xOff = externalOff.x() + xOffInternal;
        int yOff = externalOff.y() + yOffInternal;

        DrawArea relativeArea = a.getRelativeArea(xOff, yOff, this.scrollable.dimensions());
        scrollable.draw(newCtx, relativeArea);
    }

    public interface Scrollable {
        /**
         * The anchor point is the point that remains stationary if the component changes its dimensions.
         * This interface is meant to be implemented by {@link Component}s that can be scrolled.
         * This method MUST be called after calling layout() on the component.
         *
         * @return the anchor point of the scrollable component
         */
        Position getAnchor();
    }
}
