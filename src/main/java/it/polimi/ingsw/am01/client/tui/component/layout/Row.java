package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

public class Row extends Layout {

    public Row(List<Component> children) {
        super(children);
    }

    @Override
    public void layout(Constraint constraint) {
        int w = 0;
        int h = 0;

        for (Component child : this.children()) {
            child.layout(Constraint.max(
                    Dimensions.of(constraint.max().width() - w, constraint.max().height())
            ));

            Position placementPosition = Position.of(w, 0);
            child.setPosition(placementPosition);

            w += child.dimensions().width();
            h = Math.max(h, child.dimensions().height());
        }

        Dimensions d = Dimensions.constrained(constraint, w, h);
        this.setDimensions(d);
    }
}
