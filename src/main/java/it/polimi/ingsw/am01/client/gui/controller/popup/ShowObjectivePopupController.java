package it.polimi.ingsw.am01.client.gui.controller.popup;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;

public class ShowObjectivePopupController extends PopupController {

    private List<Integer> objectives;
    @FXML
    private ImageView obj1;
    @FXML
    private ImageView obj2;
    @FXML
    private ImageView obj3;

    public ShowObjectivePopupController(List<Integer> objectives) {
        this.objectives = objectives;
        super.setWidth(800);
        super.setHeight(200);
    }

    @FXML
    private void initialize() {
        obj1.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.OBJECTIVE_PATH + objectives.get(0) + Constants.IMAGE_EXTENSION)).toString()));
        obj2.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.OBJECTIVE_PATH + objectives.get(1) + Constants.IMAGE_EXTENSION)).toString()));
        obj3.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.OBJECTIVE_PATH + objectives.get(2) + Constants.IMAGE_EXTENSION)).toString()));
    }

    @FXML
    private void confirm() {
        closePopup();
    }

    @Override
    public String getFXMLFileName() {
        return "objective_popup";
    }

    @Override
    protected String getPopupTitle() {
        return "Your objective";
    }
}
