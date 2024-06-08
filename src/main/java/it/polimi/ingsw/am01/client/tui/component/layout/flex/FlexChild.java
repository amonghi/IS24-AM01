package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;

public sealed interface FlexChild {
    Component child();

    record Fixed(Component child) implements FlexChild {
    }

    record Flexible(int growFactor, Component child) implements FlexChild {
    }
}
