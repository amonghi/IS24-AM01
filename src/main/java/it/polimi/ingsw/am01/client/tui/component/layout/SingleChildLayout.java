package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;

import java.util.List;

public abstract class SingleChildLayout extends Layout {
    public SingleChildLayout(Component child) {
        super(List.of(child));
    }

    protected Component child() {
        return this.children().getFirst();
    }
}
