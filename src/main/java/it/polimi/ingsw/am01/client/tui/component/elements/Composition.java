package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

public abstract class Composition extends BaseLayout {
    private Component root;

    protected abstract Component compose();

    private Component getRoot() {
        if (root == null) {
            root = compose();
        }

        return root;
    }

    @Override
    protected List<Component> children() {
        return List.of(this.getRoot());
    }

    @Override
    public void layout(Constraint constraint) {
        this.getRoot().layout(constraint);
        this.getRoot().setPosition(Position.ZERO);
        this.setDimensions(this.getRoot().dimensions());
    }
}
