package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.layout.SpaceAround;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Sized;

public class Container implements Composition {
    private final int margin;
    private final BorderStyle borderStyle;
    private final int padding;
    private final Component child;

    public Container(int margin, BorderStyle borderStyle, int padding, Component child) {
        this.margin = margin;
        this.borderStyle = borderStyle;
        this.padding = padding;
        this.child = child;
    }

    @Override
    public Sized layout(Constraint constraint) {
        // force the child to take up all the space
        Constraint c = Constraint.minMax(constraint.max(), constraint.max());
        return this.compose().layout(c);
    }

    @Override
    public Component compose() {
        return new SpaceAround(margin, margin, margin, margin,
                new Border(borderStyle,
                        new SpaceAround(padding, padding, padding, padding,
                                child
                        )
                )
        );
    }
}
