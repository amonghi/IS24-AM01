package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.client.tui.rendering.*;

import java.util.List;

public class Centered extends LayoutComponent {
    private final Component child;
    private final boolean horizontally;
    private final boolean vertically;

    public static ComponentBuilder horizontally(ComponentBuilder child) {
        return ctx -> new Centered(ctx, child.build(ctx), true, false);
    }

    public static ComponentBuilder vertically(ComponentBuilder child) {
        return ctx -> new Centered(ctx, child.build(ctx), false, true);
    }

    public static ComponentBuilder both(ComponentBuilder child) {
        return ctx -> new Centered(ctx, child.build(ctx), true, true);
    }

    private Centered(BuildContext ctx, Component child, boolean horizontally, boolean vertically) {
        super(ctx);
        this.child = child;
        this.horizontally = horizontally;
        this.vertically = vertically;
    }

    @Override
    public Sized layout(Constraint constraint) {
        Sized sizedChild = this.child.layout(constraint);

        Dimensions d = Dimensions.of(
                this.horizontally ? constraint.max().width() : sizedChild.dimensions().width(),
                this.vertically ? constraint.max().height() : sizedChild.dimensions().height()
        );

        SizedPositioned sizedPositionedChild = sizedChild.placeAt(Position.of(
                d.width() / 2 - sizedChild.dimensions().width() / 2,
                d.height() / 2 - sizedChild.dimensions().height() / 2
        ));

        return new Sized(this, d, List.of(sizedPositionedChild));
    }
}
