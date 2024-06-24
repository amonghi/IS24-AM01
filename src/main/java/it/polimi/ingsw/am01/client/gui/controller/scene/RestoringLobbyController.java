package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.RestoringPlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.SetPlayStatusEvent;
import it.polimi.ingsw.am01.model.game.GameStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The main controller for the scene associated to these {@link GameStatus} and {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link GameStatus#RESTORING} </li>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#IN_GAME} </li>
 * </ul>
 *
 * @see SceneController
 */
public class RestoringLobbyController extends SceneController {

    @FXML
    Label gameLabel;
    @FXML
    HBox playerList;
    @FXML
    Button resumeButton;

    /**
     * It constructs a new RestoringLobbyController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public RestoringLobbyController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        gameLabel.setText("Restoring game #" + view.getGameId());
    }

    /**
     * It shows the list of the players currently in game, either connected or not
     *
     * @param event The event received from the {@link View} containing the list of players
     *              currently in the game and their information: color, score and connection status
     */
    private void updatePlayerList(SetPlayStatusEvent event) {
        playerList.getChildren().clear();
        resumeButton.setDisable(event.players().stream().filter(view::isConnected).count() <= 1);

        for (String player : event.players()) {
            playerList.getChildren().add(new RestoringPlayerSlotController(
                    player,
                    event.colors().get(player),
                    event.connections().get(player),
                    view
            ));
        }
    }

    /**
     * It calls the {@link View#resumeGame()} method to resume the game.
     * This method can be called only when there are at least two connected players in the lobby
     */
    @FXML
    private void resume() {
        view.resumeGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(SetPlayStatusEvent.class, this::updatePlayerList)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "restoring_lobby";
    }
}
