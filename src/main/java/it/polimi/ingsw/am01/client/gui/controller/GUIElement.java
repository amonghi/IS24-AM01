package it.polimi.ingsw.am01.client.gui.controller;

/**
 * The common interface for components and scenes
 *
 * @see it.polimi.ingsw.am01.client.gui.controller.component.ComponentController
 * @see it.polimi.ingsw.am01.client.gui.controller.scene.SceneController
 */
public interface GUIElement {

    /**
     * @return the fxml file name describing the style of a certain scene or component
     */
    String getFXMLFileName();
}
