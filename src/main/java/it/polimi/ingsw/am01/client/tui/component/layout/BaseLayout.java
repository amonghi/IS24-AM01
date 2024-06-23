package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

/**
 * Base class for layouts.
 */
public abstract class BaseLayout extends Component {

    /**
     * Gets the children of this layout.
     *
     * @return the children
     */
    protected abstract List<Component> children();

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        for (Component child : this.children()) {
            Position offs = ctx.local().getOffset().add(child.position());
            RenderingContext newCtx = new RenderingContext(
                    ctx.global(),
                    new RenderingContext.Local(offs)
            );
            DrawArea da = a.getRelativeArea(offs.x(), offs.y(), child.dimensions());

            child.draw(newCtx, da);
        }
    }
}
