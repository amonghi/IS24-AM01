package it.polimi.ingsw.am01.tui.component.layout.flex;

import it.polimi.ingsw.am01.tui.component.Component;

public sealed interface FlexChild {
    record Fixed(Component child) implements FlexChild {
    }

    record Flexible(int factor, Component child) implements FlexChild {
    }
}
