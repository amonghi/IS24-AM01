package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.EndingPlayerController;
import it.polimi.ingsw.am01.client.gui.event.SetFinalScoresEvent;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.*;

public class EndingController extends SceneController {
    private final Map<String, PlayerColor> playerColors;

    @FXML
    VBox scoreboard;

    public EndingController(View view) {
        super(view);
        playerColors = new HashMap<>();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(SetFinalScoresEvent.class, this::setFinalScores)
        );
    }

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

    @Override
    public String getFXMLFileName() {
        return "ending";
    }
}
