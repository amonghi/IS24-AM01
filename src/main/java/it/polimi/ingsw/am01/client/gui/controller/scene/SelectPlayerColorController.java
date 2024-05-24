package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.ColorChoiceController;
import it.polimi.ingsw.am01.client.gui.event.UpdatePlayerColorEvent;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.message.c2s.SelectColorC2S;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerColorController extends SceneController {

    private static final int HIGHER_STROKE_WIDTH = 8;

    private static final int LOWER_STROKE_WIDTH = 1;

    private final List<ColorChoiceController> colorChoiceControllers = new ArrayList<>();
    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private VBox playersBox;
    @FXML
    private Button yelloButton;
    @FXML
    private Button redButton;
    @FXML
    private Button blueButton;
    @FXML
    private Button greenButton;
    @FXML
    private Circle yelloCircle;
    @FXML
    private Circle redCircle;
    @FXML
    private Circle blueCircle;
    @FXML
    private Circle greenCircle;

    private PlayerColor colorChoice;

    @FXML
    private void initialize() {
        gameId.setText("In game #" + GUIView.getInstance().getGameId());
        GUIView.getInstance().getPlayersInGame().forEach(player -> {
            ColorChoiceController controller = new ColorChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            colorChoiceControllers.add(controller);
        });
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(UpdatePlayerColorEvent.class, this::updatePlayersColor)
        );
    }

    @FXML
    private void openChat() {
        //TODO: todo
    }

    @FXML
    private void confirm(Event event) {
        if (event.getSource().equals(yelloButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.YELLOW)) {
                return;
            }

            setCirclesStrokes(yelloCircle);
            colorChoice = PlayerColor.YELLOW;

        } else if (event.getSource().equals(redButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.RED)) {
                return;
            }

            setCirclesStrokes(redCircle);
            colorChoice = PlayerColor.RED;

        } else if (event.getSource().equals(blueButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.BLUE)) {
                return;
            }

            setCirclesStrokes(blueCircle);
            colorChoice = PlayerColor.BLUE;

        } else if (event.getSource().equals(greenButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.GREEN)) {
                return;
            }

            setCirclesStrokes(greenCircle);
            colorChoice = PlayerColor.GREEN;
        }

        GUIView.getInstance().sendMessage(
                new SelectColorC2S(
                        colorChoice
                )
        );
    }

    private void setCirclesStrokes(Circle selectedCircle) {
        yelloCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        blueCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        redCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        greenCircle.setStrokeWidth(LOWER_STROKE_WIDTH);

        selectedCircle.setStrokeWidth(HIGHER_STROKE_WIDTH);
    }

    private void updatePlayersColor(UpdatePlayerColorEvent event) {
        for (ColorChoiceController controller : colorChoiceControllers) {
            if (controller.getPlayerName().equals(event.playerName())) {
                controller.setChoice(event.playerColor());
            }
        }
    }

    @Override
    public String getFXMLFileName() {
        return "color_choice_phase";
    }
}
