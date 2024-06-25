package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * The main controller for a component.
 * A component is a {@link GUIElement} that exists only as part of a scene
 *
 * @see GUIElement
 */
public interface ComponentController extends GUIElement {

    /**
     * It tries to load the component from its fxml file.
     * It also sets itself as root and controller
     */
    default void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(ComponentController.class.getResource(
                Constants.FXML_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            // this is unrecoverable
            throw new Error(e);
        }
    }
}
