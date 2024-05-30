package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.model.chat.MessageType;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.SendBroadcastMessageC2S;
import it.polimi.ingsw.am01.network.message.c2s.SendDirectMessageC2S;
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

        for (String player : GUIView.getInstance().getPlayersInGame()) {
            if (!player.equals(GUIView.getInstance().getPlayerName())) {
                recipientOptionsChoiceBox.getItems().add(player);
            }
        }
        recipientOptionsChoiceBox.getItems().add(MessageType.BROADCAST.toString());
        recipientOptionsChoiceBox.getSelectionModel().selectLast();

        for (GUIView.Message message : GUIView.getInstance().getMessages()) {
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

        C2SNetworkMessage netMessage;

        if (recipientOptionsChoiceBox.getValue().equals(MessageType.BROADCAST.toString())) {
            netMessage = new SendBroadcastMessageC2S(messageInput.getText());
        } else {
            netMessage = new SendDirectMessageC2S(
                    recipientOptionsChoiceBox.getValue(),
                    messageInput.getText()
            );
        }

        GUIView.getInstance().sendMessage(netMessage);
        messageInput.setText("");
    }

    public void updateMessages(NewMessageEvent event) {
        messagesBox.getChildren().add(
                new ChatMessageController(
                        event.message().type().toString(),
                        event.message().sender(),
                        event.message().recipient(),
                        event.message().content(),
                        event.message().timestamp()
                )
        );
    }

    public void updatePlayersList(PlayerListChangedEvent event) {
        String oldValue = recipientOptionsChoiceBox.getValue();

        recipientOptionsChoiceBox.getItems().clear();
        for (String player : event.playerList()) {
            if (!player.equals(GUIView.getInstance().getPlayerName())) {
                recipientOptionsChoiceBox.getItems().add(player);
            }
        }
        recipientOptionsChoiceBox.getItems().add("BROADCAST");

        if (oldValue != null && !event.playerList().contains(oldValue) && !oldValue.equals("BROADCAST")) {
            recipientOptionsChoiceBox.getSelectionModel().selectLast();
        } else {
            recipientOptionsChoiceBox.getSelectionModel().select(oldValue);
        }

        //FIXME: maybe useless
        if (oldValue == null) {
            recipientOptionsChoiceBox.getSelectionModel().selectLast();
        }
    }

    @Override
    public String getFXMLFileName() {
        return "chat";
    }
}
