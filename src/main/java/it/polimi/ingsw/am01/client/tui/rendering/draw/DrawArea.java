package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;

import java.util.Objects;
import java.util.Optional;

/**
 * A drawing area on a DrawBuffer.
 * <p>
 * This class servers two purposes:
 * <ul>
 *      <li>it restricts the drawing area to a specific rectangle, to prevent components from drawing outside their bounds</li>
 *      <li>it simplifies the drawing process by providing utility methods to set the graphical rendition</li>
 * </ul>
 * <p>
 * A single DrawArea is actually three rectangles:
 * <ul>
 *     <li>the DrawBuffer area, which is the entire area of the DrawBuffer</li>
 *     <li>the crop area, which is the area of the DrawBuffer that is actually visible (this area is always fully contained in the DrawBuffer area)</li>
 *     <li>the drawing area, which is the area of the DrawBuffer that the DrawArea is allowed to draw on. This area is not necessary fully inscribed in the DrawBuffer area.</li>
 * </ul>
 * Only the drawing area is exposed to the user.
 * All methods that deal with {@link Dimensions} or {@link Position} do so in the drawing area space.
 * <p>
 * This three-area arrangement allows for drawing a component partially or fully outside the DrawBuffer area
 * (useful for {@link it.polimi.ingsw.am01.client.tui.component.layout.Scroll}).
 */
public class DrawArea {
    private final DrawBuffer drawBuffer;
    private final Rectangle cropArea;
    private final Rectangle area;
    private GraphicalRendition rendition;

    /**
     * Creates a new DrawArea that covers the entire DrawBuffer.
     *
     * @param drawBuffer the DrawBuffer to draw on
     */
    public DrawArea(DrawBuffer drawBuffer) {
        Objects.requireNonNull(drawBuffer);

        Rectangle bufferArea = new Rectangle(new Point(0, 0), drawBuffer.dimensions());

        this.drawBuffer = drawBuffer;
        this.cropArea = bufferArea;
        this.area = bufferArea;
        this.rendition = GraphicalRendition.DEFAULT;
    }

    /**
     * Creates a new DrawArea that is cropped to the given area.
     *
     * @param drawBuffer the DrawBuffer to draw on
     * @param cropArea   the area to crop the DrawArea to
     * @param area       the area to draw on
     */
    protected DrawArea(DrawBuffer drawBuffer, Rectangle cropArea, Rectangle area) {
        Rectangle bufferArea = new Rectangle(new Point(0, 0), drawBuffer.dimensions());
        if (!bufferArea.contains(cropArea)) {
            throw new IllegalArgumentException("crop area not inside drawBuffer area");
        }

        this.drawBuffer = drawBuffer;
        this.cropArea = cropArea;
        this.area = area;
        this.rendition = GraphicalRendition.DEFAULT;
    }

    /**
     * Gets a subarea of this DrawArea.
     * <p>
     * The subarea is specified in the current drawing area space.
     * The subarea must be fully contained in the current drawing area.
     *
     * @param offset     the offset of the subarea, relative to the top-left corner of the drawing area
     * @param dimensions the dimensions of the subarea
     * @return a new DrawArea that covers the subarea
     * @throws IllegalArgumentException if the subarea is not fully contained in the current drawing area
     */
    public DrawArea getSubarea(Position offset, Dimensions dimensions) {
        Rectangle subArea = new Rectangle(this.area.tl().add(Point.from(offset)), dimensions);

        Rectangle cropArea = this.cropArea.intersect(subArea).orElse(Rectangle.ZERO);

        return new DrawArea(this.drawBuffer, cropArea, subArea);
    }

    /**
     * Gets a new DrawArea with a position relative to this one.
     * <p>
     * The new area is specified in the current drawing area space.
     * The new area does not need to be fully contained in the current drawing area,
     * it could even be outside the DrawBuffer area.
     *
     * @param xOffset    the x offset of the new area. Can be positive or negative
     * @param yOffset    the y offset of the new area. Can be positive or negative
     * @param dimensions the dimensions of the new area
     * @return a new DrawArea
     */
    public DrawArea getRelativeArea(int xOffset, int yOffset, Dimensions dimensions) {
        Rectangle newArea = new Rectangle(new Point(xOffset, yOffset), dimensions);
        Rectangle cropArea = this.cropArea.intersect(newArea).orElse(Rectangle.ZERO);

        return new DrawArea(this.drawBuffer, cropArea, newArea);
    }

    /**
     * @return the dimensions of the drawing area
     */
    public Dimensions dimensions() {
        return this.area.dimension();
    }

    /**
     * Draws a character at the given position.
     * <p>
     * The position is specified in the current drawing area space.
     * The character is drawn with the current graphical rendition.
     *
     * @param position the position to draw at
     * @param c        the character to draw
     */
    public void draw(Position position, char c) {
        // 2. transform from coordinates in drawArea space to drawBuffer space
        Point drawPoint = new Point(this.area.tl().x() + position.x(), this.area.tl().y() + position.y());

        // 1. check if drawing inside draw area
        if (!this.area.contains(drawPoint)) {
            return;
        }

        // 3. apply crop
        if (!this.cropArea.contains(drawPoint)) {
            return;
        }

        // at this point we can be sure that the drawPoint is inside the drawBuffer, ad this x and y are positive,
        // so we can cover to a Position without problems
        Position drawPosition = new Position(drawPoint.x(), drawPoint.y());

        this.drawBuffer.draw(drawPosition, this.rendition, c);
    }

