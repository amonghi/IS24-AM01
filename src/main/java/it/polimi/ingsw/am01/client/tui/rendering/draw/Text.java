package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Position;

/**
 * A utility class to draw text on a DrawArea.
 */
public class Text {
    private Text() {
    }

    /**
     * Writes the given text at the given position,  from left to right.
     *
     * @param a        the DrawArea to draw on
     * @param position the position to write at
     * @param text     the text to write
     */
    public static void write(DrawArea a, Position position, String text) {
        write(a, position.x(), position.y(), text);
    }

    /**
     * Writes the given text at the given position, from left to right.
     *
     * @param a    the DrawArea to draw on
     * @param x    the x coordinate of the position to write at
     * @param y    the y coordinate of the position to write at
     * @param text the text to write
     */
    public static void write(DrawArea a, int x, int y, String text) {
        for (int i = 0; i < text.length(); i++) {
            a.draw(x + i, y, text.charAt(i));
        }
    }

    /**
     * Writes the given text at the given position, centered.
     *
     * @param a        the DrawArea to draw on
     * @param position the position to write at
     * @param text     the text to write
     */
    public static void writeCentered(DrawArea a, Position position, String text) {
        writeCentered(a, position.x(), position.y(), text);
    }

    /**
     * Writes the given text at the given position, centered.
     *
     * @param a    the DrawArea to draw on
     * @param x    the x coordinate of the position to write at
     * @param y    the y coordinate of the position to write at
     * @param text the text to write
     */
    public static void writeCentered(DrawArea a, int x, int y, String text) {
        int leftX = x - text.length() / 2;
        write(a, leftX, y, text);
    }
}
