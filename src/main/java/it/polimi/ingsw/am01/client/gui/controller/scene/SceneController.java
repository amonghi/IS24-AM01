package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.client.gui.controller.popup.PopupController;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SceneController implements GUIElement {

    private final List<EventEmitter.Registration> viewRegistrations;
    private Stage primaryStage;

    public SceneController() {
        this.viewRegistrations = new ArrayList<>();
    }

    public void loadScene(Stage stage, double width, double height) {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(
                Constants.FXML_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setController(this);
        this.primaryStage = stage;

        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: manage
        }
        stage.setTitle(Constants.WINDOW_TITLE);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    protected void openPopup(PopupController popupController) {
        popupController.loadPopup(primaryStage);
    }

    protected abstract void registerListeners();

    public List<EventEmitter.Registration> getViewRegistrations() {
        return viewRegistrations;
    }
}
