package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

public abstract class BaseLayout extends Component {

    protected abstract List<Component> children();

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        for (Component child : this.children()) {
            RenderingContext newCtx = new RenderingContext(
                    ctx.global(),
                    new RenderingContext.Local(ctx.local().getOffset().add(child.position()))
            );
            DrawArea da = a.getSubarea(child.position(), child.dimensions());

            child.draw(newCtx, da);
        }
    }
}
