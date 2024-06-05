package it.polimi.ingsw.am01.client.gui.controller.popup;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.client.gui.controller.component.ComponentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class PopupController implements GUIElement {

    private Stage popupStage;
    private double width = Constants.POPUP_WIDTH;
    private double height = Constants.POPUP_HEIGHT;

    public void loadPopup(Stage primaryStage) {
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

    protected void setWidth(double width) {
        this.width = width;
    }

    protected void setHeight(double height) {
        this.height = height;
    }

    protected void closePopup() {
        popupStage.close();
    }

    protected String getPopupTitle() {
        return "";
    }
}
