package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.PlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class LobbyController extends SceneController {
    @FXML
    private HBox playerList;

    @FXML
    private Label gameId;

    @FXML
    private Button startButton;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private Button openChatButton;
    @FXML
    private Button closeChatButton;
    private ChatBoxController chatBoxController;

    public LobbyController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(false);
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);
        closeChatButton.setVisible(false);

        gameId.setText("In game #" + view.getGameId());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(PlayerListChangedEvent.class, this::updatePlayerList),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void updatePlayerList(PlayerListChangedEvent event) {
        playerList.getChildren().clear();

        chatBoxController.updatePlayersList(event);

        startButton.setDisable(event.playerList().size() <= 1);

        for (String player : event.playerList()) {
            playerList.getChildren().add(PlayerSlotController.of(player));
        }
        int maxPlayers = view.getMaxPlayers();
        for (int i = 0; i < maxPlayers - event.playerList().size(); i++) {
            playerList.getChildren().add(PlayerSlotController.empty());
        }
    }

    @Override
    public String getFXMLFileName() {
        return "lobby";
    }

    @FXML
    private void start() {
        view.startGame();
    }

    @FXML
    private void openChat() {
        chatPane.setVisible(true);
        openChatButton.setVisible(false);
        closeChatButton.setVisible(true);
    }

    @FXML
    private void closeChat() {
        chatPane.setVisible(false);
        openChatButton.setVisible(true);
        closeChatButton.setVisible(false);
    }
}
