package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;

import java.util.Arrays;
import java.util.Optional;

public class DrawBuffer {
    private final Dimensions dimensions;
    private final char[] charBuffer;
    private final GraphicalRendition[] renditions;

    public DrawBuffer(Dimensions dimensions, char bg) {
        this.dimensions = dimensions;
        charBuffer = new char[dimensions.width() * dimensions.height()];
        renditions = new GraphicalRendition[dimensions().width() * dimensions().height()];
        Arrays.fill(this.charBuffer, bg);
    }

    public Dimensions dimensions() {
        return dimensions;
    }

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

    public DrawArea getDrawArea() {
        return new DrawArea(this, Position.ZERO, this.dimensions);
    }

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
