package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.EndingPlayerController;
import it.polimi.ingsw.am01.client.gui.event.SetFinalScoresEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EndingController extends SceneController {

    private final List<ScorePlacement> scorePlacements;

    @FXML
    VBox scoreboard;

    public EndingController() {
        scorePlacements = new ArrayList<>();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(SetFinalScoresEvent.class, this::setFinalScores)
        );
    }

    private void setFinalScores(SetFinalScoresEvent event) {
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
                            scorePlacement.placement()
                    )
            );
        }
    }

    @FXML
    private void goToGamesListScene() {
        GUIView.getInstance().changeScene(GUIView.getInstance().GAME_LIST_CONTROLLER);
    }

    @Override
    public String getFXMLFileName() {
        return "ending";
    }

    private record ScorePlacement(String player, int points, int placement) {
    }
}
