package it.polimi.ingsw.am01.client.gui.controller.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * The controller for the popup responsible for choosing the maximum number of
 * players allowed in the game
 *
 * @see PopupController
 */
public class GameCreationPopupController extends PopupController {

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4);
        valueFactory.setValue(2);

        spinner.setValueFactory(valueFactory);
    }

    /**
     * It calls the {@link it.polimi.ingsw.am01.client.View#createGameAndJoin(int)} method to
     * create a new game with the specified maximum number of players
     * <p>
     * It also closes the popup calling the {@link PopupController#closePopup()} method
     */
    @FXML
    private void confirm() {
        view.createGameAndJoin(spinner.getValue());
        closePopup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "create_game_popup";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPopupTitle() {
        return "Create a game";
    }
}
