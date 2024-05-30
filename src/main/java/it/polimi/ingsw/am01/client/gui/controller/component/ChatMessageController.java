package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ChatMessageController extends AnchorPane implements ComponentController {

    private final String type;
    private final String sender;
    private final String content;
    private final String timestamp;
    @FXML
    private Label typeLabel;
    @FXML
    private Label senderLabel;
    @FXML
    private Label contentLabel;

    public ChatMessageController(String type, String sender, String content, String timestamp) {
        this.type = type;
        if (sender.equals(GUIView.getInstance().getPlayerName())) {
            this.sender = "You";
        } else {
            this.sender = sender;
        }
        this.content = content;
        this.timestamp = timestamp;

        loadComponent();
    }

    @FXML
    private void initialize() {
        typeLabel.setText("[" + type.split("")[0] + "]");
        senderLabel.setText("(" + timestamp.split("T")[1].split("\\.")[0] + ") " + sender + ": ");
        contentLabel.setText(content);
    }

    @Override
    public String getFXMLFileName() {
        return "chat_message";
    }
}
