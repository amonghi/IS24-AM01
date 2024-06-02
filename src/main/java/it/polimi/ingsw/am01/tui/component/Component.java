package it.polimi.ingsw.am01.tui.component;

import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

public abstract class Component {
    private final BuildContext context;

    protected Component(BuildContext context) {
        this.context = context;
    }

    public void update() {
        context.getRenderer().render();
    }

    protected BuildContext getContext() {
        return context;
    }

    public abstract Sized layout(Constraint constraint);

    public abstract void drawSelf(RenderingContext ctx, DrawArea a);

    public void onScreen() {
    }

    public void offScreen() {
    }

}
