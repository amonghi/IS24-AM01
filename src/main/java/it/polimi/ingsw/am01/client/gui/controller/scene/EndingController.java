package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.EndingPlayerController;
import it.polimi.ingsw.am01.client.gui.event.SetFinalScoresEvent;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.*;

public class EndingController extends SceneController {
    private final List<ScorePlacement> scorePlacements;
    private final Map<String, PlayerColor> playerColors;

    @FXML
    VBox scoreboard;

    public EndingController(View view) {
        super(view);
        scorePlacements = new ArrayList<>();
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


        scorePlacements.clear();
        scoreboard.getChildren().clear();

        List<Map.Entry<String, Integer>> orderedScores = event.finalScores().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).toList();
        for (int i = 0; i < orderedScores.size(); i++) {
            String player = orderedScores.get(i).getKey();
            int points = orderedScores.get(i).getValue();
            int placement = i > 0 && scorePlacements.get(i - 1).points() == points
                    ? scorePlacements.get(i - 1).points()
                    : i + 1;
            scorePlacements.add(new ScorePlacement(player, points, placement));
        }

        for (ScorePlacement scorePlacement : scorePlacements) {
            scoreboard.getChildren().add(
                    new EndingPlayerController(
                            scorePlacement.player(),
                            scorePlacement.points(),
                            scorePlacement.placement(),
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

    private record ScorePlacement(String player, int points, int placement) {
    }
}
