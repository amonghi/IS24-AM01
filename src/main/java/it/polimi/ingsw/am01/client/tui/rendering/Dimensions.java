package it.polimi.ingsw.am01.client.tui.rendering;

/**
 * Represents the dimensions of something that ha a rectangular shape
 *
 * @param width  the width
 * @param height the height
 */
public record Dimensions(int width, int height) {
    /**
     * The zero dimensions. A rectangle with zero width and zero height.
     */
    public static final Dimensions ZERO = new Dimensions(0, 0);

    /**
     * Creates a new Dimensions object with the given width and height
     *
     * @param width  the width. Must be positive
     * @param height the height. Must be positive
     * @throws IllegalArgumentException if width or height are negative
     */
    public static Dimensions of(int width, int height) {
        return new Dimensions(width, height);
    }

    /**
     * Creates a new Dimensions object with the given width and height, but clamped to the given constraint
     *
     * @param constraint      the constraint to respect
     * @param preferredWidth  the width
     * @param preferredHeight the height
     * @return the new dimensions, clamped to the constraint if necessary
     */
    public static Dimensions constrained(Constraint constraint, int preferredWidth, int preferredHeight) {
        int w = preferredWidth;
        w = Math.max(w, constraint.min().width); // ensure w is not smaller than min.width
        w = Math.min(w, constraint.max().width); // ensure w is not bigger that max.width

        int h = preferredHeight;
        h = Math.max(h, constraint.min().height);
        h = Math.min(h, constraint.max().height);

        return new Dimensions(w, h);
    }

    /**
     * Creates a new Dimensions object with the given width and height
     *
     * @param width  the width. Must be positive
     * @param height the height. Must be positive
     * @throws IllegalArgumentException if width or height are negative
     */
    public Dimensions {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions must have positive width and height");
        }
    }

    /**
     * Apply a constraint to these dimensions.
     *
     * @param constraint the constraint to apply
     * @return new {@link Dimensions} that fit the given constraint
     * @see #constrained(Constraint, int, int)
     */
    public Dimensions constrain(Constraint constraint) {
        return Dimensions.constrained(constraint, width, height);
    }

    /**
     * Shrink these dimensions by the given amount.
     * <p>
     * The result will never be negative,
     * if the amount to shrink is greater than the dimensions, the result will be zero.
     *
     * @param shrinkW the amount to shrink the width
     * @param shrinkH the amount to shrink the height
     * @return new {@link Dimensions} that are smaller than these by the given amount
     */
    public Dimensions shrink(int shrinkW, int shrinkH) {
        return new Dimensions(Math.max(0, width - shrinkW), Math.max(0, height - shrinkH));
    }

    /**
     * Grow these dimensions by the given amount.
     * <p>
     * The result will be the sum of the current dimensions and the given amount.
     *
     * @param growW the amount to grow the width
     * @param growH the amount to grow the height
     * @return new {@link Dimensions} that are bigger than these by the given amount
     */
    public Dimensions grow(int growW, int growH) {
        return new Dimensions(width + growW, height + growH);
    }
}
