package it.polimi.ingsw.am01.client.gui.controller;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.scene.paint.Color;

public class Utils {
    public static Color convertColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> Color.RED.brighter().brighter().brighter();
            case YELLOW -> Color.YELLOW.brighter().brighter().brighter();
            case BLUE -> Color.BLUE.brighter().brighter().brighter();
            case GREEN -> Color.GREEN.brighter().brighter().brighter();
        };
    }
}
