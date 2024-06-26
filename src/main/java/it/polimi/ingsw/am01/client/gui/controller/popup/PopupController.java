package it.polimi.ingsw.am01.client.gui.controller.popup;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.client.gui.controller.component.ComponentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller for a generic customized popup
 *
 * @see GameCreationPopupController
 */
public abstract class PopupController implements GUIElement {

    protected View view;
    private Stage popupStage;
    private double width = Constants.POPUP_WIDTH;
    private double height = Constants.POPUP_HEIGHT;

    /**
     * It tries to load the popup component from its fxml file.
     * It also sets itself as controller
     *
     * @param primaryStage The main stage
     * @param view         The main {@link View} class, containing the local and reduced copy of server data
     */
    public void loadPopup(Stage primaryStage, View view) {
        this.view = view;
        FXMLLoader fxmlLoader = new FXMLLoader(ComponentController.class.getResource(
                Constants.FXML_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setController(this);
        Parent layout;

        try {
            layout = fxmlLoader.load();
            Scene scene = new Scene(layout, width, height);

            Stage popupStage = new Stage();

            this.popupStage = popupStage;

            popupStage.initOwner(primaryStage);

            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle(getPopupTitle());
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It closes the popup stage
     */
    protected void closePopup() {
        popupStage.close();
    }

    /**
     * @return The title of the popup
     */
    protected String getPopupTitle() {
        return "";
    }
}
