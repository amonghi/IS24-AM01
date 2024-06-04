package it.polimi.ingsw.am01.client.tui.rendering.ansi;

import java.util.List;
import java.util.stream.Collectors;

public class Ansi {
    public static final char ESC = '\033';
    // Control Sequence Introducer
    public static final String CSI = ESC + "[";

    private Ansi() {
    }

    // Set Graphical Rendition
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


    // note: cursor position in the terminal is 1-based but everywhere else in this application we use 0-based
    public static String setCursorPosition(int row, int col) {
        return CSI + (row + 1) + ";" + (col + 1) + "H";
    }

    // actually, there are various options be this is the only one I need
    public static String eraseInDisplay = CSI + "J";
}
