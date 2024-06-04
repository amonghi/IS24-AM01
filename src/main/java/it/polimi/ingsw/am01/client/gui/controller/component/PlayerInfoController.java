package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.scene.PlayAreaController;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PlayerInfoController extends AnchorPane implements ComponentController {

    private String name;
    private PlayerColor color;
    private int score;
    private boolean connected;
    private final PlayAreaController playAreaController;
    @FXML
    private Button playerColor;
    @FXML
    private Label playerName;
    @FXML
    private Label points;
    @FXML
    private Label connectionState;

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
        playerColor.setStyle("-fx-background-color: " + getColor() + "; -fx-background-radius: 50%");
        points.setText(String.valueOf(score));
        playerName.setText(name);
        connectionState.setText(connected ? "connected" : "disconnected");
        initializeButtons();
    }

    private String getColor() {
        return switch (color) {
            case RED -> {
                yield "red";
            }
            case GREEN -> {
                yield "green";
            }
            case BLUE -> {
                yield "blue";
            }
            case YELLOW -> {
                yield "yellow";
            }
        };
    }

    private void initializeButtons() {
        playerColor.setOnAction(event -> {
            playAreaController.setCurrentView(playerName.getText());
        });
    }

    public String getName() {
        return name;
    }

    public void setScore(int newScore) {
        score = newScore;
        points.setText(String.valueOf(score));
    }

    @Override
    public String getFXMLFileName() {
        return "player_info";
    }
}
