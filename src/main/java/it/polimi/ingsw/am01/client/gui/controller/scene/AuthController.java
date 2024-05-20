package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.event.NameAlreadyTakenEvent;
import it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class AuthController extends SceneController {

    @FXML
    private TextField nameField;

    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    public AuthController(GUIView view) {
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
    private void confirmName(ActionEvent event) {
        if (nameField.getText().isEmpty()) {
            messageLabel.setVisible(true);
            messageLabel.setText("Name is empty!");
            return;
        }

        AuthenticateC2S message = new AuthenticateC2S(
                nameField.getText()
        );

        getView().sendMessage(message);
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                getView().on(NameAlreadyTakenEvent.class, this::nameAlreadyTaken)
        ));
    }
}
