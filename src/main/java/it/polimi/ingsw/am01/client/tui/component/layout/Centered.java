package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

public class Centered extends SingleChildLayout {
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
        super(child);
        this.horizontally = horizontally;
        this.vertically = vertically;
    }

    @Override
    public void layout(Constraint constraint) {
        this.child().layout(constraint);

        Dimensions d = Dimensions.of(
                this.horizontally ? constraint.max().width() : this.child().dimensions().width(),
                this.vertically ? constraint.max().height() : this.child().dimensions().height()
        );
        setDimensions(d);

        this.child().setPosition(Position.of(
                d.width() / 2 - this.child().dimensions().width() / 2,
                d.height() / 2 - this.child().dimensions().height() / 2
        ));
    }
}
