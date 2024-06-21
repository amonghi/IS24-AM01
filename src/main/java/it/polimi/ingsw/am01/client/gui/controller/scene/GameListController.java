package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.controller.popup.GameCreationPopupController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class GameListController extends SceneController {
    @FXML
    private VBox box;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView newGameIcon;

    public GameListController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        Tooltip.install(newGameIcon, new Tooltip("Click here to create a new game"));
        playerNameLabel.setText("Logged as " + view.getPlayerName());
        box.getChildren().clear();
        for (Integer gameID : view.getGames().keySet()) {
            if (view.getGames().get(gameID).currentPlayersConnected() != view.getGames().get(gameID).maxPlayers()) {
                box.getChildren().add(
                        new GameController(gameID,
                                view.getGames().get(gameID).maxPlayers(),
                                view.getGames().get(gameID).currentPlayersConnected(),
                                view
                        )
                );
            }
        }
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(GameListChangedEvent.class, this::updateGameList)
        );
    }

    public void updateGameList(GameListChangedEvent event) {
        box.getChildren().clear();
        for (Integer gameID : event.gameStatMap().keySet()) {
            box.getChildren().add(
                    new GameController(gameID,
                            event.gameStatMap().get(gameID).maxPlayers(),
                            event.gameStatMap().get(gameID).currentPlayersConnected(),
                            view
                    )
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
