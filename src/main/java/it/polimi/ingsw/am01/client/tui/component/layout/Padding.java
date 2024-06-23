package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

public class Padding extends SingleChildLayout {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    public Padding(int top, int right, int bottom, int left, Component child) {
        super(child);
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public static Padding around(int padding, Component child) {
        return new Padding(padding, padding, padding, padding, child);
    }

    public static Padding hv(int horizontal, int vertical, Component child) {
        return new Padding(vertical, horizontal, vertical, horizontal, child);
    }

    @Override
    public void layout(Constraint constraint) {
        this.child().layout(constraint.shrinkMax(left + right, top + bottom));
        this.child().setPosition(Position.of(this.left, this.top));

        Dimensions d = this.child().dimensions()
                .grow(left + right, top + bottom)
                .constrain(constraint);
        this.setDimensions(d);
    }
}
