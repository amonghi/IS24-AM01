package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ChatMessageController extends AnchorPane implements ComponentController {

    private final View.Message message;
    private final View view;
    @FXML
    private Label typeLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label contentLabel;

    public ChatMessageController(View.Message message, View view) {
        this.view = view;
        this.message = message;

        loadComponent();
    }

    @FXML
    private void initialize() {
        String recString = "";
        String senderString = message.sender();
        typeLabel.setText("[" + message.type().toString().split("")[0] + "]");

        if (message.sender().equals(view.getPlayerName())) {
            senderString = "You";
        }

        if (message.type().equals(MessageType.DIRECT) && message.sender().equals(view.getPlayerName())) {
            recString = " --> " + message.recipient();
        }

        infoLabel.setText("(" + message.timestamp().split("T")[1].split("\\.")[0] + ") " + senderString + recString + ": ");
        contentLabel.setText(message.content());
    }

    @Override
    public String getFXMLFileName() {
        return "chat_message";
    }
}
