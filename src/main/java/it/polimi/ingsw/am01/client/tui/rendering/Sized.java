package it.polimi.ingsw.am01.client.tui.rendering;

import it.polimi.ingsw.am01.client.tui.component.Component;

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
