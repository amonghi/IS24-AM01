package it.polimi.ingsw.am01.client.tui.rendering.draw;

import it.polimi.ingsw.am01.client.tui.rendering.Position;

public class Text {
    private Text() {
    }

    public static void write(DrawArea a, Position position, String text) {
        write(a, position.x(), position.y(), text);
    }

    public static void write(DrawArea a, int x, int y, String text) {
        for (int i = 0; i < text.length(); i++) {
            a.draw(x + i, y, text.charAt(i));
        }
    }

    public static void writeCentered(DrawArea a, Position position, String text) {
        writeCentered(a, position.x(), position.y(), text);
    }

    public static void writeCentered(DrawArea a, int x, int y, String text) {
        int leftX = x - text.length() / 2;
        write(a, leftX, y, text);
    }
}
