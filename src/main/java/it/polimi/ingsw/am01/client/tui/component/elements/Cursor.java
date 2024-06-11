package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

public class Cursor extends Element {

    public Cursor() {
        super(Dimensions.of(0, 1));
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        ctx.global().setCursorPosition(ctx.local().getOffset());
    }
}
