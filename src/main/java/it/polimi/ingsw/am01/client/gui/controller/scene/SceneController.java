package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.FXMLUtil;
import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SceneController implements GUIElement {

    private final GUIView view;

    private final String fxmlFileName;

    private final List<EventEmitter.Registration> viewRegistrations;

    public SceneController(GUIView view) {
        this.view = view;
        this.fxmlFileName = FXMLUtil.getFXMLFullPath(this.getClass());
        this.viewRegistrations = new ArrayList<>();
    }

    public void loadScene(Stage stage, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlFileName));
        fxmlLoader.setController(this);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: manage
        }
        stage.setTitle(title);
        stage.setScene(scene);

        registerListeners();

        stage.show();
    }

    protected abstract void registerListeners();

    public GUIView getView() {
        return view;
    }

    public List<EventEmitter.Registration> getViewRegistrations() {
        return viewRegistrations;
    }
}
