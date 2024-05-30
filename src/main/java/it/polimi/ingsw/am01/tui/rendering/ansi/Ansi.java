package it.polimi.ingsw.am01.tui.rendering.ansi;

import java.util.List;
import java.util.stream.Collectors;

public class Ansi {
    public static final String ESC = "\033";
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
}
