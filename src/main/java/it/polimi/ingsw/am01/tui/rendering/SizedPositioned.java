package it.polimi.ingsw.am01.tui.rendering;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public record SizedPositioned(
        Component component,
        Dimensions dimensions,
        Position position,
        List<SizedPositioned> children
) {
    public void draw(DrawArea parentArea) {
        DrawArea a = parentArea.window(position, dimensions);
        this.component.drawSelf(a);

        for (SizedPositioned child : children) {
            child.draw(a);
        }
    }
}
