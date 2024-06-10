package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;

import java.util.Objects;
import java.util.Optional;

public class DrawArea {
    private final DrawBuffer drawBuffer;
    private final Rectangle cropArea;
    private final Rectangle area;
    private GraphicalRendition rendition;

    public DrawArea(DrawBuffer drawBuffer) {
        Objects.requireNonNull(drawBuffer);

        Rectangle bufferArea = new Rectangle(new Point(0, 0), drawBuffer.dimensions());

        this.drawBuffer = drawBuffer;
        this.cropArea = bufferArea;
        this.area = bufferArea;
        this.rendition = GraphicalRendition.DEFAULT;
    }

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

    public DrawArea getSubarea(Position offset, Dimensions dimensions) {
        Rectangle subArea = new Rectangle(this.area.tl().add(Point.from(offset)), dimensions);
        if (!this.area.contains(subArea)) {
            throw new IllegalArgumentException("subarea must be enclosed in current drawing area.");
        }

        Rectangle cropArea = this.cropArea.intersect(subArea).orElse(Rectangle.ZERO);

        return new DrawArea(this.drawBuffer, cropArea, subArea);
    }

    public DrawArea getRelativeArea(int xOffset, int yOffset, Dimensions dimensions) {
        Rectangle newArea = new Rectangle(new Point(xOffset, yOffset), dimensions);
        Rectangle cropArea = this.cropArea.intersect(newArea).orElse(Rectangle.ZERO);

        return new DrawArea(this.drawBuffer, cropArea, newArea);
    }

    public Dimensions dimensions() {
        return this.area.dimension();
    }

    // position is specified in DrawArea space
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

    public void draw(int x, int y, char c) {
        this.draw(new Position(x, y), c);
    }

    public void setRendition(GraphicalRendition rendition) {
        this.rendition = rendition;
    }

    public void weight(GraphicalRenditionProperty.Weight weight) {
        this.rendition = this.rendition.withWeight(weight);
    }

    public void italics(boolean italics) {
        this.rendition = this.rendition.withItalics(italics ? GraphicalRenditionProperty.Italics.ON : GraphicalRenditionProperty.Italics.OFF);
    }

    public void underline(boolean underline) {
        this.rendition = rendition.withUnderline(underline ? GraphicalRenditionProperty.Underline.ON : GraphicalRenditionProperty.Underline.OFF);
    }

    public void foreground(GraphicalRenditionProperty.ForegroundColor color) {
        this.rendition = rendition.withForeground(color);
    }

    public void background(GraphicalRenditionProperty.BackgroundColor color) {
        this.rendition = rendition.withBackground(color);
    }

    // IMPORTANT! Point is different from Position because x and y can be negative
    protected record Point(int x, int y) {
        public static final Point ZERO = new Point(0, 0);

        public static Point from(Position position) {
            return new Point(position.x(), position.y());
        }

        public Point add(int x, int y) {
            return new Point(this.x + x, this.y + y);
        }

        public Point add(Point p) {
            return new Point(this.x + p.x, this.y + p.y);
        }
    }

    protected record Rectangle(Point tl, Dimensions dimension) {
        public static final Rectangle ZERO = new Rectangle(Point.ZERO, Dimensions.ZERO);

        public boolean contains(Point pos) {
            return tl.x() <= pos.x() && pos.x() <= tl.x() + dimension.width() &&
                    tl.y() <= pos.y() && pos.y() <= tl.y() + dimension.height();
        }

        public boolean contains(Rectangle other) {
            Point otherBr = other.tl.add(other.dimension.width(), other.dimension.height());

            return this.contains(other.tl) && this.contains(otherBr);
        }

        public Point br() {
            return new Point(tl.x() + dimension.width(), tl.y() + dimension.height());
        }

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
