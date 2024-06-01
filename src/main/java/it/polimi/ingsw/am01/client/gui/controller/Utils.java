package it.polimi.ingsw.am01.client.gui.controller;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.scene.paint.Color;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;

public class Utils {
    public static Color convertColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> Color.RED.brighter().brighter().brighter();
            case YELLOW -> Color.YELLOW.brighter().brighter().brighter();
            case BLUE -> Color.BLUE.brighter().brighter().brighter();
            case GREEN -> Color.GREEN.brighter().brighter().brighter();
        };
    }

    public static int computeXPosition(int i, int j) {
        int xPos;
        if (i == 0 && j == 0) {
            xPos = BASE_X;
        } else if (i == 0) {
            xPos = BASE_X - OFFSET_X * j;
        } else if (j == 0) {
            xPos = BASE_X + OFFSET_X * i;
        } else {
            xPos = BASE_X + OFFSET_X * i;
            xPos = xPos - OFFSET_X * j;
        }
        return xPos;
    }

    public static int computeYPosition(int i, int j) {
        int yPos;
        if (i == 0 && j == 0) {
            yPos = BASE_Y;
        } else if (i == 0) {
            yPos = BASE_Y - OFFSET_Y * j;
        } else if (j == 0) {
            yPos = BASE_Y - OFFSET_Y * i;
        } else {
            yPos = BASE_Y - OFFSET_Y * i;
            yPos = yPos - OFFSET_Y * j;
        }
        return yPos;
    }
}
