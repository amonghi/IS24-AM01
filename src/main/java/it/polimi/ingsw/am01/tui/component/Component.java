package it.polimi.ingsw.am01.tui.component;

import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

public interface Component {

    Sized layout(Constraint constraint);

    void drawSelf(RenderingContext ctx, DrawArea a);
}
