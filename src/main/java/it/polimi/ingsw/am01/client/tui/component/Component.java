package it.polimi.ingsw.am01.client.tui.component;

import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

/**
 * <ol>
 *     <li>The parent calls {@link #layout(Constraint)}.</li>
 *     <ol>
 *         <li>The component chooses some {@link Dimensions} within the specified {@link Constraint}</li>
 *         <lI>The parent can retrieve the chosen dimensions by calling {@link #dimensions()}</lI>
 *     </ol>
 *     <li>The parent places the component, by calling {@link #setPosition(Position)}</li>
 *     <li>The parent tells the component to draw itself its children, by calling {@link #draw(RenderingContext, DrawArea)}</li>
 * </ol>
 */
public abstract class Component {
    private Dimensions dimensions;
    private Position position;

    public abstract void layout(Constraint constraint);

    protected void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Dimensions dimensions() {
        if (dimensions == null) {
            throw new IllegalStateException("Component does not have dimensions yet. You must call chooseDimensions(Constraint) before calling getDimensions().");
        }

        return this.dimensions;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalStateException("Component does not have dimensions yet. You must call chooseDimensions(Constraint) before calling setPosition(Position).");
        }

        this.position = position;
    }

    public Position position() {
        if (position == null) {
            throw new IllegalStateException("Component does not have a positioned yet. You must call setPosition(Position) before calling getPosition().");
        }

        return this.position;
    }

    public abstract void draw(RenderingContext ctx, DrawArea a);
}
