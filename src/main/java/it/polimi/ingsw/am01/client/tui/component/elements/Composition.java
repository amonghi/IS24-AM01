package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

/**
 * A composition is a tree of components that is treated as a single component.
 */
public abstract class Composition extends BaseLayout {
    private Component root;

    /**
     * Creates the subtree of components that make up this composition.
     */
    protected abstract Component compose();

    private Component getRoot() {
        if (root == null) {
            root = compose();
        }

        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Component> children() {
        return List.of(this.getRoot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layout(Constraint constraint) {
        this.getRoot().layout(constraint);
        this.getRoot().setPosition(Position.ZERO);
        this.setDimensions(this.getRoot().dimensions());
    }
}
