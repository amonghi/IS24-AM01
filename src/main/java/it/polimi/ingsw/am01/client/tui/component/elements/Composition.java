package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.layout.BaseLayout;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Position;

import java.util.List;

public abstract class Composition extends BaseLayout {
    private final Component root;

    public Composition() {
        this.root = this.compose();
    }

    protected abstract Component compose();

    @Override
    protected List<Component> children() {
        return List.of(this.root);
    }

    @Override
    public void layout(Constraint constraint) {
        this.root.layout(constraint);
        this.root.setPosition(Position.ZERO);
        this.setDimensions(this.root.dimensions());
    }
}
