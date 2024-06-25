package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.model.chat.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * The controller for the chat
 * It has methods for sending and showing new messages
 *
 * @see ChatMessageController
 * @see it.polimi.ingsw.am01.client.View.Message
 */
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

    /**
     * It constructs a new ChatBoxController.
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
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
                            message,
                            view
                    )
            );
        }

        //Event handling
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                sendMessage();
        });
    }

    /**
     * It calls:
     * <ul>
     *     <li> {@link View#sendDirectMessage(String, String)} </li>
     *     <li> {@link View#sendBroadcastMessage(String)} </li>
     * </ul>
     * to send a message, if the {@link TextArea} is not empty
     */
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

    /**
     * It shows the new message in the chat box
     *
     * @param event The event received from the {@link View} containing the new {@link it.polimi.ingsw.am01.client.View.Message}
     */
    public void updateMessages(NewMessageEvent event) {
        messagesBox.getChildren().add(
                new ChatMessageController(
                        event.message(),
                        view
                )
        );
    }

    /**
     * It fills the {@link ChoiceBox} with the list of the player currently in the game, i.e. all the possible
     * recipient of a message
     *
     * @param event The event received from the {@link View} containing the list of players currently in the game
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "chat";
    }
}
