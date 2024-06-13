package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.model.player.PlayerColor;

public class Utils {
    public static GraphicalRendition getColorRendition(PlayerColor color) {
        return switch (color) {
            case RED -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED);
            case BLUE -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.BLUE);
            case GREEN -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN);
            case YELLOW -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.YELLOW);
        };
    }
}
