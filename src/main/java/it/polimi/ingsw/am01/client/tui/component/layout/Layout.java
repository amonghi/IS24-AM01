package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;

import java.util.List;

public abstract class Layout extends BaseLayout {
    private final List<Component> children;

    public Layout(List<Component> children) {
        this.children = children;
    }

    protected List<Component> children() {
        return children;
    }
}
