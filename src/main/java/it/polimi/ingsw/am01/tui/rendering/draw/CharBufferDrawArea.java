package it.polimi.ingsw.am01.tui.rendering.draw;

import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;

import java.util.Arrays;

public class CharBufferDrawArea implements DrawArea {
    private final Dimensions dimensions;
    private final char[] buffer; // FIXME: with chars only we cant store ANSI sequences

    public CharBufferDrawArea(Dimensions dimensions, char bg) {
        this.dimensions = dimensions;
        buffer = new char[dimensions.width() * dimensions.height()];
        Arrays.fill(this.buffer, bg);
    }

    public char[] getBuffer() {
        return buffer;
    }

    @Override
    public Dimensions dimensions() {
        return dimensions;
    }

    @Override
    public void draw(int x, int y, char c) {
        this.buffer[y * dimensions.width() + x] = c;
    }

    @Override
    public DrawArea window(Position p, Dimensions d) {
        return new WindowDrawArea(this, p, d);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.dimensions.height(); i++) {
            builder.append(this.buffer, i * this.dimensions.width(), this.dimensions.width()).append('\n');
        }

        return builder.toString();
    }
}
