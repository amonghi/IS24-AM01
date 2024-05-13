package it.polimi.ingsw.am01.tui.rendering;

// FIXME: disallow negative values
public record Position(int x, int y) {
    public static final Position ZERO = new Position(0, 0);

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }
}
