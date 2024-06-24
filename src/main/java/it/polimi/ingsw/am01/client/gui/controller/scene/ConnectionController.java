package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * The main controller for the scene associated to this {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#NOT_CONNECTED} </li>
 * </ul>
 *
 * @see SceneController
 */
public class ConnectionController extends SceneController {
    @FXML
    private RadioButton tcpRadioButton;
    @FXML
    private RadioButton rmiRadioButton;
    @FXML
    private TextField ipText;
    @FXML
    private TextField portText;
    @FXML
    private Label messageLabel;
    private ToggleGroup toggleGroup;

    /**
     * It constructs a new ConnectionController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public ConnectionController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        toggleGroup = new ToggleGroup();
        tcpRadioButton.setToggleGroup(toggleGroup);
        rmiRadioButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(tcpRadioButton);
        ipText.setText(Constants.DEFAULT_HOSTNAME);
        portText.setText(Constants.DEFAULT_TCP_PORT + "");
        messageLabel.setVisible(false);
    }

    /**
     * It sets the default port number for RMI or TCP, based on the choice of the player
     */
    @FXML
    private void setConnectionType() {
        if (toggleGroup.getSelectedToggle() == tcpRadioButton) {
            portText.setText(Constants.DEFAULT_TCP_PORT + "");
        } else {
            portText.setText(Constants.DEFAULT_RMI_PORT + "");
        }
    }

    /**
     * It calls the {@link View#connect(View.ConnectionType, String, int)} method using the
     * network information, ip address and port, given by the player
     */
    @FXML
    private void confirm() {
        View.ConnectionType connectionType;
        if (toggleGroup.getSelectedToggle() == tcpRadioButton) {
            connectionType = View.ConnectionType.TCP;
        } else {
            connectionType = View.ConnectionType.RMI;
        }
        try {
            view.connect(connectionType, ipText.getText(), Integer.parseInt(portText.getText()));
        } catch (NumberFormatException e) {
            messageLabel.setText("Please specify a valid port");
            messageLabel.setVisible(true);
        }

    }

    /**
     * It shows an error message, if a connection problem occur
     *
     * @param message The error message to show
     */
    @FXML
    public void setErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "connect_client";
    }
}
