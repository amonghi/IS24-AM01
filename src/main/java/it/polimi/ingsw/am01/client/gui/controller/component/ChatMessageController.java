package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class ChatMessageController extends HBox implements ComponentController {

    private final static String baseMessageStyle = "-fx-background-color: black; -fx-background-radius: 5; -fx-text-fill: white; -fx-padding: 2";
    private final String type;
    private final String sender;
    private final String content;
    private final String recipient;
    private final String timestamp;
    private final View view;
  
    @FXML
    private Label senderLabel;
    @FXML
    private Label recipientLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private TextArea message;

    public ChatMessageController(View.Message message, View view) {
        this.view = view;
        this.type = message.type().toString();
        this.sender = message.sender();
        this.content = message.content();
        this.recipient = message.recipient();
        this.timestamp = message.timestamp();

        loadComponent();
    }

    @FXML
    private void initialize() {
        String senderString = sender.equals(view.getPlayerName()) ? "You" : sender;
        String recipientString = type.equals(MessageType.BROADCAST.toString())
                ? "Everyone"
                : recipient.equals(view.getPlayerName()) ? "You" : recipient;


        senderLabel.setText(senderString);
        recipientLabel.setText(recipientString);
        timeLabel.setText(timestamp.split("T")[1].split("\\.")[0]);
        message.setText(content);

        //Define base style
        senderLabel.setStyle(baseMessageStyle);
        recipientLabel.setStyle(baseMessageStyle);

        //Define colors, if present
        if (view.getPlayerColor(sender) != null) {
            senderLabel.setStyle("-fx-background-color: " + Utils.backgroundColorHex(view.getPlayerColor(sender))
                    + ";  -fx-background-radius: 5; -fx-padding: 2");
        }
        if (!type.equals(MessageType.BROADCAST.toString()) && view.getPlayerColor(recipient) != null) {
            recipientLabel.setStyle("-fx-background-color: " + Utils.backgroundColorHex(view.getPlayerColor(recipient))
                    + ";  -fx-background-radius: 5; -fx-padding: 2");
        }

        //Set alignment
        if (sender.equals(view.getPlayerName()))
            this.setAlignment(Pos.CENTER_RIGHT);
    }

    @Override
    public String getFXMLFileName() {
        return "chat_message";
    }
}
