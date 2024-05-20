package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.FXMLUtil;
import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class ComponentController extends Pane implements GUIElement {

    private final GUIView view;

    public ComponentController(GUIView view) {
        this.view = view;
    }

    public void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(ComponentController.class.getResource(
                FXMLUtil.getFXMLFullPath(this.getClass())
        ));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: handle
        }
    }

    public GUIView getView() {
        return view;
    }
}
