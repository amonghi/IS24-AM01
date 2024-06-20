package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;

import java.util.List;

/**
 * A layout is a component that contains other components (children)
 * and arranges them in a specific way.
 */
public abstract class Layout extends BaseLayout {
    private final List<Component> children;

    /**
     * Creates a new layout with the given children.
     *
     * @param children the children
     */
    public Layout(List<Component> children) {
        this.children = children;
    }

    /**
     * Gets the children of this layout.
     *
     * @return the children
     */
    protected List<Component> children() {
        return children;
    }
}
