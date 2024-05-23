package it.polimi.ingsw.am01.tui.rendering;

public record Position(int x, int y) {
    public static final Position ZERO = new Position(0, 0);

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Position must have positive x and y");
        }
    }

    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
}
