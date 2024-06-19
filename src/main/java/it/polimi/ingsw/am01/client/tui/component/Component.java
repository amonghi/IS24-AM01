package it.polimi.ingsw.am01.client.tui.component;

import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

/**
 * A component is a part of the TUI (Terminal User Interface).
 * <p>
 * Lifecycle of a component:
 * <ol>
 *     <li><b>Creation</b>: the component is constructed.</li>
 *     <li><b>Layout</b></li>
 *     <ol>
 *         <li>The parent calls {@link #layout(Constraint)}:</li>
 *         <ol>
 *             <li>The component performs the <b>Layout</b> step to all its children
 *             (this implies that the operation is recursive on the whole tree of components)</li>
 *             <li>The component chooses some dimensions for itself (within the given {@link Constraint})</li>
 *         </ol>
 *         <li>The parent {@link #setPosition(Position) sets the position} of this component</li>
 *    </ol>
 *     <li><b>Drawing</b>: the parent calls {@link #draw(RenderingContext, DrawArea)}.
 *     It is the components responsibility to tell its children where and how to draw themselves.</li>
 * </ol>
 */
public abstract class Component {
    private Dimensions dimensions;
    private Position position;

    /**
     * Chooses the dimensions of the component given a constraint.
     *
     * @param constraint the constraint to respect
     */
    public abstract void layout(Constraint constraint);

    /**
     * Sets the dimensions of the component.
     *
     * @param dimensions the dimensions to set
     */
    protected void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Gets the dimensions of the component.
     *
     * @return the dimensions of the component
     * @throws IllegalStateException if the component does not have dimensions yet, because {@link #layout(Constraint)} has not been called yet
     */
    public Dimensions dimensions() {
        if (dimensions == null) {
            throw new IllegalStateException("Component does not have dimensions yet. You must call layout(Constraint) before calling getDimensions().");
        }

        return this.dimensions;
    }

    /**
     * Sets the position of the component.
     *
     * @param position the position to set
     * @throws IllegalStateException if the component does not have dimensions yet, because {@link #layout(Constraint)} has not been called yet
     */
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalStateException("Component does not have dimensions yet. You must call layout(Constraint) before calling setPosition(Position).");
        }

        this.position = position;
    }

    /**
     * Gets the position of the component.
     *
     * @return the position of the component
     * @throws IllegalStateException if the component does not have a position yet, because {@link #setPosition(Position)} has not been called yet
     */
    public Position position() {
        if (position == null) {
            throw new IllegalStateException("Component does not have a positioned yet. You must call setPosition(Position) before calling getPosition().");
        }

        return this.position;
    }

    /**
     * Draws the component on the {@link DrawArea}.
     *
     * @param ctx the rendering context
     * @param a   the draw area
     */
    public abstract void draw(RenderingContext ctx, DrawArea a);
}
