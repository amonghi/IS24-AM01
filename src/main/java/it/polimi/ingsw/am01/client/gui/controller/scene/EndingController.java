package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.EndingPlayerController;
import it.polimi.ingsw.am01.client.gui.event.SetFinalScoresEvent;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * The main controller for the scene associated to these {@link GameStatus} and {@link it.polimi.ingsw.am01.client.ClientState}:
 * <ul>
 *     <li> {@link GameStatus#FINISHED} </li>
 *     <li> {@link it.polimi.ingsw.am01.client.ClientState#IN_GAME} </li>
 * </ul>
 *
 * @see SceneController
 */
public class EndingController extends SceneController {
    private final Map<String, PlayerColor> playerColors;

    @FXML
    VBox scoreboard;

    /**
     * It constructs a new EndingController, calling the constructor of {@link SceneController}
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
    public EndingController(View view) {
        super(view);
        playerColors = new HashMap<>();
    }

    /**
     * It shows the final ranking, based on each player's score
     *
     * @param event The event received from the {@link View} containing the list of players along with their
     *              scores and colors.
     */
    private void setFinalScores(SetFinalScoresEvent event) {
        playerColors.clear();
        playerColors.putAll(event.playerColors());

        scoreboard.getChildren().clear();

        SortedMap<String, Integer> finalPlacements = view.getFinalPlacements();

        for (String player : event.finalScores().keySet()) {
            scoreboard.getChildren().add(
                    new EndingPlayerController(
                            player,
                            event.finalScores().get(player),
                            finalPlacements.get(player),
                            this.playerColors
                    )
            );
        }
    }

    @FXML
    private void goToGamesListScene() {
        view.exitFinishedGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(SetFinalScoresEvent.class, this::setFinalScores)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "ending";
    }
}
