package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.LayoutComponent;
import it.polimi.ingsw.am01.client.tui.rendering.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class Flex extends LayoutComponent {
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
    public Sized layout(Constraint constraint) {
        // calculate dimensions of fixed children
        Sized[] sized = new Sized[children.size()];
        int growSum = 0;

        Dimensions maxDimensions = constraint.max();

        for (int i = 0; i < this.children.size(); i++) {
            switch (this.children.get(i)) {
                case FlexChild.Fixed(Component child) -> {
                    Sized s = child.layout(Constraint.max(maxDimensions));
                    sized[i] = s;

                    maxDimensions = switch (this.direction) {
                        case ROW -> maxDimensions.shrink(s.dimensions().width(), 0);
                        case COLUMN -> maxDimensions.shrink(0, s.dimensions().height());
                    };
                }

                case FlexChild.Flexible(int growFactor, Component ignored) -> {
                    growSum += growFactor;
                }
            }
        }

        // place both fixed children and flexible children

        SizedPositioned[] sizedPositioned = new SizedPositioned[children.size()];
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
                case FlexChild.Fixed ignored -> {
                    Sized s = sized[i];
                    sizedPositioned[i] = s.placeAt(position);
                    offs += s.dimensions().width();
                }

                case FlexChild.Flexible(int growFactor, Component child) -> {
                    // calculate dimensions of a flexible child
                    int allocatedSpace = (int) ((double) flexSpace * ((double) growFactor / (double) growSum));
                    Dimensions max = switch (this.direction) {
                        case ROW -> Dimensions.of(allocatedSpace, constraint.max().height());
                        case COLUMN -> Dimensions.of(constraint.max().width(), allocatedSpace);
                    };
                    Constraint childConstraint = Constraint.max(max);
                    Sized s = child.layout(childConstraint);

                    // place flexible child
                    sizedPositioned[i] = s.placeAt(position);
                    offs += allocatedSpace;
                }
            }
        }

        // calculate own dimensions
        ToIntFunction<Dimensions> shortSideMapper = switch (this.direction) {
            case ROW -> Dimensions::height;
            case COLUMN -> Dimensions::width;
        };

        int shortSide = Arrays.stream(sizedPositioned)
                .map(SizedPositioned::dimensions)
                .mapToInt(shortSideMapper)
                .max()
                .orElseThrow();

        Dimensions desiredDimensions = switch (this.direction) {
            case ROW -> Dimensions.of(constraint.max().width(), shortSide);
            case COLUMN -> Dimensions.of(shortSide, constraint.max().height());
        };

        return new Sized(this, desiredDimensions.constrain(constraint), List.of(sizedPositioned));
    }

    public enum Direction {
        ROW,
        COLUMN
    }
}
