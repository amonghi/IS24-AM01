package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.NameAlreadyTakenEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthController extends SceneController {

    @FXML
    private TextField nameField;

    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    public AuthController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        messageLabel.setVisible(false);
    }

    public void nameAlreadyTaken(NameAlreadyTakenEvent event) {
        messageLabel.setVisible(true);
        messageLabel.setText("Name '" + event.refusedName() + "' is already taken!");
    }

    @FXML
    private void confirmName() {
        if (nameField.getText().isEmpty()) {
            messageLabel.setVisible(true);
            messageLabel.setText("Name is empty!");
            return;
        }

        view.authenticate(nameField.getText());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(NameAlreadyTakenEvent.class, this::nameAlreadyTaken)
        );
    }

    @Override
    public String getFXMLFileName() {
        return "auth";
    }
}
