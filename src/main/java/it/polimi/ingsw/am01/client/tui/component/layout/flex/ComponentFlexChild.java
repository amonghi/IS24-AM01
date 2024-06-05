package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;

public sealed interface ComponentFlexChild {
    record Fixed(Component child) implements ComponentFlexChild {
    }

    record Flexible(int growFactor, Component child) implements ComponentFlexChild {
    }
}
