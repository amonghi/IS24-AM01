package it.polimi.ingsw.am01.client.tui.component;

import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.Sized;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

public interface Component {

    Sized layout(Constraint constraint);

    void drawSelf(RenderingContext ctx, DrawArea a);
}
