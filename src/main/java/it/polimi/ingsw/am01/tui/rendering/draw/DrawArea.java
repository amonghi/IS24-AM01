package it.polimi.ingsw.am01.tui.rendering.draw;

import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;

public interface DrawArea {
    Dimensions dimensions();

    void draw(int x, int y, char c);

    DrawArea window(Position p, Dimensions d);
}
