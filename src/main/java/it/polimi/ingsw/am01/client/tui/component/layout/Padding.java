package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
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

    public static ComponentBuilder around(int padding, ComponentBuilder child) {
        return ctx -> new Padding(ctx, padding, padding, padding, padding, child.build(ctx));
    }

    public static ComponentBuilder hv(int horizontal, int vertical, ComponentBuilder child) {
        return ctx -> new Padding(ctx, vertical, horizontal, vertical, horizontal, child.build(ctx));
    }

    public Padding(BuildContext ctx, int top, int right, int bottom, int left, Component child) {
        super(ctx);
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
                .placeAt(Position.of(this.left, this.top));

        return new Sized(
                this,
                child.dimensions().grow(left + right, top + bottom),
                List.of(child)
        );
    }
}
