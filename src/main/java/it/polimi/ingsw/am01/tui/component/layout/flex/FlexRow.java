package it.polimi.ingsw.am01.tui.component.layout.flex;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.layout.LayoutComponent;
import it.polimi.ingsw.am01.tui.rendering.*;

import java.util.Arrays;
import java.util.List;

public class FlexRow extends LayoutComponent {

    private final List<FlexChild> children;

    public FlexRow(List<FlexChild> children) {
        this.children = children;
    }

    @Override
    public Sized layout(Constraint constraint) {
        Sized[] sized = new Sized[children.size()];
        int growTotal = 0;

        Dimensions maxDimensions = constraint.max();

        for (int i = 0; i < this.children.size(); i++) {
            switch (this.children.get(i)) {
                case FlexChild.Fixed(Component child) -> {
                    Sized s = child.layout(Constraint.max(maxDimensions));
                    sized[i] = s;

                    maxDimensions = maxDimensions.shrink(s.dimensions().width(), 0);
                }

                case FlexChild.Flexible(int factor, Component ignored) -> {
                    growTotal += factor;
                }
            }
        }

        int remainingWidth = maxDimensions.width();
        SizedPositioned[] sizedPositioned = new SizedPositioned[children.size()];
        int x = 0;

        for (int i = 0; i < this.children.size(); i++) {
            switch (this.children.get(i)) {
                case FlexChild.Fixed ignored -> {
                    Sized s = sized[i];
                    sizedPositioned[i] = s.placeAt(Position.of(x, 0));
                    x += s.dimensions().width();
                }

                case FlexChild.Flexible(int factor, Component child) -> {
                    int maxWidth = (int) ((double) remainingWidth * ((double) factor / (double) growTotal));
                    Constraint childConstraint = Constraint.of(0, maxWidth, 0, constraint.max().height());

                    Sized s = child.layout(childConstraint);
                    sizedPositioned[i] = s.placeAt(Position.of(x, 0));
                    x += maxWidth;
                }
            }
        }

        int h = Arrays.stream(sizedPositioned)
                .mapToInt(s -> s.dimensions().height())
                .max()
                .orElseThrow();
        return new Sized(this, Dimensions.constrained(constraint, constraint.max().width(), h), List.of(sizedPositioned));
    }
}
