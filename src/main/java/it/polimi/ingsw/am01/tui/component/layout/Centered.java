package it.polimi.ingsw.am01.tui.component.layout;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.*;

import java.util.List;

public class Centered extends LayoutComponent {
    private final Component child;
    private final boolean horizontally;
    private final boolean vertically;

    public static Centered horizontally(Component child) {
        return new Centered(child, true, false);
    }

    public static Centered vertically(Component child) {
        return new Centered(child, false, true);
    }

    public static Centered both(Component child) {
        return new Centered(child, true, true);
    }

    private Centered(Component child, boolean horizontally, boolean vertically) {
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
