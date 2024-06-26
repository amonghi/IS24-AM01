package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

/**
 * Places the terminal cursor at the position where this component is rendered.
 * There is supposed to be only one cursor in a component tree.
 * <p>
 * The cursor has a fixed width of 0 and height of 1.
 */
public class Cursor extends Element {

    /**
     * Creates a new cursor element.
     */
    public Cursor() {
        super(Dimensions.of(0, 1));
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        ctx.global().setCursorPosition(ctx.local().getOffset());
    }
}
