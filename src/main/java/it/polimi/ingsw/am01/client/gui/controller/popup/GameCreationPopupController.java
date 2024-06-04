package it.polimi.ingsw.am01.client.gui.controller.popup;

import it.polimi.ingsw.am01.client.View;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class GameCreationPopupController extends PopupController {

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4);
        valueFactory.setValue(2);

        spinner.setValueFactory(valueFactory);
    }

    @FXML
    private void confirm() {
        View.getInstance().createGameAndJoin(spinner.getValue());
        closePopup();
    }

    @Override
    public String getFXMLFileName() {
        return "create_game_popup";
    }

    @Override
    protected String getPopupTitle() {
        return "Create a game";
    }
}
