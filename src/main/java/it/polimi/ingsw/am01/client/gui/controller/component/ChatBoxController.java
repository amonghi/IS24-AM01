package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChatBoxController extends AnchorPane implements ComponentController {

    private final View view;
    @FXML
    private VBox messagesBox;
    @FXML
    private TextArea messageInput;
    @FXML
    private ChoiceBox<String> recipientOptionsChoiceBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane chatPane;

    public ChatBoxController(View view) {
        this.view = view;
        loadComponent();
    }

    @FXML
    private void initialize() {
        AnchorPane.setBottomAnchor(chatPane, 0.0);
        AnchorPane.setRightAnchor(chatPane, 0.0);
        AnchorPane.setTopAnchor(chatPane, 0.0);

        //auto scroll down the ScrollPane
        scrollPane.vvalueProperty().bind(messagesBox.heightProperty());

        for (String player : view.getPlayersInGame()) {
            if (!player.equals(view.getPlayerName())) {
                recipientOptionsChoiceBox.getItems().add(player);
            }
        }
        recipientOptionsChoiceBox.getItems().add(MessageType.BROADCAST.toString());
        recipientOptionsChoiceBox.getSelectionModel().selectLast();

        for (View.Message message : view.getMessages()) {
            messagesBox.getChildren().add(
                    new ChatMessageController(
                            message.type().toString(),
                            message.sender(),
                            message.recipient(),
                            message.content(),
                            message.timestamp(),
                            view
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
            view.sendBroadcastMessage(messageInput.getText());
        } else {
            view.sendDirectMessage(recipientOptionsChoiceBox.getValue(), messageInput.getText());
        }
        messageInput.setText("");
    }

    public void updateMessages(NewMessageEvent event) {
        messagesBox.getChildren().add(
                new ChatMessageController(
                        event.message().type().toString(),
                        event.message().sender(),
                        event.message().recipient(),
                        event.message().content(),
                        event.message().timestamp(),
                        view
                )
        );
    }

    public void updatePlayersList(PlayerListChangedEvent event) {
        String oldValue = recipientOptionsChoiceBox.getValue();

        recipientOptionsChoiceBox.getItems().clear();
        for (String player : event.playerList()) {
            if (!player.equals(view.getPlayerName())) {
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
