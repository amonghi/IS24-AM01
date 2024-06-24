package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.GameController;
import it.polimi.ingsw.am01.client.gui.controller.popup.GameCreationPopupController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Map;

public class GameListController extends SceneController {
    @FXML
    private VBox box;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView newGameIcon;
    @FXML
    private Label messageLabel;

    public GameListController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        Tooltip.install(newGameIcon, new Tooltip("Click here to create a new game"));
        playerNameLabel.setText("Logged as " + view.getPlayerName());
        updateListOnView(view.getGames());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(GameListChangedEvent.class, this::updateGameList)
        );
    }

    private void updateListOnView(Map<Integer, UpdateGameListS2C.GameStat> gameStatMap) {
        box.getChildren().clear();

        if (gameStatMap.isEmpty()) {
            messageLabel.setVisible(true);
            return;
        }

        messageLabel.setVisible(false);
        for (Integer gameID : gameStatMap.keySet()) {
            box.getChildren().add(
                    new GameController(gameID,
                            gameStatMap.get(gameID).maxPlayers(),
                            gameStatMap.get(gameID).currentPlayersConnected(),
                            view
                    )
            );
        }
    }

    public void updateGameList(GameListChangedEvent event) {
        updateListOnView(event.gameStatMap());
    }

    public void newGame() {
        openPopup(new GameCreationPopupController());
    }

    @Override
    public String getFXMLFileName() {
        return "games_list";
    }
}
