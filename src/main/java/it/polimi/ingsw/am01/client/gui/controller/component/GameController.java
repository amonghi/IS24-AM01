package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * The controller for the component that shows a game and:
 * <ul>
 *     <li> The game id </li>
 *     <li> The maximum number of player for this game </li>
 *      <li> The number of the player currently connected to the game </li>
 * </ul>
 * in the list of join-able games
 */
public class GameController extends AnchorPane implements ComponentController {

    private final int gameID;
    private final int maxPlayers;
    private final int currPlayers;
    private final View view;

    @FXML
    private Label gameIDLabel;
    @FXML
    private Label playersLabel;
    @FXML
    private Button joinButton;

    /**
     * It constructs a new EndingPlayerController.
     * It also calls the {@link ComponentController#loadComponent()} method
     *
     * @param gameID      The id of the game to be shown in the list of join-able games
     * @param maxPlayers  The maximum number of players allowed in the game
     * @param currPlayers The number of players currently in the game
     * @param view        The main {@link View} class, containing the local and reduced copy of server data
     */
    public GameController(int gameID, int maxPlayers, int currPlayers, View view) {
        this.view = view;
        this.gameID = gameID;
        this.maxPlayers = maxPlayers;
        this.currPlayers = currPlayers;

        loadComponent();
    }

    @FXML
    private void initialize() {
        gameIDLabel.setText("Game #" + gameID);
        playersLabel.setText(currPlayers + "/" + maxPlayers + " players");
    }

    /**
     * It calls the {@link View#joinGame(int)}
     */
    @FXML
    private void join() {
        view.joinGame(gameID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "game";
    }
}