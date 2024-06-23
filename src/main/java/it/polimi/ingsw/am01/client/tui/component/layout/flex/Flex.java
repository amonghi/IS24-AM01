package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;
import java.util.function.ToIntFunction;

public class Flex extends BaseLayout {
    private final Direction direction;
    private final List<FlexChild> children;

    public static Flex row(List<FlexChild> children) {
        return new Flex(Direction.ROW, children);
    }

    public static Flex column(List<FlexChild> children) {
        return new Flex(Direction.COLUMN, children);
    }

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
        Dimensions maxDimensions = constraint.max();

        for (int i = 0; i < this.children.size(); i++) {
            switch (this.children.get(i)) {
                case FlexChild.Fixed(Component child) -> {
                    child.layout(Constraint.max(maxDimensions));

                    maxDimensions = switch (this.direction) {
                        case ROW -> maxDimensions.shrink(child.dimensions().width(), 0);
                        case COLUMN -> maxDimensions.shrink(0, child.dimensions().height());
                    };
                }

                case FlexChild.Flexible(int growFactor, Component ignored) -> {
                    growSum += growFactor;
                }
            }
        }

        // place both fixed children and flexible children

        int flexSpace = switch (this.direction) {
            case ROW -> maxDimensions.width();
            case COLUMN -> maxDimensions.height();
        };
        int offs = 0;

        for (int i = 0; i < this.children.size(); i++) {
            Position position = switch (this.direction) {
                case ROW -> Position.of(offs, 0);
                case COLUMN -> Position.of(0, offs);
            };

            switch (this.children.get(i)) {
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
