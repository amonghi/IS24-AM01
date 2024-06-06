package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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

    @FXML
    private void setConnectionType() {
        if (toggleGroup.getSelectedToggle() == tcpRadioButton) {
            portText.setText(Constants.DEFAULT_TCP_PORT + "");
        } else {
            portText.setText(Constants.DEFAULT_RMI_PORT + "");
        }
    }

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

    @FXML
    public void setErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public String getFXMLFileName() {
        return "connect_client";
    }
}
