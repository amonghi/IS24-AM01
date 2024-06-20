package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.player.PlayerColor;

public class Utils {
    private Utils() {
    }

    public static GraphicalRendition getPlayerColorRendition(PlayerColor color) {
        return switch (color) {
            case RED -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.RED);
            case BLUE -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.BLUE);
            case GREEN -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN);
            case YELLOW -> GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.YELLOW);
        };
    }

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

    public static String getCollectibleEmoji(Collectible cp) {
        return switch (cp) {
            case Item item -> getItemEmoji(item);
            case Resource resource -> getResourceEmoji(resource);
        };
    }

    public static String getItemEmoji(Item item) {
        return switch (item) {
            case QUILL -> "🪶";
            case INKWELL -> "🫙";
            case MANUSCRIPT -> "📜";
        };
    }

    public static String getResourceEmoji(Resource resource) {
        return switch (resource) {
            case PLANT -> "🌱";
            case FUNGI -> "🍄";
            case ANIMAL -> "🐺";
            case INSECT -> "🦋";
        };
    }
}
