package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public interface ComponentController extends GUIElement {

    default void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(ComponentController.class.getResource(
                Constants.FXML_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: handle
        }
    }
}
