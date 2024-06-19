package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;

import java.util.List;

/**
 * A layout that contains a single child.
 */
public abstract class SingleChildLayout extends Layout {
    /**
     * Creates a new layout with the given child.
     *
     * @param child the child
     */
    public SingleChildLayout(Component child) {
        super(List.of(child));
    }

    /**
     * Gets the child of this layout.
     *
     * @return the child
     */
    protected Component child() {
        return this.children().getFirst();
    }
}
