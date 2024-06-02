package it.polimi.ingsw.am01.tui.component.layout;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

/**
 * A {@link LayoutComponent} is a {@link Component} that does not draw anything
 */
public abstract class LayoutComponent extends Component {

    public LayoutComponent(BuildContext ctx) {
        super(ctx);
    }

    @Override
    public abstract Sized layout(Constraint constraint);

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        // no-op
    }
}
