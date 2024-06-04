package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ChatMessageController extends AnchorPane implements ComponentController {

    private final String type;
    private final String sender;
    private final String content;
    private final String recipient;
    private final String timestamp;
    @FXML
    private Label typeLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label contentLabel;

    public ChatMessageController(String type, String sender, String recipient, String content, String timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;

        loadComponent();
    }

    @FXML
    private void initialize() {
        String recString = "";
        String senderString = sender;
        typeLabel.setText("[" + type.split("")[0] + "]");

        if (sender.equals(View.getInstance().getPlayerName())) {
            senderString = "You";
        }

        if (type.equals(MessageType.DIRECT.toString()) && sender.equals(View.getInstance().getPlayerName())) {
            recString = " --> " + recipient;
        }

        infoLabel.setText("(" + timestamp.split("T")[1].split("\\.")[0] + ") " + senderString + recString + ": ");
        contentLabel.setText(content);
    }

    @Override
    public String getFXMLFileName() {
        return "chat_message";
    }
}
