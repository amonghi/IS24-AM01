package it.polimi.ingsw.am01.client.gui.controller;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;

/**
 * Utility class containing the common methods needed to:
 * <ul>
 *     <li> Place cards in the playarea </li>
 *     <li> Get colors </li>
 *     <li> Move pane for animations </li>
 * </ul>
 */
public class Utils {

    /**
     * It returns a {@link Color}, given a certain {@link PlayerColor}
     *
     * @param playerColor The {@link PlayerColor} of a player
     * @return the {@link Color} to be shown
     */
    public static Color convertColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> Color.RED.brighter().brighter().brighter();
            case YELLOW -> Color.YELLOW.brighter().brighter().brighter();
            case BLUE -> Color.BLUE.brighter().brighter().brighter();
            case GREEN -> Color.GREEN.brighter().brighter().brighter();
        };
    }

    /**
     * It returns a {@link Color}, given a certain {@link PlayerColor}
     *
     * @param playerColor The {@link PlayerColor} of a player
     * @return the {@link Color} to be shown
     */
    public static String backgroundColorHex(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> "#ff8080";
            case YELLOW -> "#ffe680";
            case BLUE -> "#8080ff";
            case GREEN -> "#99e699";
        };
    }

    /**
     * It converts a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position} to a cartesian coordinate system,
     * returning the x-coordinate
     *
     * @param i The i-coordinate of a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position}
     * @param j The j-coordinate if a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position}
     * @return The x-coordinate of the main {@link javafx.scene.layout.AnchorPane}, where the card has to be placed
     */
    public static double computeXPosition(int i, int j) {
        final double OFFSET_X = CARD_PLACEMENT_WIDTH * (1 - X_RATIO);
        double xPos;
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

    /**
     * It converts a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position} to a cartesian coordinate system,
     * returning the y-coordinate
     *
     * @param i The i-coordinate of a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position}
     * @param j The j-coordinate if a {@link it.polimi.ingsw.am01.model.game.PlayArea.Position}
     * @return The y-coordinate of the main {@link javafx.scene.layout.AnchorPane}, where the card has to be placed
     */
    public static double computeYPosition(int i, int j) {
        final double OFFSET_Y = CARD_PLACEMENT_HEIGHT * (1 - Y_RATIO);
        double yPos;
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

    /**
     * It moves the specified {@link Node} to the specified position
     *
     * @param position The final x-coordinate that the translating node has to reach
     * @param node     The {@link Node} to move
     */
    public static void movePane(float position, Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1));
        tt.setNode(node);
        tt.setToX(position);
        tt.play();
    }
}
