package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.client.gui.controller.scene.PlayAreaController;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * The controller for the component that show the information of a player in the game:
 * <ul>
 *     <li> The player name </li>
 *     <li> The player score </li>
 *     <li> The player color </li>
 *     <li> The connection status of the player </li>
 * </ul>
 */
public class PlayerInfoController extends AnchorPane implements ComponentController {

    private final PlayAreaController playAreaController;
    private String name;
    private PlayerColor color;
    private int score;
    private boolean connected;
    @FXML
    private Text playerName;
    @FXML
    private Text playerScore;
    @FXML
    private ImageView connectionState;

    /**
     * It constructs a new PlayerInfoController.
     * It also calls the {@link ComponentController#loadComponent()} method
     *
     * @param name               The player name
     * @param color              The player color
     * @param score              The player score
     * @param connected          The player's connection status
     * @param playAreaController The controller of the play area, i.e. the scene where this component is placed
     */
    public PlayerInfoController(String name, PlayerColor color, int score, boolean connected, PlayAreaController playAreaController) {
        this.name = name;
        this.color = color;
        this.score = score;
        this.connected = connected;
        this.playAreaController = playAreaController;
        loadComponent();
    }

    @FXML
    private void initialize() {
        this.setStyle("-fx-background-color: " + Utils.backgroundColorHex(color) + "; -fx-background-radius: 20;");
        playerScore.setText(String.valueOf(score));
        playerName.setText(name);
        String state = connected ? "online" : "offline";
        connectionState.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + state + Constants.IMAGE_EXTENSION)).toString()));
        this.setOnMouseClicked(event -> playAreaController.setCurrentView(playerName.getText()));
    }

    /**
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * It sets and shows the new player score
     *
     * @param newScore The new score of the player, obtained after a placement
     */
    public void setScore(int newScore) {
        score = newScore;
        playerScore.setText(String.valueOf(score));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "player_info";
    }
}
