package it.polimi.ingsw.am01.tui.component.layout;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Position;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.SizedPositioned;

import java.util.List;

public class SpaceAround extends LayoutComponent {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;
    private final Component child;

    public SpaceAround(int top, int right, int bottom, int left, Component child) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.child = child;
    }

    @Override
    public Sized layout(Constraint constraint) {
        SizedPositioned child = this.child
                .layout(constraint.shrinkMax(left + right, top + bottom))
                .placeAt(Position.of(this.top, this.left));

        return new Sized(
                this,
                child.dimensions().grow(left + right, top + bottom),
                List.of(child)
        );
    }
}
