package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.Sized;
import it.polimi.ingsw.am01.client.tui.rendering.SizedPositioned;

import java.util.List;

public class Padding extends LayoutComponent {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;
    private final Component child;

    public Padding(int top, int right, int bottom, int left, Component child) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.child = child;
    }

    public static Padding around(int padding, Component child) {
        return new Padding(padding, padding, padding, padding, child);
    }

    public static Padding hv(int horizontal, int vertical, Component child) {
        return new Padding(vertical, horizontal, vertical, horizontal, child);
    }

    @Override
    public Sized layout(Constraint constraint) {
        SizedPositioned child = this.child
                .layout(constraint.shrinkMax(left + right, top + bottom))
                .placeAt(Position.of(this.left, this.top));

        return new Sized(
                this,
                child.dimensions().grow(left + right, top + bottom),
                List.of(child)
        );
    }
}
