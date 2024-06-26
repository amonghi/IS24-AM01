package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.PlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.model.game.GameStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.am01.client.gui.controller.Utils.movePane;

/**
 * The main controller for the scene associated to these {@link GameStatus} and {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link GameStatus#AWAITING_PLAYERS} </li>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#IN_GAME} </li>
 * </ul>
 *
 * @see SceneController
 */
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
    private ImageView openChatIcon;
    @FXML
    private ImageView closeChatIcon;
    private ChatBoxController chatBoxController;

    /**
     * It constructs a new LobbyController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public LobbyController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(true);
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);
        closeChatIcon.setVisible(false);

        gameId.setText("In game #" + view.getGameId());
    }

    /**
     * It shows the list of the players currently in the lobby
     *
     * @param event The event received from the {@link View} containing the list of players currently in the lobby
     */
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

    /**
     * It calls the {@link View#startGame()} method to start the game.
     * This method can be called only when there are at least two players in the game
     */
    @FXML
    private void start() {
        view.startGame();
    }

    /**
     * It opens the pane showing the chat
     */
    @FXML
    private void openChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(0, chatPane);
        openChatIcon.setVisible(false);
        closeChatIcon.setVisible(true);
    }

    /**
     * It closes the pane showing the chat
     */
    @FXML
    private void closeChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(400, chatPane);
        openChatIcon.setVisible(true);
        closeChatIcon.setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(PlayerListChangedEvent.class, this::updatePlayerList),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "lobby";
    }
}
