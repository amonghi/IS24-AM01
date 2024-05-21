package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SceneController implements GUIElement {

    private final List<EventEmitter.Registration> viewRegistrations;

    public SceneController() {
        this.viewRegistrations = new ArrayList<>();
    }

    public void loadScene(Stage stage, double width, double height) {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(
                Constants.RESOURCES_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setController(this);
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: manage
        }
        stage.setTitle(Constants.WINDOW_TITLE);
        stage.setScene(scene);

        registerListeners();

        stage.show();
    }

    protected abstract void registerListeners();

    public List<EventEmitter.Registration> getViewRegistrations() {
        return viewRegistrations;
    }
}
