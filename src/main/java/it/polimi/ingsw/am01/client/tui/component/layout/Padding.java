package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

/**
 * Adds padding around its child component.
 */
public class Padding extends SingleChildLayout {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    /**
     * Creates a new Padding with the given padding and child.
     *
     * @param top    padding on top of the child
     * @param right  padding to the right of the child
     * @param bottom padding on the bottom of the child
     * @param left   padding to the left of the child
     * @param child  the child
     */
    public Padding(int top, int right, int bottom, int left, Component child) {
        super(child);
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Creates a new Padding with the same padding on all sides and the given child.
     *
     * @param padding the padding on all sides
     * @param child   the child
     */
    public static Padding around(int padding, Component child) {
        return new Padding(padding, padding, padding, padding, child);
    }

    /**
     * Creates a new Padding with the given horizontal and vertical padding and the given child.
     *
     * @param horizontal the padding on the left and right
     * @param vertical   the padding on the top and bottom
     * @param child      the child
     */
    public static Padding hv(int horizontal, int vertical, Component child) {
        return new Padding(vertical, horizontal, vertical, horizontal, child);
    }

    @Override
    public void layout(Constraint constraint) {
        this.child().layout(constraint.shrinkMax(left + right, top + bottom));
        this.child().setPosition(Position.of(this.left, this.top));

        Dimensions d = this.child().dimensions()
                .grow(left + right, top + bottom)
                .constrain(constraint);
        this.setDimensions(d);
    }
}
