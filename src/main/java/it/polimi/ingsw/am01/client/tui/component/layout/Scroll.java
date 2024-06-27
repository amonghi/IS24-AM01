package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

/**
 * A component that allows for scrolling of another component.
 * <p>
 * The Scroll component will take up all the space available to it.
 * The scrollable (aka the child) component is allowed to take up as much space as it wants.
 * Scroll will then clip the scrollable component to fit within its own dimensions.
 * <p>
 * The scrollable component will be drawn at the offset specified in the constructor.
 * The offset is relative to the center of the Scroll component and the anchor of the scrollable component.
 * This means that if the offset is 0,0
 * then the anchor point of the scrollable component will be at the center of the Scroll component.
 *
 * @param <T> The type of the scrollable component.
 */
public class Scroll<T extends Component & Scroll.Scrollable> extends Component {
    private static final Constraint MAX_CONSTRAINT = Constraint.max(Dimensions.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

    private final T scrollable;
    private final int xOff;
    private final int yOff;

    /**
     * Create a new Scroll component.
     *
     * @param xOff       The x offset of the scrollable component.
     * @param yOff       The y offset of the scrollable component.
     * @param scrollable The scrollable component.
     */
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

    /**
     * An interface that allows a {@link Component} to be scrolled.
     */
    public interface Scrollable {
        /**
         * The anchor point is the point that remains stationary if the component changes its dimensions.
         * This method MUST be called after calling layout() on the component.
         * <p>
         * The anchor point is relative to the top-left corner of the component.
         *
         * @return the anchor point of the scrollable component
         */
        Position getAnchor();
    }
}
