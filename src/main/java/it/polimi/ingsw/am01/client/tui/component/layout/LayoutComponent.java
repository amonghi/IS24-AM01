package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.Sized;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

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
