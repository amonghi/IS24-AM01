package it.polimi.ingsw.am01.client.gui.controller;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.scene.paint.Color;

public class Utils {
    public static Color convertColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> Color.RED;
            case YELLOW -> Color.YELLOW;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
        };
    }
}
