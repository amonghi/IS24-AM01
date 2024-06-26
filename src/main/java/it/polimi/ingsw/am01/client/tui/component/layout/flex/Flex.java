package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * A layout that arranges its children in a row or column and allows for "flexible" children.
 * A flexible child is a child that can grow to fill the remaining space in the layout.
 * The children that are not flexible are called "fixed" children.
 * <p>
 * This layout does not take {@link Component}s as children directly, but rather takes {@link FlexChild}s.
 * This is because each child must be marked as either "fixed" or "flexible".
 * <p>
 * This layout will first calculate the dimensions of all fixed children
 * and then distribute the remaining space among the flexible children.
 * Each flexible child is assigned a "grow factor" proportional to the amount of space it should take up.
 *
 * @see FlexChild.Fixed
 * @see FlexChild.Flexible
 */
public class Flex extends BaseLayout {
    private final Direction direction;
    private final List<FlexChild> children;

    /**
     * Create a new Flex layout that arranges its children in a row.
     *
     * @param children The children of the layout.
     */
    public static Flex row(List<FlexChild> children) {
        return new Flex(Direction.ROW, children);
    }

    /**
     * Create a new Flex layout that arranges its children in a column.
     *
     * @param children The children of the layout.
     */
    public static Flex column(List<FlexChild> children) {
        return new Flex(Direction.COLUMN, children);
    }

    /**
     * Create a new Flex layout.
     *
     * @param direction The direction in which the children should be arranged.
     * @param children  The children of the layout.
     */
    public Flex(Direction direction, List<FlexChild> children) {
        this.direction = direction;
        this.children = children;
    }

    @Override
    protected List<Component> children() {
        return this.children.stream().map(FlexChild::child).toList();
    }

    @Override
    public void layout(Constraint constraint) {
        // calculate dimensions of fixed children
        int growSum = 0;
        Dimensions remainingSpace = constraint.max();

        for (FlexChild flexChild : this.children) {
            switch (flexChild) {
                case FlexChild.Fixed(Component child) -> {
                    child.layout(Constraint.max(remainingSpace));

                    remainingSpace = switch (this.direction) {
                        case ROW -> remainingSpace.shrink(child.dimensions().width(), 0);
                        case COLUMN -> remainingSpace.shrink(0, child.dimensions().height());
                    };
                }

                case FlexChild.Flexible(int growFactor, Component ignored) -> {
                    growSum += growFactor;
                }
            }
        }

        // place both fixed children and flexible children

        int flexSpace = switch (this.direction) {
            case ROW -> remainingSpace.width();
            case COLUMN -> remainingSpace.height();
        };
        int offs = 0;

        for (FlexChild flexChild : this.children) {
            Position position = switch (this.direction) {
                case ROW -> Position.of(offs, 0);
                case COLUMN -> Position.of(0, offs);
            };

            switch (flexChild) {
                // place fixed child
                case FlexChild.Fixed(Component child) -> {
                    child.setPosition(position);
                    offs += switch (this.direction) {
                        case ROW -> child.dimensions().width();
                        case COLUMN -> child.dimensions().height();
                    };
                }

                case FlexChild.Flexible(int growFactor, Component child) -> {
                    // calculate dimensions of a flexible child
                    int allocatedSpace = (int) ((double) flexSpace * ((double) growFactor / (double) growSum));
                    Dimensions max = switch (this.direction) {
                        case ROW -> Dimensions.of(allocatedSpace, constraint.max().height());
                        case COLUMN -> Dimensions.of(constraint.max().width(), allocatedSpace);
                    };
                    Constraint childConstraint = Constraint.max(max);
                    child.layout(childConstraint);

                    // place flexible child
                    child.setPosition(position);
                    offs += allocatedSpace;
                }
            }
        }

        // calculate own dimensions
        ToIntFunction<Dimensions> shortSideMapper = switch (this.direction) {
            case ROW -> Dimensions::height;
            case COLUMN -> Dimensions::width;
        };

        int shortSide = this.children.stream()
                .map(FlexChild::child)
                .map(Component::dimensions)
                .mapToInt(shortSideMapper)
                .max()
                .orElseThrow();

        Dimensions desiredDimensions = switch (this.direction) {
            case ROW -> Dimensions.of(constraint.max().width(), shortSide);
            case COLUMN -> Dimensions.of(shortSide, constraint.max().height());
        };

        Dimensions d = desiredDimensions.constrain(constraint);
        this.setDimensions(d);
    }

    public enum Direction {
        ROW,
        COLUMN
    }
}
