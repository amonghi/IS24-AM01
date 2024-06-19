package it.polimi.ingsw.am01.client.tui.rendering;

/**
 * Represents a position.
 * <p>
 * The position is always positive, zero-based, left-to-right, top-to-bottom.
 * The origin of this coordinate system depends on the context in which the {@link Position} is used: for example,
 * in a {@link it.polimi.ingsw.am01.client.tui.component.Component} the origin is the top-left corner of the component.
 *
 * @param x the x coordinate, always positive, zero-based, left-to-right
 * @param y the y coordinate, always positive, zero-based, top-to-bottom
 */
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

    /**
     * Adds two positions together.
     * <p>
     * The sum is performed component-wise.
     *
     * @param position the position to add
     * @return a new position that is the sum of this position and the given position
     */
    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

    /**
     * Adds two positions together. The second position is specified by its x and y coordinates.
     *
     * @param x the x coordinate of the position to add
     * @param y the y coordinate of the position to add
     * @return a new position that is the sum of this position and the given position
     */
    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
}