    /**
     * Draws a character at the given position.
     * <p>
     * The position is specified in the current drawing area space.
     * The character is drawn with the current graphical rendition.
     *
     * @param x the x coordinate of the position to draw at
     * @param y the y coordinate of the position to draw at
     * @param c the character to draw
     */
    public void draw(int x, int y, char c) {
        this.draw(new Position(x, y), c);
    }

    /**
     * Set the graphical rendition to use for drawing.
     *
     * @param rendition the graphical rendition to use
     */
    public void setRendition(GraphicalRendition rendition) {
        this.rendition = rendition;
    }

    /**
     * Update the current graphical rendition with the given weight.
     *
     * @param weight the weight to set
     */
    public void weight(GraphicalRenditionProperty.Weight weight) {
        this.rendition = this.rendition.withWeight(weight);
    }

    /**
     * Update the current graphical rendition with the given italics.
     *
     * @param italics the italics to set
     */
    public void italics(boolean italics) {
        this.rendition = this.rendition.withItalics(italics ? GraphicalRenditionProperty.Italics.ON : GraphicalRenditionProperty.Italics.OFF);
    }

    /**
     * Update the current graphical rendition with the given underline.
     *
     * @param underline the underline to set
     */
    public void underline(boolean underline) {
        this.rendition = rendition.withUnderline(underline ? GraphicalRenditionProperty.Underline.ON : GraphicalRenditionProperty.Underline.OFF);
    }

    /**
     * Update the current graphical rendition with the given foreground color.
     *
     * @param color the foreground color to set
     */
    public void foreground(GraphicalRenditionProperty.ForegroundColor color) {
        this.rendition = rendition.withForeground(color);
    }

    /**
     * Update the current graphical rendition with the given background color.
     *
     * @param color the background color to set
     */
    public void background(GraphicalRenditionProperty.BackgroundColor color) {
        this.rendition = rendition.withBackground(color);
    }

    /**
     * A point in a 2D space.
     * <p>
     * Note that, unlike {@link Position}, x and y can be negative.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected record Point(int x, int y) {
        /**
         * The origin of the 2D space.
         */
        public static final Point ZERO = new Point(0, 0);

        /**
         * Creates a new Point from a Position.
         *
         * @param position the position to create the point from
         * @return a new Point
         */
        public static Point from(Position position) {
            return new Point(position.x(), position.y());
        }

        /**
         * Adds the given values to the coordinates of this point.
         *
         * @param x the x value to add
         * @param y the y value to add
         * @return a new Point
         */
        public Point add(int x, int y) {
            return new Point(this.x + x, this.y + y);
        }

        /**
         * Adds the coordinates of the given point to this one.
         *
         * @param p the point to add
         * @return a new Point
         */
        public Point add(Point p) {
            return new Point(this.x + p.x, this.y + p.y);
        }
    }

    /**
     * A rectangle in a 2D space.
     *
     * @param tl        the top-left corner of the rectangle
     * @param dimension the dimensions of the rectangle
     */
    protected record Rectangle(Point tl, Dimensions dimension) {
        /**
         * The "zero" rectangle: a rectangle with a top-left corner at the origin and zero dimensions.
         */
        public static final Rectangle ZERO = new Rectangle(Point.ZERO, Dimensions.ZERO);

        /**
         * Checks whether this rectangle contains the given point.
         *
         * @param pos the point to check
         * @return true if the point is inside the rectangle, false otherwise
         */
        public boolean contains(Point pos) {
            return tl.x() <= pos.x() && pos.x() <= tl.x() + dimension.width() &&
                    tl.y() <= pos.y() && pos.y() <= tl.y() + dimension.height();
        }

        /**
         * Checks whether this rectangle fully contains the given rectangle.
         *
         * @param other the rectangle to check
         * @return true if the other rectangle is fully contained in this one, false otherwise
         */
        public boolean contains(Rectangle other) {
            Point otherBr = other.tl.add(other.dimension.width(), other.dimension.height());

            return this.contains(other.tl) && this.contains(otherBr);
        }

        /**
         * Gets the bottom-right corner of the rectangle.
         *
         * @return the bottom-right corner
         */
        public Point br() {
            return new Point(tl.x() + dimension.width(), tl.y() + dimension.height());
        }

        /**
         * Intersects this rectangle with another one.
         *
         * @param other the other rectangle to intersect with
         * @return an optional containing the intersection rectangle, or empty if the rectangles do not intersect
         */
        public Optional<Rectangle> intersect(Rectangle other) {
            Point tl = new Point(
                    Math.max(this.tl.x, other.tl.x),
                    Math.max(this.tl.y, other.tl.y)
            );

            int w = Math.min(this.br().x(), other.br().x()) - tl.x;
            int h = Math.min(this.br().y(), other.br().y()) - tl.y;
            if (w < 0 || h < 0) {
                return Optional.empty();
            }

            return Optional.of(new Rectangle(tl, new Dimensions(w, h)));
        }
    }

}
