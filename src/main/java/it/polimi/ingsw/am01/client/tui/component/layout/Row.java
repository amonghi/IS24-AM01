package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

/**
 * A layout that arranges its children in a row.
 * <p>
 * The children are placed next to each other, from left to right.
 * The column will have the height of the tallest child and the width of the sum of the widths of all children.
 * <p>
 * The children are laid out sequentially in the order that they were given to the constructor.
 * This means that the first child will be able to take up as much space as it wants,
 * but the second child will only be able to take up the remaining space, and so on.
 */
public class Row extends Layout {

    /**
     * Creates a new row with the given children.
     *
     * @param children the children. They will be laid out in the order that they are given in the list. From left to right.
     */
    public Row(List<Component> children) {
        super(children);
    }

    @Override
    public void layout(Constraint constraint) {
        int w = 0;
        int h = 0;

        for (Component child : this.children()) {
            child.layout(Constraint.max(
                    Dimensions.of(constraint.max().width() - w, constraint.max().height())
            ));

            Position placementPosition = Position.of(w, 0);
            child.setPosition(placementPosition);

            w += child.dimensions().width();
            h = Math.max(h, child.dimensions().height());
        }

        Dimensions d = Dimensions.constrained(constraint, w, h);
        this.setDimensions(d);
    }
}
