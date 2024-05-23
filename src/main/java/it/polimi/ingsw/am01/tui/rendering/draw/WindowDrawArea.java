package it.polimi.ingsw.am01.tui.rendering.draw;

import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;

import java.util.Objects;

public class WindowDrawArea implements DrawArea {
    private final DrawArea baseDrawArea;
    private final Position position;
    private final Dimensions dimensions;

    public WindowDrawArea(DrawArea baseDrawArea, Position position, Dimensions dimensions) {
        Objects.requireNonNull(position);
        Objects.requireNonNull(baseDrawArea);
        Objects.requireNonNull(dimensions);

        if (position.x() + dimensions.width() > baseDrawArea.dimensions().width()) {
            throw new IllegalArgumentException("drawing window out of bounds");
        }

        if (position.y() + dimensions.height() > baseDrawArea.dimensions().height()) {
            throw new IllegalArgumentException("drawing window out of bounds");
        }

        this.baseDrawArea = baseDrawArea;
        this.position = position;
        this.dimensions = dimensions;
    }

    @Override
    public Dimensions dimensions() {
        return this.dimensions;
    }

    @Override
    public void draw(int x, int y, char c) {
        this.baseDrawArea.draw(this.position.x() + x, this.position.y() + y, c);
    }

    @Override
    public DrawArea window(Position p, Dimensions d) {
        return new WindowDrawArea(this.baseDrawArea, this.position.add(p), d);
    }
}
