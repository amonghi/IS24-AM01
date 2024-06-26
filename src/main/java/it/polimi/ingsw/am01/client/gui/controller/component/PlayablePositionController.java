package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

/**
 * The controller for the component that represent a placeholder to show a playable position
 */
public class PlayablePositionController extends Rectangle implements ComponentController {

    /**
     * It constructs a new PlayablePositionController.
     * It also calls the {@link ComponentController#loadComponent()} method and the
     * {@link this#setPosition(double, double)} method
     *
     * @param x The x-coordinate of the layer where the playable position's placeholder has to be placed
     * @param y The y-coordinate of the layer where the playable position's placeholder has to be placed
     */
    public PlayablePositionController(double x, double y) {
        loadComponent();
        setPosition(x, y);
    }

    @FXML
    private void initialize() {
    }

    /**
     * It sets the x and y coordinates of the playable position's placeholder
     *
     * @param x The x-coordinate of the layer where the playable position's placeholder has to be placed
     * @param y The y-coordinate of the layer where the playable position's placeholder has to be placed
     */
    public void setPosition(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "playable_position";
    }
}
