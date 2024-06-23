package it.polimi.ingsw.am01.client.tui.rendering;

/**
 * Represents a constraint for the dimensions of a {@link it.polimi.ingsw.am01.client.tui.component.Component}.
 *
 * @param min the minimum dimensions
 * @param max the maximum dimensions
 */
public record Constraint(Dimensions min, Dimensions max) {
    /**
     * Creates a new constraint with the given minimum and maximum dimensions, specified by their width and height.
     *
     * @param minW the minimum width
     * @param maxW the maximum width
     * @param minH the minimum height
     * @param maxH the maximum height
     * @return the new constraint
     */
    public static Constraint of(int minW, int maxW, int minH, int maxH) {
        return new Constraint(new Dimensions(minW, minH), new Dimensions(maxW, maxH));
    }

    /**
     * Creates a new constraint with the given minimum and maximum dimensions.
     *
     * @param min the minimum dimensions
     * @param max the maximum dimensions
     * @return the new constraint
     */
    public static Constraint minMax(Dimensions min, Dimensions max) {
        return new Constraint(min, max);
    }

    /**
     * Creates a new constraint with the given maximum dimensions.
     * The minimum dimensions will be set to {@link Dimensions#ZERO}.
     *
     * @param max the maximum dimensions
     * @return the new constraint
     */
    public static Constraint max(Dimensions max) {
        return new Constraint(Dimensions.ZERO, max);
    }

    /**
     * Crates a new {@link Constraint} with the minimum dimensions {@link Dimensions#grow(int, int) grown} by the given amount.
     * <p>
     * The maximum dimensions are not changed.
     *
     * @param growW the amount to grow the minimum width
     * @param growH the amount to grow the minimum height
     * @return the new constraint
     */
    public Constraint growMin(int growW, int growH) {
        return new Constraint(min.grow(growW, growH), max);
    }

    /**
     * Crates a new {@link Constraint} with the maximum dimensions {@link Dimensions#shrink(int, int) shrunk} by the given amount.
     * <p>
     * The minimum dimensions are not changed.
     *
     * @param shrinkW the amount to shrink the maximum width
     * @param shrinkH the amount to shrink the maximum height
     * @return the new constraint
     */
    public Constraint shrinkMax(int shrinkW, int shrinkH) {
        return new Constraint(min, max.shrink(shrinkW, shrinkH));
    }
}
