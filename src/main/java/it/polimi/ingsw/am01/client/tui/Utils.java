package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.player.PlayerColor;

/**
 * Utility class containing various utility methods for the TUI.
 * Mostly mapping methods from model objects to graphical renditions.
 */
public class Utils {
    private Utils() {
    }

    /**
     * Converts a {@link PlayerColor} to a {@link GraphicalRendition} to be used for rendering.
     *
     * @param color the player color to convert
     * @return the graphical rendition for the given color
     */
    public static GraphicalRendition getPlayerColorRendition(PlayerColor color) {
        return switch (color) {
            case RED -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED);
            case BLUE -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.BLUE);
            case GREEN -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN);
            case YELLOW -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.YELLOW);
        };
    }

    /**
     * Converts a {@link CardColor} to a {@link GraphicalRendition} to be used for rendering.
     *
     * @param color the card color to convert
     * @return the graphical rendition for the given color
     */
    public static GraphicalRendition getCardColorRendition(CardColor color) {
        return switch (color) {
            case RED -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED);
            case BLUE -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.BLUE);
            case GREEN -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN);
            case PURPLE ->
                    GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.MAGENTA);
            case NEUTRAL -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE);
        };
    }

    /**
     * Given a {@link Collectible} returns a string containing an emoji that represents that collectible.
     *
     * @param cp the collectible
     * @return the emoji associated with the collectible
     */
    public static String getCollectibleEmoji(Collectible cp) {
        return switch (cp) {
            case Item item -> getItemEmoji(item);
            case Resource resource -> getResourceEmoji(resource);
        };
    }

    /**
     * Given an {@link Item} returns a string containing an emoji that represents that item.
     *
     * @param item the item
     * @return the emoji associated with the item
     */
    public static String getItemEmoji(Item item) {
        return switch (item) {
            case QUILL -> "🪶";
            case INKWELL -> "🫙";
            case MANUSCRIPT -> "📜";
        };
    }

    /**
     * Given a {@link Resource} returns a string containing an emoji that represents that resource.
     *
     * @param resource the resource
     * @return the emoji associated with the resource
     */
    public static String getResourceEmoji(Resource resource) {
        return switch (resource) {
            case PLANT -> "🌱";
            case FUNGI -> "🍄";
            case ANIMAL -> "🐺";
            case INSECT -> "🦋";
        };
    }
}
