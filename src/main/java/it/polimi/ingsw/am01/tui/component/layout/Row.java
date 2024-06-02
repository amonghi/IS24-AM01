package it.polimi.ingsw.am01.tui.component.layout;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.rendering.*;

import java.util.ArrayList;
import java.util.List;

public class Row extends LayoutComponent {
    private final List<Component> children;

    public static ComponentBuilder of(ComponentBuilder... children) {
        return ctx -> new Row(ctx, ComponentBuilder.buildAll(ctx, children));
    }

    public Row(BuildContext ctx, List<Component> children) {
        super(ctx);
        this.children = children;
    }

    @Override
    public Sized layout(Constraint constraint) {
        int w = 0;
        int h = 0;

        List<SizedPositioned> childrenLayouts = new ArrayList<>();

        for (Component child : this.children) {
            Sized layout = child.layout(Constraint.max(
                    Dimensions.of(constraint.max().width() - w, constraint.max().height())
            ));

            Position placementPosition = Position.of(w, 0);
            childrenLayouts.add(layout.placeAt(placementPosition));

            w += layout.dimensions().width();
            h = Math.max(h, layout.dimensions().height());
        }

        return new Sized(this, Dimensions.constrained(constraint, w, h), childrenLayouts);
    }
}
