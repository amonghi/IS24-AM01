package it.polimi.ingsw.am01.tui.rendering;

import it.polimi.ingsw.am01.tui.component.Component;

import java.util.List;

public record Sized(
        Component component,
        Dimensions dimensions,
        List<SizedPositioned> children
) {
    public SizedPositioned placeAt(Position position) {
        return new SizedPositioned(component, dimensions, position, children);
    }
}
