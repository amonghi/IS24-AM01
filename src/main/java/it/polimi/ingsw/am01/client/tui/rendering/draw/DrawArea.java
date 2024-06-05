package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;

import java.util.Objects;

public class DrawArea {
    private final DrawBuffer drawBuffer;
    private final Position offset;
    private final Dimensions dimensions;
    private GraphicalRendition rendition;

    public DrawArea(DrawBuffer drawBuffer, Position offset, Dimensions dimensions) {
        Objects.requireNonNull(drawBuffer);
        Objects.requireNonNull(offset);
        Objects.requireNonNull(dimensions);

        if (offset.x() + dimensions.width() > drawBuffer.dimensions().width()) {
            throw new IllegalArgumentException("DrawArea out of bounds");
        }

        if (offset.y() + dimensions.height() > drawBuffer.dimensions().height()) {
            throw new IllegalArgumentException("DrawArea window out of bounds");
        }

        this.drawBuffer = drawBuffer;
        this.offset = offset;
        this.dimensions = dimensions;
        this.rendition = GraphicalRendition.DEFAULT;
    }

    public DrawArea getSubarea(Position offset, Dimensions dimensions) {
        return new DrawArea(this.drawBuffer, this.offset.add(offset), dimensions);
    }

    public Dimensions dimensions() {
        return this.dimensions;
    }

    public void draw(Position position, char c) {
        this.drawBuffer.draw(position.add(this.offset), this.rendition, c);
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

}
