package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.controller.popup.GameCreationPopupController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * The main controller for the scene associated to this {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#AUTHENTICATED} </li>
 * </ul>
 *
 * @see SceneController
 */
public class GameListController extends SceneController {
    @FXML
    private VBox box;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView newGameIcon;
    @FXML
    private Label messageLabel;

    /**
     * It constructs a new GameListController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public GameListController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        Tooltip.install(newGameIcon, new Tooltip("Click here to create a new game"));
        playerNameLabel.setText("Logged as " + view.getPlayerName());
        updateListOnView(view.getGames());
    }

    /**
     * It updates the list of the games a player can join.
     *
     * @param gameStatMap The map containing the information about the current players connected to
     *                    a game, and the maximum number of players allowed
     */
    private void updateListOnView(Map<Integer, UpdateGameListS2C.GameStat> gameStatMap) {
        box.getChildren().clear();

        if (gameStatMap.isEmpty()) {
            messageLabel.setVisible(true);
            return;
        }

        messageLabel.setVisible(false);
        for (Integer gameID : gameStatMap.keySet()) {
            box.getChildren().add(
                    new GameController(gameID,
                            gameStatMap.get(gameID).maxPlayers(),
                            gameStatMap.get(gameID).currentPlayersConnected(),
                            view
                    )
            );
        }
    }

    /**
     * It calls the {@link this#updateListOnView(Map)} method to show the available games a player can join
     *
     * @param event The event received from the {@link View} containing the list of games currently available
     */
    public void updateGameList(GameListChangedEvent event) {
        updateListOnView(event.gameStatMap());
    }

    /**
     * It shows a popup to create a new game
     */
    public void newGame() {
        openPopup(new GameCreationPopupController());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(GameListChangedEvent.class, this::updateGameList)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "games_list";
    }
}
