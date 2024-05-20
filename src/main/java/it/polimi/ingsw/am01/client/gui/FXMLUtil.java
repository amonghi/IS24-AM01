package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.controller.scene.AuthController;
import it.polimi.ingsw.am01.client.gui.controller.scene.GameListController;

import java.util.Map;

public class FXMLUtil {
    private static final String EXTENSION = ".fxml";
    private static final String RESOURCES_PATH = "/it/polimi/ingsw/am01/";

    private static final Map<Class<? extends GUIElement>, String> resources = Map.ofEntries(
            Map.entry(AuthController.class, "auth"),
            Map.entry(GameListController.class, "games_list"),
            Map.entry(GameController.class, "game")
    );

    public static String getFXMLFullPath(Class<? extends GUIElement> controllerClass) {
        return RESOURCES_PATH + resources.get(controllerClass) + EXTENSION;
    }
}
