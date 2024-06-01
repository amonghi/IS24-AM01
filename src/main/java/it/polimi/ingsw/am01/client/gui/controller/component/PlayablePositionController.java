package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class PlayablePositionController extends Rectangle implements ComponentController {

    public PlayablePositionController(int x, int y) {
        loadComponent();
        setPosition(x, y);
    }

    @FXML
    private void initialize() {
    }

    public void setPosition(int x, int y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    @Override
    public String getFXMLFileName() {
        return "playable_position";
    }
}
