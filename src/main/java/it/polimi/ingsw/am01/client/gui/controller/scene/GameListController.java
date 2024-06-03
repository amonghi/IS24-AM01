package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.controller.popup.GameCreationPopupController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameListController extends SceneController {

    @FXML
    private VBox box;

    @FXML
    private Label playerNameLabel;

    @FXML
    private void initialize() {
        playerNameLabel.setText("Logged as " + View.getInstance().getPlayerName());
        box.getChildren().clear();
        for (Integer gameID : View.getInstance().getGames().keySet()) {
            if (View.getInstance().getGames().get(gameID).currentPlayersConnected() != View.getInstance().getGames().get(gameID).maxPlayers()) {
                box.getChildren().add(
                        new GameController(gameID, View.getInstance().getGames().get(gameID).maxPlayers(),
                                View.getInstance().getGames().get(gameID).currentPlayersConnected()
                        )
                );
            }
        }
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                View.getInstance().on(GameListChangedEvent.class, this::updateGameList)
        );
    }

    public void updateGameList(GameListChangedEvent event) {
        box.getChildren().clear();
        for (Integer gameID : event.gameStatMap().keySet()) {
            box.getChildren().add(
                    new GameController(gameID, event.gameStatMap().get(gameID).maxPlayers(), event.gameStatMap().get(gameID).currentPlayersConnected())
            );
        }
    }

    public void newGame() {
        openPopup(new GameCreationPopupController());
    }

    @Override
    public String getFXMLFileName() {
        return "games_list";
    }
}
