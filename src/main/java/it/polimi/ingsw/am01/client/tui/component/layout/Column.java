package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

/**
 * A layout that arranges its children in a column.
 * <p>
 * The children are placed below each other, from top to bottom.
 * The column will have the width of the widest child and the height of the sum of the heights of all children.
 * <p>
 * The children are laid out sequentially in the order that they were given to the constructor.
 * This means that the first child will be able to take up as much space as it wants,
 * but the second child will only be able to take up the remaining space, and so on.
 */
public class Column extends Layout {
    private final List<Component> children;

    /**
     * Creates a new column with the given children.
     *
     * @param children the children. They will be laid out in the order that they are given in the list. From top to bottom.
     */
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
