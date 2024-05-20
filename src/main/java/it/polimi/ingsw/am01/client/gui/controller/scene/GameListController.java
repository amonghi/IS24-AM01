package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameListController extends SceneController {

    @FXML
    private VBox box;

    @FXML
    private Label playerNameLabel;

    public GameListController(GUIView view) {
        super(view);
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                getView().on(GameListChangedEvent.class, this::updateGameList)
        ));
    }

    public void updateGameList(GameListChangedEvent event) {
        box.getChildren().clear();
        for (Integer gameID : event.gameStatMap().keySet()) {
            box.getChildren().add(
                    new GameController(getView(), gameID, event.gameStatMap().get(gameID).maxPlayers(), event.gameStatMap().get(gameID).currentPlayersConnected())
            );
        }
    }
}
