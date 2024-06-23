package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;

import java.util.Arrays;
import java.util.Optional;

/**
 * A buffer to draw on.
 * <p>
 * It is a 2D array of characters with a fixed size.
 * Each character can have a graphical rendition associated with it.
 */
public class DrawBuffer {
    private final Dimensions dimensions;
    private final char[] charBuffer;
    private final GraphicalRendition[] renditions;

    /**
     * Creates a new DrawBuffer with the given dimensions and background character.
     *
     * @param dimensions the dimensions of the buffer
     * @param bg         the background character
     */
    public DrawBuffer(Dimensions dimensions, char bg) {
        this.dimensions = dimensions;
        charBuffer = new char[dimensions.width() * dimensions.height()];
        renditions = new GraphicalRendition[dimensions().width() * dimensions().height()];
        Arrays.fill(this.charBuffer, bg);
    }

    /**
     * @return the dimensions of the buffer
     */
    public Dimensions dimensions() {
        return dimensions;
    }

    /**
     * Draws a character with the given graphical rendition at the given position.
     *
     * @param position  the position to draw at
     * @param rendition the graphical rendition to use
     * @param c         the character to draw
     */
    public void draw(Position position, GraphicalRendition rendition, char c) {
        int x = position.x();
        int y = position.y();

        // ensure draw is within bounds
        if (x < 0 || y < 0 || x >= dimensions.width() || y >= dimensions.height()) {
            return;
        }

        this.renditions[y * dimensions.width() + x] = rendition;
        this.charBuffer[y * dimensions.width() + x] = c;
    }

    /**
     * Gets a {@link DrawArea} that can be used to draw on this buffer.
     *
     * @return a {@link DrawArea} that can be used to draw on this buffer
     */
    public DrawArea getDrawArea() {
        return new DrawArea(this);
    }

    /**
     * Writes this buffer to a string, ready to be printed.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        GraphicalRendition prevRendition = null;

        for (int j = 0; j < this.dimensions.height(); j++) {
            for (int i = 0; i < this.dimensions.width(); i++) {
                // append ansi sequence
                GraphicalRendition currRendition = Optional.ofNullable(this.renditions[j * this.dimensions.width() + i])
                        .orElse(GraphicalRendition.DEFAULT);

                String ansi = prevRendition != null
                        ? currRendition.getUpdateAnsiSequence(prevRendition)
                        : currRendition.getAnsiSequence();
                builder.append(ansi);

                // append char
                builder.append(this.charBuffer[j * this.dimensions.width() + i]);

                prevRendition = currRendition;
            }

            if (j != this.dimensions.height() - 1) {
                builder.append("\r\n");
            }
        }

        return builder.toString();
    }
}
