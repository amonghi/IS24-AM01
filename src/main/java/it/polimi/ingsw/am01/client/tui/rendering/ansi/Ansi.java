package it.polimi.ingsw.am01.client.tui.rendering.ansi;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating ANSI escape codes.
 */
public class Ansi {
    /**
     * Escape character.
     */
    public static final char ESC = '\033';

    /**
     * Control Sequence Introducer.
     * <p>
     * This is the sequence that starts all ANSI escape codes.
     */
    public static final String CSI = ESC + "[";

    private Ansi() {
    }

    /**
     * Generate an SGR (Select Graphic Rendition) escape code.
     *
     * @param renditionProperties List of rendition properties to apply.
     * @return The SGR escape code.
     */
    public static String SGR(List<GraphicalRenditionProperty> renditionProperties) {
        if (renditionProperties.isEmpty()) {
            return "";
        }

        String str = renditionProperties.stream()
                .mapToInt(GraphicalRenditionProperty::getAnsiCode)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(";"));
        return CSI + str + "m";
    }

    /**
     * Generate a cursor position escape code.
     *
     * @param row The row to move the cursor to.
     * @param col The column to move the cursor to.
     * @return The cursor position escape code.
     */
    public static String setCursorPosition(int row, int col) {
        // the cursor position in the terminal is 1-based, but everywhere else in this application we use 0-based
        return CSI + (row + 1) + ";" + (col + 1) + "H";
    }

    /**
     * ANSI escape code that erases the screen from the cursor position to the end of the screen.
     */
    public static String eraseInDisplay = CSI + "J";
}
