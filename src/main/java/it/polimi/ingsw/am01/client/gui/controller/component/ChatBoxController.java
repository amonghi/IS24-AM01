package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChatBoxController extends AnchorPane implements ComponentController {

    @FXML
    private VBox messagesBox;
    @FXML
    private TextField messageInput;
    @FXML
    private ChoiceBox<String> recipientOptionsChoiceBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane chatPane;

    public ChatBoxController() {
        loadComponent();
    }

    @FXML
    private void initialize() {
        AnchorPane.setBottomAnchor(chatPane, 0.0);
        AnchorPane.setRightAnchor(chatPane, 0.0);
        AnchorPane.setTopAnchor(chatPane, 0.0);

        //auto scroll down the ScrollPane
        scrollPane.vvalueProperty().bind(messagesBox.heightProperty());

        for (String player : View.getInstance().getPlayersInGame()) {
            if (!player.equals(View.getInstance().getPlayerName())) {
                recipientOptionsChoiceBox.getItems().add(player);
            }
        }
        recipientOptionsChoiceBox.getItems().add(MessageType.BROADCAST.toString());
        recipientOptionsChoiceBox.getSelectionModel().selectLast();

        for (View.Message message : View.getInstance().getMessages()) {
            messagesBox.getChildren().add(
                    new ChatMessageController(
                            message.type().toString(),
                            message.sender(),
                            message.recipient(),
                            message.content(),
                            message.timestamp()
                    )
            );
        }
    }

    @FXML
    private void sendMessage() {
        if (messageInput.getText().isEmpty()) {
            return;
        }

        if (recipientOptionsChoiceBox.getValue().equals(MessageType.BROADCAST.toString())) {
            View.getInstance().sendBroadcastMessage(messageInput.getText());
        } else {
            View.getInstance().sendDirectMessage(recipientOptionsChoiceBox.getValue(), messageInput.getText());
        }
        messageInput.setText("");
    }

    public void updateMessages(NewMessageEvent event) {
        Platform.runLater(() -> {
            messagesBox.getChildren().add(
                    new ChatMessageController(
                            event.message().type().toString(),
                            event.message().sender(),
                            event.message().recipient(),
                            event.message().content(),
                            event.message().timestamp()
                    )
            );
        });
    }

    public void updatePlayersList(PlayerListChangedEvent event) {
        String oldValue = recipientOptionsChoiceBox.getValue();

        recipientOptionsChoiceBox.getItems().clear();
        for (String player : event.playerList()) {
            if (!player.equals(View.getInstance().getPlayerName())) {
                recipientOptionsChoiceBox.getItems().add(player);
            }
        }
        recipientOptionsChoiceBox.getItems().add(MessageType.BROADCAST.toString());

        if (oldValue == null) {
            recipientOptionsChoiceBox.getSelectionModel().selectLast();
            return;
        }

        if (!event.playerList().contains(oldValue) && !oldValue.equals(MessageType.BROADCAST.toString())) {
            recipientOptionsChoiceBox.getSelectionModel().selectLast();
        } else {
            recipientOptionsChoiceBox.getSelectionModel().select(oldValue);
        }
    }

    @Override
    public String getFXMLFileName() {
        return "chat";
    }
}
