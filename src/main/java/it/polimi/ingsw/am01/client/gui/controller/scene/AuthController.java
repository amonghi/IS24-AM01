package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.NameAlreadyTakenEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * The main controller for the scene associated to this {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#NOT_AUTHENTICATED} </li>
 * </ul>
 *
 * @see SceneController
 */
public class AuthController extends SceneController {

    @FXML
    private TextField nameField;
    @FXML
    private Button confirmButton;
    @FXML
    private Label messageLabel;
    @FXML
    private AnchorPane background;

    /**
     * It constructs a new AuthController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public AuthController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        messageLabel.setVisible(false);
    }

    /**
     * It shows a message telling that the name selected by the player has been refused
     *
     * @param event The event received from the {@link View} containing the refused name
     */
    public void nameAlreadyTaken(NameAlreadyTakenEvent event) {
        messageLabel.setVisible(true);
        messageLabel.setText("Name '" + event.refusedName() + "' is already taken!");
    }

    /**
     * It calls the {@link View#authenticate(String)} method to authenticate the player
     */
    @FXML
    private void confirmName() {
        if (nameField.getText().isEmpty()) {
            messageLabel.setVisible(true);
            messageLabel.setText("Name is empty!");
            return;
        }

        view.authenticate(nameField.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(NameAlreadyTakenEvent.class, this::nameAlreadyTaken)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "auth";
    }
}
