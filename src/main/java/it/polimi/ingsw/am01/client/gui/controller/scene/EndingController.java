package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.EndingPlayerController;
import it.polimi.ingsw.am01.client.gui.event.SetFinalScoresEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.*;

public class EndingController extends SceneController {

    private final List<ScorePlacement> scorePlacements;

    @FXML
    VBox scoreboard;

    @FXML
    Button backButton;

    public EndingController(){
        scorePlacements = new ArrayList<>();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(SetFinalScoresEvent.class, this::setFinalScores)
        );
    }

    private void setFinalScores(SetFinalScoresEvent event) {
        List<Map.Entry<String, Integer>> orderedScores = event.finalScores().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).toList();
        for (int i = 0; i < orderedScores.size(); i++) {
            String player = orderedScores.get(i).getKey();
            int points = orderedScores.get(i).getValue();
            int placement = i > 0 && scorePlacements.get(i-1).points() == points
                    ? scorePlacements.get(i-1).points()
                    : i + 1;
            scorePlacements.add(new ScorePlacement(player, points, placement));
        }

        for (ScorePlacement scorePlacement : scorePlacements){
            scoreboard.getChildren().add(
                    new EndingPlayerController(
                            scorePlacement.player(),
                            scorePlacement.points(),
                            scorePlacement.placement()
                            )
            );
        }
    }


    @Override
    public String getFXMLFileName() {
        return "ending";
    }


    private record ScorePlacement(String player, int points, int placement){}
}
